package top.zhangsiyao.betterfishing.gui;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.constant.MessageKey;
import top.zhangsiyao.betterfishing.constant.NbtConstant;
import top.zhangsiyao.betterfishing.item.FishItem;
import top.zhangsiyao.betterfishing.item.Rod;
import top.zhangsiyao.betterfishing.utils.GuiUtils;
import top.zhangsiyao.betterfishing.utils.NbtUtils;

import java.util.ArrayList;
import java.util.List;

public class RodGui implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void process(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player)){
            return;
        }
        Player player=(Player) event.getWhoClicked();
        if(event.getView().getTitle().equals(BetterFishing.messageConfig.get(MessageKey.rod_gui_title))){
            event.setCancelled(true);
            player.updateInventory();
            ItemStack item = event.getInventory().getItem(53);
            NBTItem nbtItem=new NBTItem(item);
            Integer page=Integer.parseInt(NbtUtils.getString(nbtItem, NbtConstant.PAGE));
            if(event.getSlot()==45){
                if((page-2)*36>=0){
                    player.closeInventory();
                    player.openInventory(getRodGui(player,page-1));
                }
            }else if(event.getSlot()==53){
                if(page*36<BetterFishing.allFishes.size()){
                    player.closeInventory();
                    player.openInventory(getRodGui(player,page+1));
                }
            }
        }
    }

    public static Inventory getRodGui(Player player, Integer page){
        List<Rod> rodItems=new ArrayList<>(BetterFishing.rodMap.values());
        Inventory inventory = GuiUtils.createBasePageInventory(BetterFishing.messageConfig.get(MessageKey.rod_gui_title),page,rodItems.size());
        for(int i=(page-1)*36,j=0;i<page*36&&i<rodItems.size();i++,j++){
            inventory.setItem(j,rodItems.get(i).give(player,-1));
        }
        return inventory;
    }
}
