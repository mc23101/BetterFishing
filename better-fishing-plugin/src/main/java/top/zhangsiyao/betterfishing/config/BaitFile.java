package top.zhangsiyao.betterfishing.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import top.zhangsiyao.betterfishing.BetterFishing;

import java.io.File;
import java.io.IOException;

public class BaitFile {

    private final BetterFishing plugin;
    private FileConfiguration baitConfig;

    public BaitFile(BetterFishing plugin) {
        this.plugin = plugin;
        reload();
    }

    // Makes sure all th
    public void reload() {

        File baitFile = new File(this.plugin.getDataFolder(), "baits.yml");

        if (!baitFile.exists()) {
            baitFile.getParentFile().mkdirs();
            this.plugin.saveResource("baits.yml", false);
        }

        this.baitConfig = new YamlConfiguration();

        try {
            this.baitConfig.load(baitFile);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }

        BetterFishing.baitFile = this;
    }

    public FileConfiguration getConfig() {
        if (this.baitConfig == null) reload();
        return this.baitConfig;
    }
}
