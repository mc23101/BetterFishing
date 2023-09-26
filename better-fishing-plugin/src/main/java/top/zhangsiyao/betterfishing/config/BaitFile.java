package top.zhangsiyao.betterfishing.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.constant.BaitKey;
import top.zhangsiyao.betterfishing.item.BaitItem;

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

        if(!baitConfig.contains(BaitKey.bait_root)){
            return;
        }

        for(String baitName:baitConfig.getConfigurationSection(BaitKey.bait_root).getKeys(false)){
            if(BetterFishing.baitMap.containsKey(baitName)){
                throw new RuntimeException(baitFile.getPath()+"中的名称："+baitName+"已被占用");
            }
            BaitItem baitItem=new BaitItem(baitName,baitConfig,baitFile);
            BetterFishing.baitMap.put(baitName,baitItem);
        }
    }


    public FileConfiguration getConfig() {
        if (this.baitConfig == null) reload();
        return this.baitConfig;
    }
}
