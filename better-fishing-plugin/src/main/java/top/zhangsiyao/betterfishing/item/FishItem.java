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
import top.zhangsiyao.betterfishing.exceptions.InvalidFishException;
import top.zhangsiyao.betterfishing.reward.Reward;
import top.zhangsiyao.betterfishing.utils.BFWorthNBT;
import top.zhangsiyao.betterfishing.utils.ColorUtils;
import top.zhangsiyao.betterfishing.utils.ItemFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Getter
public class FishItem  implements AbstractItem{

    File file;

    ItemFactory itemFactory;

    String displayName;

    BRarity rarity;

    List<String> lore;

    Integer durability;


    String effect;

    ItemProperties itemProperties;

    Boolean glowing;

    Double minPoint;

    Double weight;

    Double maxPoint;

    Float length;

    String fishName;

    List<Reward> eatRewards=new ArrayList<>();

    List<Reward> interactRewards=new ArrayList<>();


    UUID fisherman;

    FileConfiguration fishConfig;

    public FishItem(String fishName, FileConfiguration fishConfig, File file) {
        this.fishName = fishName;
        this.fishConfig = fishConfig;
        this.file=file;
        loadDisplayName();
        loadLore();
        loadDurability();
        loadEatEvent();
        loadEffect();
        loadInteractEvent();
        loadItemProperties();
        loadGlowing();
        loadMaxSize();
        loadMinSize();
        loadRarity();
        loadWeight();
        itemFactory=new ItemFactory(this,file);
    }

    public String getDisplayName() {
        return ColorUtils.translateHexColorCodes(displayName);
    }

    public List<String> getLore() {
        List<String> cur=new ArrayList<>();
        for (String l:lore){
            cur.add(ColorUtils.translateHexColorCodes(l));
        }
        return cur;
    }

    private ConfigurationSection getSection(){
        return fishConfig.getConfigurationSection("fish."+fishName);
    }

    public ItemStack give(Player player, int randomIndex) {
        ItemStack fish = itemFactory.createItem(player, randomIndex);

        ItemMeta fishMeta;

        if ((fishMeta = fish.getItemMeta()) != null) {
            if (displayName != null) fishMeta.setDisplayName(ColorUtils.translateHexColorCodes(displayName));
            else fishMeta.setDisplayName(ColorUtils.translateHexColorCodes(rarity.getColour() + getFishName()));

            fishMeta.setLore(getLore());

            fishMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            fishMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            fishMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            fish.setItemMeta(fishMeta);
            fish = BFWorthNBT.setFishNBT(fish, this);
        }
        return fish;
    }

    public Float getLength(){
        return 20f;
    }


    public void setFisherman(UUID fisherman) {
        this.fisherman = fisherman;
    }

    public void loadWeight(){
        weight=getSection().getDouble("weight",10.0);
    }

    private void loadDisplayName(){
        displayName=getSection().getString("displayName",fishName);
    }

    private void loadLore(){
        lore=getSection().getStringList("lore");
    }

    private void loadDurability(){
        durability=getSection().getInt("durability",0);
    }

    private void loadEatEvent(){
        List<String> rewards = getSection().getStringList("eat-event");
        for(String reward:rewards){
            eatRewards.add(new Reward(reward));
        }
    }

    private void loadEffect(){
        effect=getSection().getString("effect");
    }

    private void loadInteractEvent(){
        List<String> rewards = getSection().getStringList("interact-event");
        for(String reward:rewards){
            interactRewards.add(new Reward(reward));
        }
    }

    private void loadItemProperties(){
        itemProperties=new ItemProperties("fish",fishName,fishConfig);
    }

    private void loadGlowing(){
        glowing=getSection().getBoolean("glowing",false);
    }

    private void loadMaxSize(){
        maxPoint =getSection().getDouble("point.max");
    }

    private void loadMinSize(){
        minPoint =getSection().getDouble("pint.min");
    }

    private void loadRarity(){
        String r=getSection().getString("rarity");
        if(r!=null){
            if(BetterFishing.rarityMap.containsKey(r)){
                rarity= BetterFishing.rarityMap.get(r);
            }else {
                try {
                    throw new InvalidFishException(fishConfig.getName()+"文件中的"+fishName+"中的稀有度："+r+"不存在");
                } catch (InvalidFishException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public String toString() {
        return "FishItem{" +
                "displayName='" + displayName + '\'' +
                ", rarity=" + rarity +
                ", lore=" + lore +
                ", durability=" + durability +
                ", effect='" + effect + '\'' +
                ", itemProperties=" + itemProperties +
                ", glowing=" + glowing +
                ", minPoint=" + minPoint +
                ", maxPoint=" + maxPoint +
                ", fishName='" + fishName + '\'' +
                ", fishConfig=" + fishConfig.getName() +
                '}';
    }

    @Override
    public int hashCode(){
        return fishName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        FishItem fishItem=(FishItem) obj;
        return this.fishName.equals(fishItem.getFishName());
    }

}
