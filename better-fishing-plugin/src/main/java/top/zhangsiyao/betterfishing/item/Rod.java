package top.zhangsiyao.betterfishing.item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.constant.RodKey;
import top.zhangsiyao.betterfishing.utils.BFWorthNBT;
import top.zhangsiyao.betterfishing.utils.TextUtils;
import top.zhangsiyao.betterfishing.utils.ItemFactory;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EqualsAndHashCode(callSuper = true)
@Data
public class Rod extends AbstractItem {

    Double fishingSpeed;

    Double doubleDrop;

    Double mutualityExp=1.0;

    ItemFactory itemFactory;

    Map<String,Integer> rarities=new HashMap<>();

    String extraFish;
    Set<String> extraList=new HashSet<>();

    FileConfiguration rodConfig;

    File file;



    public Rod(String name, FileConfiguration rodConfig, File file) {
        this.name=name;
        this.rodConfig=rodConfig;
        this.file=file;
        itemProperties=new ItemProperties();
        itemProperties.setMaterial("FISHING_ROD");
        loadConfigs();
        itemFactory= new ItemFactory(this,file);
    }


    public ItemStack give(Player player, int randomIndex) {

        ItemStack rod = itemFactory.createItem(player, randomIndex);

        ItemMeta rodMeta;

        if ((rodMeta = rod.getItemMeta()) != null) {
            if (displayName != null) rodMeta.setDisplayName(TextUtils.translateHexColorCodes(displayName));
            else rodMeta.setDisplayName(TextUtils.translateHexColorCodes(name));

            List<String> newLore=getLore();

            newLore.add(BetterFishing.messageConfig.getRodBaitSlot("无"));
            rodMeta.setLore(newLore);

            rodMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            rodMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            rodMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            rod.setItemMeta(rodMeta);
            rod = BFWorthNBT.setRodNBT(rod,this);
        }
        return rod;
    }



    void loadConfigs(){
        ConfigurationSection root = Objects.requireNonNull(rodConfig.getConfigurationSection(RodKey.rod_root)).getConfigurationSection(name);
        if(root==null){
            return;
        }
        displayName=root.getString(RodKey.displayName,name);
        lore=root.getStringList(RodKey.lore);
        extraFish=root.getString(RodKey.extra_fish);
        extraList.add(extraFish);
        List<String> etra = root.getStringList(RodKey.extra_list);
        extraList.addAll(etra);
        if(extraFish!=null){
            if(BetterFishing.allFishes.containsKey(extraFish)){
                throw new RuntimeException("名为 "+extraFish+" 的额外掉落物配置文件不存在");
            }
        }
        doubleDrop=root.getDouble(RodKey.double_drop,0);
        fishingSpeed=root.getDouble(RodKey.fishing_speed,0);
        if(root.contains("rarities")){
            Map<String, Object> values = root.getConfigurationSection(RodKey.rarities).getValues(false);
            Map<String,Integer> result=new HashMap<>();
            for(String key:values.keySet()){
                if(!BetterFishing.rarityMap.containsKey(key)){
                    throw new RuntimeException("鱼竿："+name+"中的稀有度："+key+"不存在");
                }
                result.put(key,Integer.parseInt(String.valueOf(values.get(key))));
            }
            rarities=result;
        }
        mutualityExp=root.getDouble(RodKey.mutuality_exp,1.0);
        unbreakable=root.getBoolean(RodKey.unbreakable,false);
        glowing=root.getBoolean(RodKey.glowing,false);
        if (root.contains(RodKey.model)) {
            model=root.getInt(RodKey.model);
        }
    }


}
