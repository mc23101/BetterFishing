package top.zhangsiyao.betterfishing.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.constant.AttachmentKey;
import top.zhangsiyao.betterfishing.item.Attachment;

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
        File attachmentFile = new File(this.plugin.getDataFolder(), "attachment.yml");

        if (!attachmentFile.exists()) {
            attachmentFile.getParentFile().mkdirs();
            this.plugin.saveResource("attachment.yml", false);
        }

        this.attachmentConfig = new YamlConfiguration();

        try {
            this.attachmentConfig.load(attachmentFile);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
        for(String name:attachmentConfig.getConfigurationSection(AttachmentKey.root).getKeys(false)){
            Attachment attachment = new Attachment(name, attachmentFile, attachmentConfig);
            BetterFishing.attachments.put(name,attachment);
        }

    }
}
