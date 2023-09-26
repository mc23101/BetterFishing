package top.zhangsiyao.betterfishing.item;

import lombok.Data;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.constant.NbtConstant;
import top.zhangsiyao.betterfishing.utils.BFWorthNBT;
import top.zhangsiyao.betterfishing.utils.TextUtils;
import top.zhangsiyao.betterfishing.utils.ItemFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class Rod implements AbstractItem {

    String rodName;

    String displayName;

    String fishingSpeed;

    String doubleDrop;

    String extraFish;

    ItemFactory itemFactory;

    Boolean glowing;

    Integer durability=0;

    ItemProperties itemProperties;

    FileConfiguration rodConfig;

    List<String> lore=new ArrayList<>();

    File file;

    Map<String, String> nbt=new HashMap<>();

    Map<String,Integer> rarities=new HashMap<>();

    public Rod(String name, FileConfiguration rodConfig, File file) {
        this.rodName=name;
        this.rodConfig=rodConfig;
        this.file=file;
        itemProperties=new ItemProperties();
        itemProperties.setMaterial("FISHING_ROD");
        loadDisplayName();
        loadExtraFish();
        loadLore();
        loadDoubleDrop();
        loadFishingSpeed();
        loadRarities();
        loadGlowing();
        itemFactory= new ItemFactory(this,file);
    }

    public String getDisplayName() {
        return TextUtils.translateHexColorCodes(displayName);
    }

    public List<String> getLore() {
        List<String> cur=new ArrayList<>();
        for (String l:lore){
            cur.add(TextUtils.translateHexColorCodes(l));
        }
        return cur;
    }

    public ItemStack give(Player player, int randomIndex) {

        ItemStack rod = itemFactory.createItem(player, randomIndex);

        ItemMeta rodMeta;

        if ((rodMeta = rod.getItemMeta()) != null) {
            if (displayName != null) rodMeta.setDisplayName(TextUtils.translateHexColorCodes(displayName));
            else rodMeta.setDisplayName(TextUtils.translateHexColorCodes(getRodName()));

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


    private ConfigurationSection getSection(){
        return rodConfig.getConfigurationSection("rods."+rodName);
    }


    /**
     * 获取鱼竿displayName
     * */
    private void loadDisplayName(){
        displayName=getSection().getString("displayName",rodName);
    }


    /**
     * 获取鱼竿的lore标签
     * */
    private void loadLore(){
        lore=getSection().getStringList("lore");
    }


    /**
     * 获取鱼竿额外凋落物的配置文件名称
     * */
    private void  loadExtraFish(){
        extraFish=getSection().getString(NbtConstant.EXTRA_FISH);
        if(extraFish==null){
            return;
        }
        if(BetterFishing.allFishes.containsKey(extraFish)){
            throw new RuntimeException("名为 "+extraFish+" 的额外掉落物配置文件不存在");
        }
    }

    private void loadDoubleDrop(){
        doubleDrop=getSection().getString(NbtConstant.DOUBLE_DROP);
        if(doubleDrop==null){
            return;
        }
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        Matcher isNum = pattern.matcher(doubleDrop);
        if(!isNum.matches()){
            throw new RuntimeException();
        }else {
            float f=Float.parseFloat(doubleDrop);
            if(f<0||f>1){
                throw new RuntimeException();
            }
        }
    }

    private void loadFishingSpeed(){
        fishingSpeed=getSection().getString(NbtConstant.FISHING_SPEED);
        if(fishingSpeed==null){
            return ;
        }
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        Matcher isNum = pattern.matcher(fishingSpeed);
        if(!isNum.matches()){
            throw new RuntimeException();
        }else {
            float f=Float.parseFloat(fishingSpeed);
            if(f<0||f>1){
                throw new RuntimeException();
            }
        }
    }

    private void loadRarities(){
        if(!getSection().contains("rarities")){
            return;
        }
        Map<String, Object> values = getSection().getConfigurationSection("rarities").getValues(false);
        Map<String,Integer> result=new HashMap<>();
        for(String key:values.keySet()){
            if(!BetterFishing.rarityMap.containsKey(key)){
                throw new RuntimeException("鱼竿："+rodName+"中的稀有度："+key+"不存在");
            }
            result.put(key,Integer.parseInt(String.valueOf(values.get(key))));
        }
        rarities=result;
    }


    private void loadGlowing(){
        glowing=getSection().getBoolean("glowing",false);
    }

    @Override
    public String toString() {
        return "Rod{" +
                "rodName='" + rodName + '\'' +
                ", fishingSpeed='" + fishingSpeed + '\'' +
                ", doubleDrop='" + doubleDrop + '\'' +
                ", extraFish='" + extraFish + '\'' +
                ", lore=" + lore +
                ", nbt=" + nbt +
                ", Rarities=" + rarities +
                '}';
    }


}
