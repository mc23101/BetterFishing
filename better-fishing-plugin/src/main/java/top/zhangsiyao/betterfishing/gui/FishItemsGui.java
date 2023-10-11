package top.zhangsiyao.betterfishing.gui;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
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
import top.zhangsiyao.betterfishing.reward.Reward;
import top.zhangsiyao.betterfishing.reward.RewardType;
import top.zhangsiyao.betterfishing.utils.GuiUtils;
import top.zhangsiyao.betterfishing.utils.MaterialUtils;
import top.zhangsiyao.betterfishing.utils.NbtUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FishItemsGui implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void process(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player)){
            return;
        }
        Player player=(Player) event.getWhoClicked();
        Set<String> fishDisplayName=new HashSet<>();
        BetterFishing.allFishes.values().forEach(fishItem -> fishDisplayName.add(fishItem.getDisplayName()));
        if(fishDisplayName.contains(event.getView().getTitle())){
            event.setCancelled(true);
            player.updateInventory();
            ItemStack item = event.getInventory().getItem(53);
            NBTItem nbtItem=new NBTItem(item);
            int page=Integer.parseInt(NbtUtils.getString(nbtItem, NbtConstant.PAGE));
            String fishName=NbtUtils.getString(nbtItem,NbtConstant.BF_FISH_NAME);
            System.out.println(fishName);
            if(BetterFishing.allFishes.containsKey(fishName)){
                FishItem fishItem=BetterFishing.allFishes.get(fishName);
                List<Reward> interactRewards = fishItem.getInteractRewards();
                List<ItemStack> itemStacks=new ArrayList<>();
                for(Reward reward:interactRewards){
                    if(reward.getType().equals(RewardType.ITEM)){
                        String[] parsedItem = reward.getAction().split(" ");
                        itemStacks.add(new ItemStack(MaterialUtils.getMaterial(parsedItem[0].toUpperCase()), parsedItem.length>1?Integer.parseInt(parsedItem[1]):1));
                    }
                }
                if(event.getSlot()==45){
                    if((page-2)*36>=0){
                        player.closeInventory();
                        player.openInventory(getItemsGui(fishItem,page-1));
                    }
                }else if(event.getSlot()==53){
                    if(page*36<itemStacks.size()){
                        player.closeInventory();
                        player.openInventory(getItemsGui(fishItem,page+1));
                    }
                }
            }
        }
    }

    public static Inventory getItemsGui(FishItem fishItem, Integer page){
        List<Reward> interactRewards = fishItem.getInteractRewards();
        List<ItemStack> itemStacks=new ArrayList<>();
        for(Reward reward:interactRewards){
            if(reward.getType().equals(RewardType.ITEM)){
                String[] parsedItem = reward.getAction().split(" ");
                itemStacks.add(new ItemStack(MaterialUtils.getMaterial(parsedItem[0].toUpperCase()), parsedItem.length>1?Integer.parseInt(parsedItem[1]):1));
            }
        }
        Inventory inventory = GuiUtils.createBasePageInventory(fishItem.getDisplayName(),page,itemStacks.size());
        for(int i=(page-1)*36,j=0;i<page*36&&i<itemStacks.size();i++,j++){
            inventory.setItem(j,itemStacks.get(i));
        }
        ItemStack item = inventory.getItem(53);
        NBTItem nbtItem= new NBTItem(item);
        NbtUtils.setString(nbtItem,NbtConstant.BF_FISH_NAME,fishItem.getFishName());
        inventory.setItem(53,nbtItem.getItem());
        return inventory;
    }
}
