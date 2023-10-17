package top.zhangsiyao.betterfishing.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import top.zhangsiyao.betterfishing.BetterFishing;

import java.io.File;
import java.io.IOException;

public class AttachmentSlotFile implements FileConfig{
    private final BetterFishing plugin;
    private FileConfiguration attachmentSlotConfig;

    public AttachmentSlotFile(BetterFishing plugin) {
        this.plugin = plugin;
    }

    public void reload(){
        File attachmentslotFile = new File(this.plugin.getDataFolder(), "attachmentSlot.yml");

        if (!attachmentslotFile.exists()) {
            attachmentslotFile.getParentFile().mkdirs();
            this.plugin.saveResource("attachmentSlot.yml", false);
        }

        this.attachmentSlotConfig = new YamlConfiguration();

        try {
            this.attachmentSlotConfig.load(attachmentslotFile);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
