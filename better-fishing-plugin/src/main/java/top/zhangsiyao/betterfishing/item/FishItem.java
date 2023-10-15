package top.zhangsiyao.betterfishing.item;

import lombok.Data;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.constant.BaitKey;
import top.zhangsiyao.betterfishing.constant.FishKey;
import top.zhangsiyao.betterfishing.constant.RodKey;
import top.zhangsiyao.betterfishing.exceptions.InvalidFishException;
import top.zhangsiyao.betterfishing.reward.Reward;
import top.zhangsiyao.betterfishing.utils.BFWorthNBT;
import top.zhangsiyao.betterfishing.utils.TextUtils;
import top.zhangsiyao.betterfishing.utils.ItemFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@Getter
public class FishItem  extends AbstractItem {

    File file;

    ItemFactory itemFactory;

    BRarity rarity;
    
    String effect;

    Double minPoint;

    Double weight;

    Double maxPoint;

    Float length;

    Boolean interactType;

    List<Reward> eatRewards=new ArrayList<>();

    List<Reward> interactRewards=new ArrayList<>();

    FileConfiguration fishConfig;

    public FishItem(String name, FileConfiguration fishConfig, File file) {
        this.name = name;
        this.fishConfig = fishConfig;
        this.file=file;
        loadConfigs();
        itemFactory=new ItemFactory(this,file);
    }

    private ConfigurationSection getSection(){
        return fishConfig.getConfigurationSection(FishKey.root).getConfigurationSection(name);
    }

    public ItemStack give(Player player, int randomIndex) {
        ItemStack fish = itemFactory.createItem(player, randomIndex);

        ItemMeta fishMeta;

        if ((fishMeta = fish.getItemMeta()) != null) {
            if (displayName != null) fishMeta.setDisplayName(TextUtils.translateHexColorCodes(displayName));
            else fishMeta.setDisplayName(TextUtils.translateHexColorCodes(rarity.getColour() + name));

            fishMeta.setLore(getLore());

            fishMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            fishMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            fishMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            fish.setItemMeta(fishMeta);
            fish = BFWorthNBT.setFishNBT(fish, this);
        }
        return fish;
    }


    private void loadConfigs(){
        ConfigurationSection root = Objects.requireNonNull(fishConfig.getConfigurationSection(FishKey.root)).getConfigurationSection(name);
        if(root==null){
            return;
        }
        weight=root.getDouble(FishKey.weight,10.0);
        displayName=root.getString(FishKey.display_name,name);
        lore=root.getStringList(FishKey.lore);
        durability=root.getInt(FishKey.durability,0);
        List<String> eatrewards = root.getStringList(FishKey.eat_event);
        for(String reward:eatrewards){
            eatRewards.add(new Reward(reward));
        }
        effect=root.getString(FishKey.effect);
        List<String> intRewards = getSection().getStringList(FishKey.interact_event);
        for(String reward:intRewards){
            interactRewards.add(new Reward(reward));
        }
        itemProperties=new ItemProperties(FishKey.root,name,fishConfig);
        glowing=root.getBoolean(FishKey.glowing,false);
        maxPoint=root.getDouble(FishKey.maxPoint);
        minPoint=root.getDouble(FishKey.minPoint);
        String r=root.getString(FishKey.rarity);
        if(r!=null){
            if(BetterFishing.rarityMap.containsKey(r)){
                rarity= BetterFishing.rarityMap.get(r);
            }else {
                try {
                    throw new InvalidFishException(fishConfig.getName()+"文件中的"+name+"中的稀有度："+r+"不存在");
                } catch (InvalidFishException e) {
                    e.printStackTrace();
                }
            }
        }else {
            throw new RuntimeException(file.getPath()+"中的鱼："+name+"未指定稀有度.");
        }
        interactType=root.getBoolean(FishKey.interact_type,false);
        unbreakable=root.getBoolean(FishKey.unbreakable,false);
        if (root.contains(FishKey.model)) {
            model=root.getInt(FishKey.model);
        }
    }


    @Override
    public int hashCode(){
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        FishItem fishItem=(FishItem) obj;
        return this.name.equals(fishItem.getName());
    }



}
