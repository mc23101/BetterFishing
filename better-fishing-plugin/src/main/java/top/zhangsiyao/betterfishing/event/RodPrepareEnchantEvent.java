package top.zhangsiyao.betterfishing.event;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import top.zhangsiyao.betterfishing.utils.FishUtils;

public class RodPrepareEnchantEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void process(PrepareItemEnchantEvent event){
        ItemStack item = event.getItem();
        if (FishUtils.isRod(item)){
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void processAnvil(PrepareAnvilEvent event){
        for(ItemStack item:event.getInventory()){
            if(FishUtils.isRod(item)){
                HumanEntity viewer = event.getView().getPlayer();
                if(viewer instanceof Player){
                    viewer.closeInventory();
                    viewer.sendMessage("不推荐你附魔这个鱼竿哦！强行附魔会使这个鱼竿失效");
                }
            }
        }
    }


}
