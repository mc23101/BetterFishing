package top.zhangsiyao.betterfishing.utils;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import top.zhangsiyao.betterfishing.constant.NbtConstant;

public class GuiUtils {

    public static Inventory createBasePageInventory(String title,Integer page,Integer total){
        Inventory inventory= Bukkit.createInventory(null,54,title);
        for(int i=36;i<=44;i++){
            inventory.setItem(i,new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        for(int i=46;i<=52;i++){
            inventory.setItem(i,new ItemStack(Material.YELLOW_STAINED_GLASS_PANE));
        }
        ItemStack left=(page-2)*36>=0?new ItemStack(Material.GREEN_STAINED_GLASS_PANE):new ItemStack(Material.RED_STAINED_GLASS_PANE) ;
        ItemStack right=(page)*36<total?new ItemStack(Material.GREEN_STAINED_GLASS_PANE):new ItemStack(Material.RED_STAINED_GLASS_PANE) ;
        NBTItem nbtItem=new NBTItem(right);
        NbtUtils.setString(nbtItem, NbtConstant.PAGE,String.valueOf(page));
        inventory.setItem(45,left);
        inventory.setItem(53,nbtItem.getItem());
        return inventory;
    }
}
