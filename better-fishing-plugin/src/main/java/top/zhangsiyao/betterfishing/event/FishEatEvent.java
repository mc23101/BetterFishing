package top.zhangsiyao.betterfishing.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import top.zhangsiyao.betterfishing.item.FishItem;
import top.zhangsiyao.betterfishing.reward.Reward;
import top.zhangsiyao.betterfishing.utils.FishUtils;

public class FishEatEvent implements Listener {

    @EventHandler
    public void onEatItem(final PlayerItemConsumeEvent event) {
        // Checks if the eaten item is a fish
        if (FishUtils.isFish(event.getItem())) {
            // Creates a replica of the fish we can use
            FishItem fish = FishUtils.getFish(event.getItem());
            if (fish != null) {
                if (fish.getEatRewards().size()>0) {
                    // Runs through each eat-event
                    for (Reward r : fish.getEatRewards()) {
                        r.run(event.getPlayer(), null);
                    }
                }
            }
        }
    }
}
