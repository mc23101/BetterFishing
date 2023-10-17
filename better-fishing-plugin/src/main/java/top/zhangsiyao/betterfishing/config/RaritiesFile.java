package top.zhangsiyao.betterfishing.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.item.BRarity;

import java.io.File;
import java.io.IOException;

public class RaritiesFile implements FileConfig{

    private final BetterFishing plugin;
    private FileConfiguration raritiesConfig;

    public RaritiesFile(BetterFishing plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {

        File raritiesFile = new File(this.plugin.getDataFolder(), "rarities.yml");

        if (!raritiesFile.exists()) {
            raritiesFile.getParentFile().mkdirs();
            this.plugin.saveResource("rarities.yml", false);
        }

        this.raritiesConfig = new YamlConfiguration();

        try {
            this.raritiesConfig.load(raritiesFile);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
        BetterFishing.raritiesFile = this;

        for(String rarityName:raritiesConfig.getConfigurationSection("rarities").getKeys(false)){
            BRarity BRarity =new BRarity(rarityName,raritiesConfig);
            BetterFishing.rarityMap.put(rarityName, BRarity);
        }
    }

    public FileConfiguration getConfig() {
        if (this.raritiesConfig == null) reload();
        return this.raritiesConfig;
    }
}
