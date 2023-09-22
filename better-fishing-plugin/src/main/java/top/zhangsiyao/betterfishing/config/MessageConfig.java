package top.zhangsiyao.betterfishing.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.utils.ColorUtils;

import java.io.File;
import java.io.IOException;

public class MessageConfig {

    private final BetterFishing plugin;

    private FileConfiguration messageConfig;

    public MessageConfig(BetterFishing plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload(){
        File baitFile = new File(this.plugin.getDataFolder(), "message.yml");

        if (!baitFile.exists()) {
            baitFile.getParentFile().mkdirs();
            this.plugin.saveResource("message.yml", false);
        }

        this.messageConfig = new YamlConfiguration();

        try {
            this.messageConfig.load(baitFile);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取鱼竿诱饵槽的lore
     * */
    public String getRodBaitSlot(String baitName){
        return ColorUtils.translateHexColorCodes(messageConfig.getString("rod-bait-slot").replaceAll("\\{baitName}",baitName));
    }

    /**
     * 获取双倍掉落的提示消息
     * */
    public String getDoubleDropMessage(){
        return ColorUtils.translateHexColorCodes(messageConfig.getString("double-drop-message"));
    }
    /**
     * 获取鱼饵数量不足的提示消息
     * */
    public String getBaitNotEnoughMessage(String baitName){
        return ColorUtils.translateHexColorCodes(messageConfig.getString("bait-not-enough").replaceAll("\\{baitName}",baitName));
    }


    /**
     * 获取鱼饵不存在的提示消息
     * */
    public String getBaitNotExistMessage(){
        return ColorUtils.translateHexColorCodes(messageConfig.getString("bait-not-exist"));
    }


}
