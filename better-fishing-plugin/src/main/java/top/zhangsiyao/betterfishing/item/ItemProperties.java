package top.zhangsiyao.betterfishing.item;

import lombok.Data;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
public class ItemProperties implements Serializable {

    /**
     * 材质
     * */
    String material;

    /**
     * 玩家头颅
     * */
    String head64;

    /**
     * 玩家头颅(UUID)
     * */
    String headUUID;

    /**
     * 自己的头
     * */
    Boolean ownHead;

    /**
     * 原始物品
     * */
    String rawMaterial;

    /**
     * 随机材质
     * */
    List<String> materials;

    /**
     * 表示为药水
     * */
    String potion;

    String fishName;
    FileConfiguration fishConfig;

    public ItemProperties(String fishName, FileConfiguration fishConfig) {
        this.fishName = fishName;
        this.fishConfig = fishConfig;
        loadMaterial();
        loadHead64();
        loadHeadUUID();
        loadOwnHead();
        loadRowMaterial();
        loadMaterials();
        loadPotion();
    }

    private ConfigurationSection getSection(){
         return Objects.requireNonNull(fishConfig.getConfigurationSection("fish")).getConfigurationSection(fishName+".item");
    }

    private void loadMaterial(){
        material=getSection().getString("material");
    }

    private void loadHead64(){
        head64=getSection().getString("head-64");
    }

    private void loadHeadUUID(){
        headUUID=getSection().getString("head-uuid");
    }

    private void loadOwnHead(){
        ownHead=getSection().getBoolean("own-head");
    }

    private void loadRowMaterial(){
        rawMaterial=getSection().getString("raw-material");
    }

    private void loadMaterials(){
        materials=getSection().getStringList("materials");
    }

    private void loadPotion(){
        potion=getSection().getString("potion");
    }

    @Override
    public String toString() {
        return "ItemProperties{" +
                "material='" + material + '\'' +
                ", head64='" + head64 + '\'' +
                ", headUUID='" + headUUID + '\'' +
                ", ownHead=" + ownHead +
                ", rawMaterial='" + rawMaterial + '\'' +
                ", materials=" + materials +
                ", potion='" + potion + '\'' +
                ", fishName='" + fishName + '\'' +
                ", fishConfig=" + fishConfig +
                '}';
    }
}
