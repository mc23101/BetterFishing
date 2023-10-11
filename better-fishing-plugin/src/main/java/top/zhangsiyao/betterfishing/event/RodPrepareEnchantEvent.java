package top.zhangsiyao.betterfishing.event;

import org.bukkit.Material;
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
                event.getInventory().setRepairCost(9999);
                event.getInventory().setRepairCostAmount(9999);
                event.setResult(new ItemStack(Material.AIR));
            }
        }
    }


}
