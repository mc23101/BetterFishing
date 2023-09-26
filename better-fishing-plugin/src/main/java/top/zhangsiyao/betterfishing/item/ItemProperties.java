package top.zhangsiyao.betterfishing.item;

import lombok.Data;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import top.zhangsiyao.betterfishing.constant.ItemPropertiesKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class ItemProperties implements Serializable {

    /**
     * 材质
     * */
    String material="";

    /**
     * 玩家头颅
     * */
    String head64="";

    /**
     * 玩家头颅(UUID)
     * */
    String headUUID="";

    /**
     * 自己的头
     * */
    Boolean ownHead=false;

    /**
     * 原始物品
     * */
    String rawMaterial="";

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

    String prefix;

    public ItemProperties(){
        materials=new ArrayList<>();
    }

    public ItemProperties(String prefix,String name, FileConfiguration fishConfig) {
        this.fishName = name;
        this.fishConfig = fishConfig;
        this.prefix=prefix;
        loadMaterial();
        loadHead64();
        loadHeadUUID();
        loadOwnHead();
        loadRowMaterial();
        loadMaterials();
        loadPotion();
    }

    private ConfigurationSection getSection(){
         return Objects.requireNonNull(fishConfig.getConfigurationSection(prefix)).getConfigurationSection(fishName).getConfigurationSection(ItemPropertiesKey.root);
    }

    private void loadMaterial(){
        material=getSection().getString(ItemPropertiesKey.material);
    }

    private void loadHead64(){
        head64=getSection().getString(ItemPropertiesKey.head_64);
    }

    private void loadHeadUUID(){
        headUUID=getSection().getString(ItemPropertiesKey.head_uuid);
    }

    private void loadOwnHead(){
        ownHead=getSection().getBoolean(ItemPropertiesKey.own_head);
    }

    private void loadRowMaterial(){
        rawMaterial=getSection().getString(ItemPropertiesKey.raw_material);
    }

    private void loadMaterials(){
        materials=getSection().getStringList(ItemPropertiesKey.materials);
    }

    private void loadPotion(){
        potion=getSection().getString(ItemPropertiesKey.potion);
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
