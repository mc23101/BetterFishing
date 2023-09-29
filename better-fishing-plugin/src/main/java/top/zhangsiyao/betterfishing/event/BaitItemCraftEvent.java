package top.zhangsiyao.betterfishing.event;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.constant.MessageKey;
import top.zhangsiyao.betterfishing.utils.FishUtils;

public class BaitItemCraftEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void process(CraftItemEvent event){
        for (ItemStack itemStack : event.getInventory()) {
            if (itemStack!=null&&FishUtils.isBait(itemStack)) {
                event.setCancelled(true);
                for (HumanEntity humanEntity : event.getInventory().getViewers()) {
                    if(humanEntity!=null){
                        humanEntity.sendMessage(BetterFishing.messageConfig.get(MessageKey.bait_cant_craft));
                    }
                }
            }

        }
    }

}
