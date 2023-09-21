package top.zhangsiyao.betterfishing.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Rod implements Serializable {

    String rodName;

    String display;

    String fishingSpeed;

    String doubleDrop;

    String extraFish;

    List<String> lore=new ArrayList<>();



    Map<String, String> nbt=new HashMap<>();

    Map<String,Integer> Rarities=new HashMap<>();


    public String getFishingSpeed() {
        return fishingSpeed;
    }

    public void setFishingSpeed(String fishingSpeed) {
        this.fishingSpeed = fishingSpeed;
    }

    public String getDoubleDrop() {
        return doubleDrop;
    }

    public void setDoubleDrop(String doubleDrop) {
        this.doubleDrop = doubleDrop;
    }

    public String getRodName() {
        return rodName;
    }

    public void setRodName(String rodName) {
        this.rodName = rodName;
    }

    public Map<String, Integer> getRarities() {
        return Rarities;
    }

    public String getExtraFish() {
        return extraFish;
    }

    public void setExtraFish(String extraFish) {
        this.extraFish = extraFish;
    }

    public void setRarities(Map<String, Integer> rarities) {
        Rarities = rarities;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public Map<String, String> getNbt() {
        return nbt;
    }

    public void setNbt(Map<String, String> nbt) {
        this.nbt = nbt;
    }

    @Override
    public String toString() {
        return "Rod{" +
                "rodName='" + rodName + '\'' +
                ", display='" + display + '\'' +
                ", fishingSpeed='" + fishingSpeed + '\'' +
                ", doubleDrop='" + doubleDrop + '\'' +
                ", extraFish='" + extraFish + '\'' +
                ", lore=" + lore +
                ", nbt=" + nbt +
                ", Rarities=" + Rarities +
                '}';
    }
}
