package top.zhangsiyao.betterfishing.fishing;


import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.constant.MessageKey;
import top.zhangsiyao.betterfishing.event.FishTitleEvent;
import top.zhangsiyao.betterfishing.item.BaitItem;
import top.zhangsiyao.betterfishing.item.Rod;
import top.zhangsiyao.betterfishing.utils.FishUtils;
import top.zhangsiyao.betterfishing.utils.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FishingTitleEvent implements Listener {

    public static Map<UUID,Boolean> sentActionBar=new HashMap<>();
    public static Thread sectionBar;

    public FishingTitleEvent() {
        sectionBar= new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for(UUID uuid:sentActionBar.keySet()){
                    Player player= Bukkit.getPlayer(uuid);
                    if(sentActionBar.get(uuid).equals(true)&&player != null && player.isOnline()){
                        TextComponent textComponent=new TextComponent();
                        ItemStack itemStack=player.getInventory().getItemInMainHand();
                        Rod rod= FishUtils.getRod(itemStack);
                        BaitItem baitItem=FishUtils.getBaitByRod(itemStack);
                        baitItem=baitItem==null?BaitItem.empty:baitItem;
                        textComponent.setText(TextUtils.parseTest(BetterFishing.messageConfig.get(MessageKey.fishing_actionbar_message),rod,baitItem));
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,textComponent);
                    }else {
                        sentActionBar.remove(uuid);
                    }
                }
            }
        });
        sectionBar.setDaemon(true);
        sectionBar.start();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public static void process(FishTitleEvent event) {
        Player player=event.getPlayer();
        player.resetTitle();
        //当鱼儿咬住鱼竿时
        if(event.getState()==PlayerFishEvent.State.BITE){
            String title=BetterFishing.messageConfig.get(MessageKey.fish_catch_title);
            String subtitle=BetterFishing.messageConfig.get(MessageKey.fish_catch_subtitle);
            player.sendTitle(title,subtitle,20,60,20);
            sentActionBar.put(player.getUniqueId(),false);
            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,TextComponent.fromLegacyText(""));
        } else if(event.getState()==PlayerFishEvent.State.FAILED_ATTEMPT){
            String title=BetterFishing.messageConfig.get(MessageKey.fish_failed_attempt_title);
            String subtitle=BetterFishing.messageConfig.get(MessageKey.fish_failed_attempt_subtitle);
            event.getPlayer().sendTitle(title,subtitle,5,20,5);
            sentActionBar.put(player.getUniqueId(),true);
        } else if(event.getState()==PlayerFishEvent.State.CAUGHT_FISH){
            String title=TextUtils.parseTest(BetterFishing.messageConfig.get(MessageKey.fishing_title_success_title),FishUtils.getFish(event.getFish()));
            String subtitle=TextUtils.parseTest(BetterFishing.messageConfig.get(MessageKey.fishing_title_success_subtitle));
            event.getPlayer().sendTitle(title,subtitle,10,10,10);
            sentActionBar.put(player.getUniqueId(),false);
            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,TextComponent.fromLegacyText(""));
        }else if(event.getState()==PlayerFishEvent.State.IN_GROUND){
            String title=TextUtils.parseTest(BetterFishing.messageConfig.get(MessageKey.fishing_title_reel_in_fast_title));
            String subtitle=TextUtils.parseTest(BetterFishing.messageConfig.get(MessageKey.fishing_title_reel_in_fast_subtitle));
            event.getPlayer().sendTitle(title,subtitle,10,10,10);
            sentActionBar.put(player.getUniqueId(),false);
            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,TextComponent.fromLegacyText(""));
        }else {
            sentActionBar.put(player.getUniqueId(),true);
        }
    }

}
