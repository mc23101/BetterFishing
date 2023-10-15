package top.zhangsiyao.betterfishing.item;

import lombok.Data;
import top.zhangsiyao.betterfishing.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class AbstractItem implements IItem{

    Integer model;

    ItemProperties itemProperties;

    String displayName;

    List<String> lore;

    Boolean glowing;

    Integer durability=0;

    Boolean unbreakable=false;

    String name;


    @Override
    public final ItemProperties getItemProperties() {
        return itemProperties;
    }

    @Override
    public final List<String> getLore() {
        List<String> cur=new ArrayList<>();
        for (String l:lore){
            cur.add(TextUtils.translateHexColorCodes(l));
        }
        return cur;
    }

    @Override
    public final Integer getDurability() {
        return durability;
    }

    @Override
    public final String getDisplayName() {
        return TextUtils.translateHexColorCodes(displayName==null?name:displayName);
    }

    @Override
    public final Boolean getGlowing() {
        return glowing;
    }


    @Override
    public final Boolean getUnbreakable() {
        return unbreakable;
    }

}
