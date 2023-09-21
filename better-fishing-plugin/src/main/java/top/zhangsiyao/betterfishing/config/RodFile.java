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

public class RodFile {

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
        loadRods();
        BetterFishing.rodFile=this;
    }


    private void loadRods(){
        Map<String,Rod> rodMap=new HashMap<>();
        Set<String> rods = this.rodConfig.getRoot().getKeys(false);
        for (String rod : rods) {
            Rod rodEntity = new Rod();
            rodEntity.setRodName(rod);
            rodEntity.setDisplay(getDisplayName(rod));
            rodEntity.setLore(getLore(rod));
            rodEntity.setFishingSpeed(getFishingSpeed(rod));
            rodEntity.setDoubleDrop(getDoubleDrop(rod));
            rodEntity.setExtraFish(getExtraFish(rod));
            rodEntity.setRarities(getRarities(rod));
            rodMap.put(rod,rodEntity);
        }
        BetterFishing.rodMap=rodMap;
    }

    /**
     * 获取鱼竿displayName
     * */
    private String getDisplayName(String rodName){
        return rodConfig.getConfigurationSection(rodName).getString("display",rodName);
    }


    /**
     * 获取鱼竿的lore标签
     * */
    private List<String> getLore(String rodName){
        return (List<String>) this.rodConfig.getConfigurationSection(rodName).getList("lore",new ArrayList<>());
    }


    /**
     * 获取鱼竿额外凋落物的配置文件名称
     * */
    private String getExtraFish(String rodName){
        if (rodConfig.getConfigurationSection(rodName).contains(NbtConstant.EXTRA_FISH)){
            String value=rodConfig.getConfigurationSection(rodName).getString(NbtConstant.EXTRA_FISH);
            if(BetterFishing.allFishes.containsKey(value)){
                throw new RuntimeException("名为 "+value+" 的额外掉落物配置文件不存在");
            }else {
                return value;
            }
        }else {
            return null;
        }
    }

    private String getDoubleDrop(String rodName){
        if(rodConfig.getConfigurationSection(rodName).contains(NbtConstant.DOUBLE_DROP)){
            String value=rodConfig.getConfigurationSection(rodName).getString(NbtConstant.DOUBLE_DROP);
            Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
            Matcher isNum = pattern.matcher(value);
            if(!isNum.matches()){
                throw new RuntimeException();
            }else {
                float f=Float.parseFloat(value);
                if(f<0||f>1){
                    throw new RuntimeException();
                }
            }
            return value;
        }else {
            return null;
        }
    }

    private String getFishingSpeed(String rodName){
        if(rodConfig.getConfigurationSection(rodName).contains(NbtConstant.FISHING_SPEED)){
            String value=rodConfig.getConfigurationSection(rodName).getString(NbtConstant.FISHING_SPEED);
            Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
            Matcher isNum = pattern.matcher(value);
            if(!isNum.matches()){
                throw new RuntimeException();
            }else {
                float f=Float.parseFloat(value);
                if(f<0||f>1){
                    throw new RuntimeException();
                }
            }
            return value;
        }else {
            return null;
        }
    }

    private Map<String,Integer> getRarities(String rodName){
        Map<String, Object> values = rodConfig.getConfigurationSection(rodName+".rarities").getValues(false);
        Map<String,Integer> result=new HashMap<>();
        for(String key:values.keySet()){
            result.put(key,Integer.parseInt(String.valueOf(values.get(key))));
        }
        return result;
    }
}
