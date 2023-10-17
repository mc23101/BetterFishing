package top.zhangsiyao.betterfishing.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import top.zhangsiyao.betterfishing.BetterFishing;

import java.io.File;
import java.io.IOException;

public class AttachmentFile implements FileConfig{

    private final BetterFishing plugin;

    private FileConfiguration attachmentConfig;

    public AttachmentFile(BetterFishing plugin) {
        this.plugin = plugin;
        reload();
    }

    @Override
    public void reload() {
        File attachmentslotFile = new File(this.plugin.getDataFolder(), "attachment.yml");

        if (!attachmentslotFile.exists()) {
            attachmentslotFile.getParentFile().mkdirs();
            this.plugin.saveResource("attachment.yml", false);
        }

        this.attachmentConfig = new YamlConfiguration();

        try {
            this.attachmentConfig.load(attachmentslotFile);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }


    }
}
