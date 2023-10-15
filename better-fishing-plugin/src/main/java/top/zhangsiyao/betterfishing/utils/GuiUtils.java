package top.zhangsiyao.betterfishing.utils;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.zhangsiyao.betterfishing.constant.NbtConstant;

public class GuiUtils {

    public static Inventory createBasePageInventory(String title,Integer page,Integer total){
        Inventory inventory= Bukkit.createInventory(null,54,title);
        ItemStack black = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = black.getItemMeta();
        meta.setDisplayName("-");
        black.setItemMeta(meta);
        ItemStack yellow = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        yellow.setItemMeta(meta);
        for(int i=36;i<=44;i++){
            inventory.setItem(i,black);
        }
        for(int i=46;i<=52;i++){
            inventory.setItem(i,yellow);
        }
        ItemStack left;

        if((page-2)*36>=0){
            left=new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta itemMeta = left.getItemMeta();
            itemMeta.setDisplayName("§e§l上一页");
            left.setItemMeta(itemMeta);
        }else {
            left=new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta itemMeta = left.getItemMeta();
            itemMeta.setDisplayName("§c§l上一页");
            left.setItemMeta(itemMeta);
        }
        ItemStack right;
        if((page)*36<total){
            right=new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta itemMeta=right.getItemMeta();
            itemMeta.setDisplayName("§e§l下一页");
            right.setItemMeta(itemMeta);
        }else {
            right=new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta itemMeta=right.getItemMeta();
            itemMeta.setDisplayName("§c§l下一页");
            right.setItemMeta(itemMeta);
        }

        NBTItem nbtItem=new NBTItem(right);
        NbtUtils.setString(nbtItem, NbtConstant.PAGE,String.valueOf(page));
        inventory.setItem(45,left);
        inventory.setItem(53,nbtItem.getItem());
        return inventory;
    }
}
