package top.zhangsiyao.betterfishing.config;

import de.tr7zw.annotations.FAUtil;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.item.BRarity;
import top.zhangsiyao.betterfishing.item.FishItem;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FishFile implements FileConfig{

    private final BetterFishing plugin;
    private FileConfiguration fishConfig;

    private FileConfiguration globalFishConfig;


    public FishFile(BetterFishing plugin) {
        this.plugin = plugin;
        reload();
    }

    // Makes sure all th
    public void reload() {
        loadGlobalFish();
        loadExtraFish();
    }

    private void loadGlobalFish(){
        File fishFile = new File(this.plugin.getDataFolder(), "globalfish.yml");

        if (!fishFile.exists()) {
            fishFile.getParentFile().mkdirs();
            this.plugin.saveResource("globalfish.yml", false);
        }

        this.globalFishConfig = new YamlConfiguration();

        try {
            this.globalFishConfig.load(fishFile);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }

        for(String fishName:globalFishConfig.getConfigurationSection("fish").getKeys(false)){
            if(BetterFishing.allFishes.containsKey(fishName)){
                throw new RuntimeException(fishFile.getPath()+"中的掉落物"+ fishName+ "名称已被占用，请更换其他名称");
            }
            FishItem fishItem=new FishItem(fishName,globalFishConfig,fishFile);
            BetterFishing.globalFishes.put(fishName,fishItem);
            BetterFishing.allFishes.put(fishName,fishItem);
            if(BetterFishing.globalRarityFishes.containsKey(fishItem.getRarity())){
                BetterFishing.globalRarityFishes.get(fishItem.getRarity()).add(fishItem);
            }else {
                BetterFishing.globalRarityFishes.put(fishItem.getRarity(), new ArrayList<>(Collections.singletonList(fishItem)));
            }
        }

    }

    @SneakyThrows
    private void loadExtraFish(){
        File dir=new File(plugin.getDataFolder(),"extrafish");
        if(!dir.exists()){
            dir.mkdirs();
        }
        for(File file: Objects.requireNonNull(dir.listFiles())){

            FileConfiguration fileConfiguration=new YamlConfiguration();
            fileConfiguration.load(file);

            String name= fileConfiguration.getString("name");

            if(name==null||name.length()==0){
                throw new RuntimeException("请配置"+file.getPath()+"中的name值");
            }

            if(BetterFishing.extraRarityFishes.containsKey(name)){
                throw new RuntimeException(file.getPath()+"中的name："+name+"的值已被占用，请更换其他值");
            }
            Map<BRarity, List<FishItem>> map = new HashMap<>();
            for(String fishName:fileConfiguration.getConfigurationSection("fish").getKeys(false)){
                if(BetterFishing.allFishes.containsKey(fishName)){
                    throw new RuntimeException(file.getPath()+"中的掉落物"+ fishName+ "名称已被占用，请更换其他名称");
                }
                FishItem fishItem=new FishItem(fishName,fileConfiguration,file);
                BetterFishing.allFishes.put(fishName,fishItem);
                BetterFishing.extraFishes.put(fishName,fishItem);
                if(map.containsKey(fishItem.getRarity())){
                    map.get(fishItem.getRarity()).add(fishItem);
                }else {
                    map.put(fishItem.getRarity(),new ArrayList<>(Collections.singletonList(fishItem)));
                }
            }
            BetterFishing.extraRarityFishes.put(name,map);
        }
    }

    public FileConfiguration getConfig() {
        if (this.fishConfig == null) reload();
        return this.fishConfig;
    }
}
