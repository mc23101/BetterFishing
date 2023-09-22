package top.zhangsiyao.betterfishing.item;

import lombok.Data;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

@Data
@Getter
public class BRarity{

    String name;

    FileConfiguration rarityConfig;

    Double weight;

    String colour;

    Double worthMultiplier;

    Boolean broadcast;

    Double minSize;

    Double maxSize;

    public BRarity(String name, FileConfiguration rarityConfig) {
        this.name = name;
        this.rarityConfig=rarityConfig;
        loadWeight();
        loadColour();
        loadWorthMultiplier();
        loadBroadcast();
        loadMinPoint();
        loadMaxPoint();
    }

    private void loadWeight(){
        weight=rarityConfig.getConfigurationSection("rarities."+name).getDouble("weight");
    }

    private void loadColour(){
        colour=rarityConfig.getConfigurationSection("rarities."+name).getString("colour");
    }

    private void loadWorthMultiplier(){
        worthMultiplier=rarityConfig.getConfigurationSection("rarities."+name).getDouble("worth-multiplier");
    }

    private void loadBroadcast(){
        broadcast=rarityConfig.getConfigurationSection("rarities."+name).getBoolean("broadcast");
    }

    private void loadMaxPoint(){
        maxSize=rarityConfig.getConfigurationSection("rarities."+name).getDouble("point.max");
    }

    private void loadMinPoint(){
        minSize=rarityConfig.getConfigurationSection("rarities."+name).getDouble("point.min");
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        BRarity rarity=(BRarity) obj;
        return this.name.equals(rarity.getName());
    }

}
