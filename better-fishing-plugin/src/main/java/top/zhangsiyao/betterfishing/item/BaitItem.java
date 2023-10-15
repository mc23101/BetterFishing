package top.zhangsiyao.betterfishing.item;

import lombok.Data;
import lombok.EqualsAndHashCode;
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
import top.zhangsiyao.betterfishing.utils.BFWorthNBT;
import top.zhangsiyao.betterfishing.utils.TextUtils;
import top.zhangsiyao.betterfishing.utils.ItemFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
public class BaitItem extends AbstractItem {

    public static BaitItem empty=new BaitItem("无");

    ItemFactory itemFactory;

    File file;

    List<FishItem> fish;

    List<BRarity> rarity;

    Double fishWeight;

    Double rarityWeight;

    FileConfiguration baitConfig;

    private BaitItem(String name){
        this.name=name;
    }

    public BaitItem(String name, FileConfiguration baitConfig,File file) {
        this.name = name;
        this.baitConfig = baitConfig;
        this.file=file;
        loadConfigs();
        itemFactory=new ItemFactory(this,file);
    }

    
    @Override
    public ItemStack give(Player player, int randomIndex) {
        ItemStack bait = itemFactory.createItem(player, randomIndex);

        ItemMeta baitMeta;

        if ((baitMeta = bait.getItemMeta()) != null) {
            if (displayName != null) baitMeta.setDisplayName(TextUtils.translateHexColorCodes(displayName));
            else baitMeta.setDisplayName(TextUtils.translateHexColorCodes(name));

            baitMeta.setLore(getLore());

            baitMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            baitMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            baitMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            bait.setItemMeta(baitMeta);
            bait = BFWorthNBT.setBaitNBT(bait,this);
        }
        return bait;
    }

    private void loadConfigs(){
        ConfigurationSection root = Objects.requireNonNull(baitConfig.getConfigurationSection(BaitKey.bait_root)).getConfigurationSection(name);
        if (root == null) {
            return;
        }
        fishWeight=root.getDouble(BaitKey.fish_weight,10.0);
        rarityWeight=root.getDouble(BaitKey.rarity_weight,10.0);
        displayName=root.getString(BaitKey.display_name,name);
        lore=root.getStringList(BaitKey.lore);
        durability=root.getInt(BaitKey.durability,0);
        List<FishItem> fishItems=new ArrayList<>();
        for(String fishName:root.getStringList(BaitKey.fish)){
            if(BetterFishing.allFishes.containsKey(fishName)){
                fishItems.add(BetterFishing.allFishes.get(fishName));
            }else {
                throw new RuntimeException(file.getPath()+"中的"+name+".fish："+fishName+"该鱼不存在");
            }
        }
        fish=fishItems;
        itemProperties=new ItemProperties(BaitKey.bait_root,name,baitConfig);
        glowing=root.getBoolean(BaitKey.glowing,false);
        List<BRarity> rarities=new ArrayList<>();
        for(String rarityName:root.getStringList(BaitKey.rarity)){
            if(BetterFishing.rarityMap.containsKey(rarityName)){
                rarities.add(BetterFishing.rarityMap.get(rarityName));
            }else{
                throw new RuntimeException(file.getPath()+"中的"+name+"rarity："+rarityName+"该稀有度不存在");
            }
        }
        rarity=rarities;
        model=root.getInt(BaitKey.model);
        unbreakable=root.getBoolean(BaitKey.unbreakable,false);
        if (root.contains(BaitKey.model)) {
            model=root.getInt(BaitKey.model);
        }
    }
}
