package top.zhangsiyao.betterfishing.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface IItem {
    ItemProperties getItemProperties();

    List<String> getLore();

    Integer getDurability();

    String getDisplayName();

    Boolean getGlowing();

    ItemStack give(Player player, int randomIndex);

    Boolean getUnbreakable();

    Integer getModel();



}
