package top.zhangsiyao.betterfishing.item;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Rod implements Serializable {

    String rodName;

    String display;

    String fishingSpeed;

    String doubleDrop;

    String extraFish;

    List<String> lore=new ArrayList<>();



    Map<String, String> nbt=new HashMap<>();

    Map<String,Integer> Rarities=new HashMap<>();

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
