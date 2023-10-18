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
import top.zhangsiyao.betterfishing.constant.AttachmentKey;
import top.zhangsiyao.betterfishing.constant.BaitKey;
import top.zhangsiyao.betterfishing.constant.RodKey;
import top.zhangsiyao.betterfishing.utils.BFWorthNBT;
import top.zhangsiyao.betterfishing.utils.ItemFactory;
import top.zhangsiyao.betterfishing.utils.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class Attachment extends AbstractItem {


    private final File file;

    private final FileConfiguration configuration;

    String slot;

    Double fishingSpeed;

    Double doubleDrop;

    Double mutualityExp=1.0;

    ItemFactory itemFactory;

    Map<String,Integer> rarities=new HashMap<>();

    Map<String,Integer> fish=new HashMap<>();

    public Attachment(String name, File file, FileConfiguration configuration) {
        super(AttachmentKey.root,name,file,configuration);
        this.name = name;
        this.file = file;
        this.configuration=configuration;
        loadConfigs();
        itemFactory= new ItemFactory(this,file);
    }

    @Override
    public ItemStack give(Player player, int randomIndex) {
        ItemStack attachment = itemFactory.createItem(player, randomIndex);

        ItemMeta rodMeta;

        if ((rodMeta = attachment.getItemMeta()) != null) {
            if (displayName != null) rodMeta.setDisplayName(TextUtils.translateHexColorCodes(displayName));
            else rodMeta.setDisplayName(TextUtils.translateHexColorCodes(name));


            rodMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            rodMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            rodMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            attachment=BFWorthNBT.setAttachmentNBT(attachment,this);

        }
        return attachment;
    }

    @Override
    void loadConfigs() {
        ConfigurationSection root = Objects.requireNonNull(configuration.getConfigurationSection(AttachmentKey.root)).getConfigurationSection(name);
        if(root==null){
            return;
        }
        displayName=root.getString(AttachmentKey.displayName,name);
        lore=root.getStringList(AttachmentKey.lore);
        doubleDrop=root.getDouble(AttachmentKey.double_drop,0);
        fishingSpeed=root.getDouble(AttachmentKey.fishing_speed,0);
        mutualityExp=root.getDouble(AttachmentKey.mutuality_exp,1.0);
        unbreakable=root.getBoolean(AttachmentKey.unbreakable,false);
        glowing=root.getBoolean(AttachmentKey.glowing,false);
        slot=root.getString(AttachmentKey.slot,null);
        if(slot==null||!BetterFishing.attachmentSlots.containsKey(slot)){
            throw new RuntimeException("配件："+name+" 中配件插槽不存在");
        }
        if (root.contains(AttachmentKey.model)) {
            model=root.getInt(AttachmentKey.model);
        }
        if(root.contains("rarities")){
            Map<String, Object> values = root.getConfigurationSection(AttachmentKey.rarities).getValues(false);
            Map<String,Integer> result=new HashMap<>();
            for(String key:values.keySet()){
                if(!BetterFishing.rarityMap.containsKey(key)){
                    throw new RuntimeException("配件："+name+"中的稀有度："+key+"不存在");
                }
                result.put(key,Integer.parseInt(String.valueOf(values.get(key))));
            }
            rarities=result;
        }
        if(root.contains("fish")){
            Map<String, Object> values = root.getConfigurationSection(AttachmentKey.fish).getValues(false);
            Map<String,Integer> result=new HashMap<>();
            for(String key:values.keySet()){
                if(!BetterFishing.allFishes.containsKey(key)){
                    throw new RuntimeException("配件："+name+"中的稀有度："+key+"不存在");
                }
                result.put(key,Integer.parseInt(String.valueOf(values.get(key))));
            }
            fish=result;
        }

    }
}
