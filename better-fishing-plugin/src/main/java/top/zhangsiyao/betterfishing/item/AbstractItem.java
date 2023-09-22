package top.zhangsiyao.betterfishing.item;

import java.util.List;

public interface AbstractItem {
    ItemProperties getItemProperties();

    List<String> getLore();

    Integer getDurability();

    String getDisplayName();

    Boolean getGlowing();




}
