package top.zhangsiyao.betterfishing.config;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import top.zhangsiyao.betterfishing.BetterFishing;

import java.util.List;
import java.util.logging.Level;

public class ConfigFile {

    private final BetterFishing plugin;
    private FileConfiguration config;

    public ConfigFile(BetterFishing plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        config = plugin.getConfig();
        // 初始化全局api
        BetterFishing.usingPAPI = plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;


        if (isEconomyEnabled()) {
            // could not set up economy.
            if (!setupEconomy()) {
                BetterFishing.logger.log(Level.WARNING, "BetterFishing won't be hooking into economy. If this wasn't by choice in config.yml, please install Economy handling plugins.");
            }
        }

        if (!setupPermissions()) {
            Bukkit.getServer().getLogger().log(Level.SEVERE, "BetterFishing couldn't hook into Vault permissions. Disabling to prevent serious problems.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

        // checks against both support region plugins and sets an active plugin (worldguard is priority)
        if (checkWG()) {
            BetterFishing.guardPL = "worldguard";
        } else if (checkRP()) {
            BetterFishing.guardPL = "redprotect";
        }

        BetterFishing.wgPlugin = getWorldGuard();


        checkPapi();

        initDatabase();

    }

    private void initDatabase(){

    }

    /* Gets the worldguard plugin, returns null and assumes the player has this functionality disabled if it
   can't find the plugin. */
    private WorldGuardPlugin getWorldGuard() {
        return (WorldGuardPlugin) plugin.getServer().getPluginManager().getPlugin("WorldGuard");
    }

    private void checkPapi() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            BetterFishing.papi = true;
//            new PlaceholderReceiver(plugin).register();
        }

    }

    private boolean checkRP() {
        Plugin pRP = Bukkit.getPluginManager().getPlugin("RedProtect");
        return (pRP != null);
    }

    private boolean checkWG() {
        Plugin pWG = Bukkit.getPluginManager().getPlugin("WorldGuard");
        return (pWG != null);
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        BetterFishing.permission = rsp.getProvider();
        return BetterFishing.permission != null;
    }


    private boolean setupEconomy() {
        if (isEconomyEnabled()) {
            RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                return false;
            }
            BetterFishing.econ = rsp.getProvider();
            return BetterFishing.econ != null;
        } else {
            return false;
        }

    }


    public FileConfiguration getConfig() {
        return this.config;
    }


    public boolean doingRandomDurability() {
        return config.getBoolean("random-durability");
    }


    public boolean worldWhitelist() {
        return !config.getStringList("allowed-worlds").isEmpty();
    }

    public List<String> getAllowedRegions() {
        return config.getStringList("allowed-regions");
    }

    public List<String> getAllowedWorlds() {
        return config.getStringList("allowed-worlds");
    }

    public boolean isEconomyEnabled() {
        return config.getBoolean("enable-economy");
    }

    public Integer getFishingMaxWaitTime(){
        return config.getInt("fishing.maxWaitTime", 600);
    }

    public Integer getFishingMinWaitTime(){
        return config.getInt("fishing.minWaitTime", 200);
    }
}
