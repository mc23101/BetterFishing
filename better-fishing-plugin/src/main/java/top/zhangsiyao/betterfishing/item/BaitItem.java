package top.zhangsiyao.betterfishing.item;

import com.gmail.nossr50.datatypes.treasure.Rarity;
import lombok.Data;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.exceptions.InvalidFishException;
import top.zhangsiyao.betterfishing.reward.Reward;
import top.zhangsiyao.betterfishing.utils.ItemFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
public class BaitItem implements AbstractItem{

    ItemFactory itemFactory;

    File file;

    ItemProperties itemProperties;

    String displayName;

    List<String> lore;

    List<FishItem> fish;

    List<BRarity> rarity;

    Boolean glowing;

    Integer durability;

    String baitName;

    Double fishWeight;

    Double rarityWeight;



    FileConfiguration baitConfig;

    public BaitItem(String name, FileConfiguration baitConfig,File file) {
        this.baitName = name;
        this.baitConfig = baitConfig;
        this.file=file;
        loadFishWeight();
        loadRarityWeight();
        loadDisplayName();
        loadLore();;
        loadDurability();
        loadFish();
        loadItemProperties();
        loadGlowing();
        loadRarities();
        itemFactory=new ItemFactory(this,file);
    }

    private ConfigurationSection getSection(){
        return baitConfig.getConfigurationSection("baits."+baitName);
    }

    private void loadFishWeight(){
        fishWeight=getSection().getDouble("fish-weight",10.0);
    }

    private void loadRarityWeight(){
        rarityWeight=getSection().getDouble("rarity-weight",10.0);
    }

    private void loadDisplayName(){
        displayName=getSection().getString("displayName",baitName);
    }

    private void loadLore(){
        lore=getSection().getStringList("lore");
    }

    private void loadDurability(){
        durability=getSection().getInt("durability");
    }

    private void loadFish(){
        List<FishItem> fish=new ArrayList<>();
        for(String fishName:getSection().getStringList("fish")){
            if(BetterFishing.allFishes.containsKey(fishName)){
                fish.add(BetterFishing.allFishes.get(fishName));
            }else {
                throw new RuntimeException(file.getPath()+"中的"+baitName+".fish："+fishName+"该鱼不存在");
            }
        }
        this.fish=fish;
    }

    private void loadItemProperties(){
        itemProperties=new ItemProperties("baits",baitName,baitConfig);
    }

    private void loadGlowing(){
        glowing=getSection().getBoolean("glowing",false);
    }


    private void loadRarities(){
        List<BRarity> rarities=new ArrayList<>();
        for(String rarityName:getSection().getStringList("rarity")){
            if(BetterFishing.rarityMap.containsKey(rarityName)){
                rarities.add(BetterFishing.rarityMap.get(rarityName));
            }else{
                throw new RuntimeException(file.getPath()+"中的"+baitName+"rarity："+rarityName+"该稀有度不存在");
            }
        }
    }

}
