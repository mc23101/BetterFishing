package top.zhangsiyao.betterfishing.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.constant.NbtConstant;
import top.zhangsiyao.betterfishing.item.Rod;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RodFile implements FileConfig{

    private final BetterFishing plugin;
    private FileConfiguration rodConfig;

    public RodFile(BetterFishing plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload(){
        File rodsFile = new File(this.plugin.getDataFolder(), "rods.yml");

        if (!rodsFile.exists()) {
            rodsFile.getParentFile().mkdirs();
            this.plugin.saveResource("rods.yml", false);
        }

        this.rodConfig = new YamlConfiguration();

        try {
            this.rodConfig.load(rodsFile);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Map<String,Rod> rodMap=new HashMap<>();
        Set<String> rods = this.rodConfig.getConfigurationSection("rods").getKeys(false);
        for (String rod : rods) {
            Rod rodEntity = new Rod(rod,rodConfig,rodsFile);
            rodMap.put(rod,rodEntity);
        }
        BetterFishing.rodMap=rodMap;
    }
}
