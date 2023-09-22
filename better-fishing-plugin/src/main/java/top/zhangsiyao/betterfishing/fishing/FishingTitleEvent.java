package top.zhangsiyao.betterfishing.fishing;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import top.zhangsiyao.betterfishing.event.FishTitleEvent;

public class FishingTitleEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public static void process(FishTitleEvent event) {
        event.getPlayer().resetTitle();
        //当鱼儿咬住鱼竿时
        if(event.getState()==PlayerFishEvent.State.BITE){
            event.getPlayer().sendTitle("鱼儿上钩啦","快收起鱼竿吧",20,60,20);
        }
        if(event.getState()==PlayerFishEvent.State.FAILED_ATTEMPT){
            event.getPlayer().sendTitle("很遗憾，你的鱼儿跑掉啦","再来尝试一次吧",5,20,5);
        }

        if(event.getState()==PlayerFishEvent.State.CAUGHT_FISH){
            event.getPlayer().sendTitle(event.getFish().getItemMeta().getDisplayName(),"恭喜你，获得",10,10,10);
        }

    }

}
