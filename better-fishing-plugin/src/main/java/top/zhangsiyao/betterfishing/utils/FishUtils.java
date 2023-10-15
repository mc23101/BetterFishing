package top.zhangsiyao.betterfishing.utils;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Bukkit.Region;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.constant.NbtConstant;
import top.zhangsiyao.betterfishing.item.BRarity;
import top.zhangsiyao.betterfishing.item.BaitItem;
import top.zhangsiyao.betterfishing.item.FishItem;
import top.zhangsiyao.betterfishing.item.Rod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class FishUtils {

    public static Map<BRarity,List<FishItem>> getCurFish(Rod rod){
        Map<BRarity,List<FishItem>> curFish=new HashMap<>();
        for(BRarity rarity:BetterFishing.globalRarityFishes.keySet()){
            curFish.put(rarity,new ArrayList<>(BetterFishing.globalRarityFishes.get(rarity)));
        }

        if(rod.getExtraList()!=null&&rod.getExtraList().size()>0&&BetterFishing.extraRarityFishes.containsKey(rod.getExtraFish())){
            for(String extra:rod.getExtraList()){
                Map<BRarity,List<FishItem>> map= BetterFishing.extraRarityFishes.get(extra);
                for(BRarity r:map.keySet()){
                    if(curFish.containsKey(r)){
                        curFish.get(r).addAll(map.get(r));
                    }else {
                        curFish.put(r,new ArrayList<>(map.get(r)));
                    }
                }
            }
        }
        return curFish;
    }

    public static Rod getRod(ItemStack stack){
        if(stack==null||stack.getType().equals(Material.AIR)){
            return null;
        }
        NBTItem nbtItem=new NBTItem(stack);
        if(!NbtUtils.hasKey(nbtItem,NbtConstant.ROD_NAME)){
            return null;
        }
        String rodName=NbtUtils.getString(nbtItem, NbtConstant.ROD_NAME);
        if(!BetterFishing.rodMap.containsKey(rodName)){
            return null;
        }
        return BetterFishing.rodMap.get(rodName);
    }

    public static boolean isRod(ItemStack stack){
        if(stack==null||stack.getType().equals(Material.AIR)){
            return false;
        }
        NBTItem nbtItem=new NBTItem(stack);
        return NbtUtils.hasKey(nbtItem,NbtConstant.ROD_NAME);
    }


    public static boolean useBait(ItemStack itemStack){
        if(itemStack==null||itemStack.getType().equals(Material.AIR)){
            return false;
        }
        NBTItem nbtItem=new NBTItem(itemStack);
        return NbtUtils.hasKey(nbtItem, NbtConstant.USE_BAIT_NAME);
    }

    public static boolean isBait(ItemStack itemStack){
        if(itemStack==null||itemStack.getType().equals(Material.AIR)){
            return false;
        }
        NBTItem nbtItem=new NBTItem(itemStack);
        return NbtUtils.hasKey(nbtItem, NbtConstant.BF_BAIT_NAME);
    }

    public static BaitItem getBait(ItemStack baitItemstack){
        if(baitItemstack==null||baitItemstack.getType().equals(Material.AIR)){
            return null;
        }
        NBTItem nbtItem=new NBTItem(baitItemstack);
        if(!isBait(baitItemstack)||!BetterFishing.baitMap.containsKey(NbtUtils.getString(nbtItem,NbtConstant.BF_BAIT_NAME))){
            return null;
        }else {
            return BetterFishing.baitMap.get(NbtUtils.getString(nbtItem,NbtConstant.BF_BAIT_NAME));
        }
    }

    public static BaitItem getBaitByRod(ItemStack rodItemStack){
        if(rodItemStack==null||rodItemStack.getType().equals(Material.AIR)){
            return null;
        }
        NBTItem nbtItem=new NBTItem(rodItemStack);
        if(!useBait(rodItemStack)||!BetterFishing.baitMap.containsKey(NbtUtils.getString(nbtItem,NbtConstant.USE_BAIT_NAME))){
            return null;
        }else {
            return BetterFishing.baitMap.get(NbtUtils.getString(nbtItem,NbtConstant.USE_BAIT_NAME));
        }
    }

    public static void giveItems(List<ItemStack> items, Player player) {
        if (items.isEmpty()) {
            return;
        }
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5f, 1.5f);
        player.getInventory().addItem(items.toArray(new ItemStack[0]))
                .values()
                .forEach(item -> new BukkitRunnable() {
                    public void run() {
                        if (!item.getType().equals(Material.AIR)) {
                            player.getWorld().dropItem(player.getLocation(), item);
                        }
                    }
                }.runTask(JavaPlugin.getProvidingPlugin(FishUtils.class)));
    }

    public static void refreshRodLore(ItemStack itemStack){
        if (itemStack == null || itemStack.getType() == Material.AIR || !itemStack.hasItemMeta()) {
            return;
        }
        Rod rod=getRod(itemStack);
        if(rod==null){
            return;
        }
        List<String> lore=new ArrayList<>(rod.getLore());
        BaitItem baitItem=getBaitByRod(itemStack);
        if(baitItem!=null){
            lore.add(BetterFishing.messageConfig.getRodBaitSlot(baitItem.getDisplayName()));
        }else {
            lore.add(BetterFishing.messageConfig.getRodBaitSlot("无"));
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setLore(lore);
        }
        itemStack.setItemMeta(itemMeta);
    }




    public static ItemStack getBait(Player player, String baitName){
        for (ItemStack itemStack : player.getInventory()) {
           if(itemStack!=null&&!itemStack.getType().equals(Material.AIR)){
              NBTItem nbtItem=new NBTItem(itemStack);
              if(NbtUtils.hasKey(nbtItem,NbtConstant.BF_BAIT_NAME)){
                  String name=NbtUtils.getString(nbtItem,NbtConstant.BF_BAIT_NAME);
                  if(name!=null&&name.equals(baitName)){
                      return itemStack;
                  }
              }
           }
        }
        return new ItemStack(Material.AIR);
    }


    /**
     * 检查物品是否为鱼
     * */
    public static boolean isFish(ItemStack item) {
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) {
            return false;
        }

        return NbtUtils.hasKey(new NBTItem(item), NbtConstant.BF_FISH_NAME);
    }

    /**
     * 获取FishItem的实例
     * */
    public static FishItem getFish(ItemStack itemStack){
        if(!isFish(itemStack)){
            return null;
        }
        String fishName = NbtUtils.getString(new NBTItem(itemStack), NbtConstant.BF_FISH_NAME);
        return BetterFishing.allFishes.get(fishName);
    }


    public static boolean checkRegion(Location l, List<String> whitelistedRegions) {
        // if there's any region plugin installed
        if (BetterFishing.guardPL == null) {
            return true;
        }
        // if the user has defined a region whitelist
        if (whitelistedRegions.isEmpty()) {
            return true;
        }

        if (BetterFishing.guardPL.equals("worldguard")) {

            // Creates a query for whether the player is stood in a protectedregion defined by the user
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(l));

            // runs the query
            for (ProtectedRegion pr : set) {
                if (whitelistedRegions.contains(pr.getId())) {
                    return true;
                }
            }
            return false;
        } else if (BetterFishing.guardPL.equals("redprotect")) {
            Region r = RedProtect.get().getAPI().getRegion(l);
            // if the hook is in any redprotect region
            if (r != null) {
                // if the hook is in a whitelisted region
                return whitelistedRegions.contains(r.getName());
            }
            return false;
        } else {
            // the user has defined a region whitelist but doesn't have a region plugin.
            BetterFishing.logger.log(Level.WARNING, "Please install WorldGuard or RedProtect to enable region-specific fishing.");
            return true;
        }
    }

    public static boolean checkWorld(Location l) {
        // if the user has defined a world whitelist
        if (!BetterFishing.mainConfig.worldWhitelist()) {
            return true;
        }

        // Gets a list of user defined regions
        List<String> whitelistedWorlds = BetterFishing.mainConfig.getAllowedWorlds();
        if (l.getWorld() == null) {
            return false;
        } else {
            return whitelistedWorlds.contains(l.getWorld().getName());
        }
    }

}
