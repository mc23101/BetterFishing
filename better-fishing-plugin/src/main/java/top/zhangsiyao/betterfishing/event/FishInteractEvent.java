package top.zhangsiyao.betterfishing.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import top.zhangsiyao.betterfishing.item.FishItem;
import top.zhangsiyao.betterfishing.reward.Reward;
import top.zhangsiyao.betterfishing.utils.FishUtils;

public class FishInteractEvent implements Listener {

    @EventHandler
    public void interactEvent(PlayerInteractEvent event) {
        // 如果不符合操作，则返回事件
        if (event.getItem() == null || event.getPlayer().isSneaking() || !FishUtils.isFish(event.getItem()) || event.getHand() == EquipmentSlot.OFF_HAND
                || !event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        FishItem fish = FishUtils.getFish(event.getItem());
        if (fish != null) {
            if (fish.getInteractRewards().size()>0) {
                event.setCancelled(true);
                ItemStack itemInHand = event.getItem();
                event.getPlayer().getInventory().getItemInMainHand().setAmount(itemInHand.getAmount() - 1);
                for (Reward r : fish.getInteractRewards()) {
                    r.run(event.getPlayer(), null);
                }
            }
        }
    }
}
