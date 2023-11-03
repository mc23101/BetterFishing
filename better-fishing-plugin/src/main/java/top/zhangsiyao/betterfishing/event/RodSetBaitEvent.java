package top.zhangsiyao.betterfishing.event;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import top.zhangsiyao.betterfishing.constant.NbtConstant;
import top.zhangsiyao.betterfishing.item.BaitItem;
import top.zhangsiyao.betterfishing.item.Rod;
import top.zhangsiyao.betterfishing.utils.FishUtils;

public class RodSetBaitEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public static void process(InventoryClickEvent event){
        if(event.getAction()== InventoryAction.SWAP_WITH_CURSOR){
            ItemStack cursor=event.getCursor();
            ItemStack curr=event.getCurrentItem();
            Rod rod= FishUtils.getRod(curr);
            BaitItem bait=FishUtils.getBait(cursor);
            if(rod!=null&&bait!=null){
                NBTItem nbtItem=new NBTItem(curr);
                NBTCompound bfCompound = nbtItem.getOrCreateCompound(NbtConstant.BF_COMPOUND);
                bfCompound.setString(NbtConstant.USE_BAIT_NAME,bait.getName());
                FishUtils.refreshRodLore(nbtItem.getItem());
                event.setCurrentItem(nbtItem.getItem());
                event.setCancelled(true);
            }
        }
    }
}
