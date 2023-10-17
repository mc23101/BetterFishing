package top.zhangsiyao.betterfishing.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.utils.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageFile {

    private final BetterFishing plugin;

    private FileConfiguration messageConfig;

    public MessageFile(BetterFishing plugin) {
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
        return TextUtils.translateHexColorCodes(messageConfig.getString("rod-bait-slot").replaceAll("\\{baitName}",baitName));
    }

    /**
     * 获取双倍掉落的提示消息
     * */
    public String getDoubleDropMessage(){
        return TextUtils.translateHexColorCodes(messageConfig.getString("double-drop-message"));
    }
    /**
     * 获取鱼饵数量不足的提示消息
     * */
    public String getBaitNotEnoughMessage(String baitName){
        return TextUtils.translateHexColorCodes(messageConfig.getString("bait-not-enough").replaceAll("\\{baitName}",baitName));
    }


    /**
     * 获取鱼饵不存在的提示消息
     * */
    public String getBaitNotExistMessage(){
        return TextUtils.translateHexColorCodes(messageConfig.getString("bait-not-exist"));
    }

    public String get(String messageKey){
        return TextUtils.translateHexColorCodes(messageConfig.getString(messageKey,""));
    }

    public List<String> getList(String messageKey){
        List<String> result=new ArrayList<>();
        for(String s:messageConfig.getStringList(messageKey)){
            result.add(TextUtils.translateHexColorCodes(s));
        }
        return result;
    }


}
