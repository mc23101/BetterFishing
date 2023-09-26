package top.zhangsiyao.betterfishing.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import top.zhangsiyao.betterfishing.item.FishItem;
import top.zhangsiyao.betterfishing.utils.FishUtils;

public class FishBlockPlaceEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void process(BlockPlaceEvent event){
        ItemStack itemInHand = event.getItemInHand();
        if(FishUtils.isFish(itemInHand)){
            FishItem fishItem=FishUtils.getFish(itemInHand);
            if (fishItem != null && fishItem.getInteractRewards() != null && fishItem.getInteractRewards().size() > 0) {
                event.setCancelled(true);
            }
        }
    }

}
