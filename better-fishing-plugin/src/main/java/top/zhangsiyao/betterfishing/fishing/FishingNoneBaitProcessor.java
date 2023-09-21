package top.zhangsiyao.betterfishing.fishing;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.constant.NbtConstant;
import top.zhangsiyao.betterfishing.item.BRarity;
import top.zhangsiyao.betterfishing.item.FishItem;
import top.zhangsiyao.betterfishing.item.Rod;
import top.zhangsiyao.betterfishing.utils.FishUtils;
import top.zhangsiyao.betterfishing.utils.NbtUtils;

import java.util.*;
import java.util.logging.Level;

public class FishingNoneBaitProcessor implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public static void process(PlayerFishEvent event) {

        ItemStack rodInHand = event.getPlayer().getInventory().getItemInMainHand();
        NBTItem nbtItem = new NBTItem(rodInHand);


        Rod rod=null;
        if(NbtUtils.hasKey(nbtItem, NbtConstant.ROD_NAME)){
            String rodName=NbtUtils.getString(nbtItem, NbtConstant.ROD_NAME);
            if(BetterFishing.rodMap.containsKey(rodName)){
                rod= BetterFishing.rodMap.get(rodName);
            }
        }

        if(rod==null){
            return;
        }

        // 判断鱼竿有没有时间加成
        int maxTime= BetterFishing.mainConfig.getFishingMaxWaitTime();
        int minTime= BetterFishing.mainConfig.getFishingMinWaitTime();
        if(rod!=null&&rod.getFishingSpeed()!=null){
            maxTime= (int)Math.floor(BetterFishing.mainConfig.getFishingMaxWaitTime()*(1-Float.parseFloat(rod.getFishingSpeed())));
            minTime=(int)Math.floor(BetterFishing.mainConfig.getFishingMinWaitTime()*(1-Float.parseFloat(rod.getFishingSpeed())));
        }
        event.getHook().setMinWaitTime(minTime);
        event.getHook().setMaxWaitTime(maxTime);


        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            // 获取掉到的鱼
            ItemStack fish = randomFish(event.getPlayer(), event.getHook().getLocation(), event.getPlayer().getInventory().getItemInMainHand(), true, true);

            if (fish == null) {
                return;
            }

            //判断是否触发双倍奖励
            if (rod != null && rod.getDoubleDrop() != null) {
                Random rand = new Random();
                float randDouble = rand.nextFloat();
                if (randDouble <= Float.parseFloat(rod.getDoubleDrop())) {
                    fish.setAmount(2);
                    event.getPlayer().sendMessage("恭喜你获得双倍奖励!");
                }
            }

            // 替换获取的物品
            Item nonCustom = (Item) event.getCaught();
            if (nonCustom != null) {
                if (fish.getType().isAir()) {
                    nonCustom.remove();
                } else {
                    nonCustom.setItemStack(fish);
                }
            }
        }
    }

    /**
     * 随机获取钓鱼结果
     * */
    private static ItemStack randomFish(Player player, Location location, ItemStack fishingRod, boolean runRewards, boolean sendMessages){

        if (!FishUtils.checkRegion(location, BetterFishing.mainConfig.getAllowedRegions())) {
            return null;
        }

        if (!FishUtils.checkWorld(location)) {
            return null;
        }


        FishItem fish=null;

        fish=chooseNonBaitFish(player,location);
        return fish.give(player,-1);
    }

    public static FishItem chooseNonBaitFish(Player player, Location location) {

        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        NBTItem nbtItem=new NBTItem(itemInMainHand);

        Map<BRarity,List<FishItem>> curFish=new HashMap<>(BetterFishing.globalRarityFishes);

        if(NbtUtils.hasKey(nbtItem, NbtConstant.ROD_NAME)){
            Rod rod=BetterFishing.rodMap.get(NbtUtils.getString(nbtItem,NbtConstant.ROD_NAME));
            System.out.println(BetterFishing.extraRarityFishes.keySet());
            if(rod.getExtraFish()!=null&&BetterFishing.extraRarityFishes.containsKey(rod.getExtraFish())){
                Map<BRarity,List<FishItem>> map= BetterFishing.extraRarityFishes.get(rod.getExtraFish());
                for(BRarity r:map.keySet()){
                    if(curFish.containsKey(r)){
                        curFish.get(r).addAll(map.get(r));
                    }else {
                        curFish.put(r,new ArrayList<>(map.get(r)));
                    }
                }
            }
        }


        BRarity fishRarity = randomWeightedRarity(player, 1, null, curFish.keySet());
        if (fishRarity == null) {
            BetterFishing.logger.log(Level.SEVERE, "Could not determine a rarity for fish for " + player.getName());
            return null;
        }

        FishItem fish = randomFish(fishRarity, location, player, 1, null);
        if (fish == null) {
            BetterFishing.logger.log(Level.SEVERE, "Could not determine a fish for " + player.getName());
            return null;
        }
        fish.setFisherman(player.getUniqueId());
        return fish;
    }


    public static FishItem randomFish(BRarity r, Location l, Player p, double boostRate, List<FishItem> boostedFish) {
        if (r == null) return null;
        // will store all the fish that match the player's biome or don't discriminate biomes

        List<FishItem> available = new ArrayList<>();

        ItemStack itemInMainHand = p.getInventory().getItemInMainHand();
        NBTItem nbtItem=new NBTItem(itemInMainHand);

        Map<BRarity,List<FishItem>> curFish=new HashMap<>(BetterFishing.globalRarityFishes);

        if(NbtUtils.hasKey(nbtItem, NbtConstant.ROD_NAME)){
            Rod rod=BetterFishing.rodMap.get(NbtUtils.getString(nbtItem,NbtConstant.ROD_NAME));
            if(rod.getExtraFish()!=null&&BetterFishing.extraRarityFishes.containsKey(rod.getExtraFish())){
                Map<BRarity,List<FishItem>> map= BetterFishing.extraRarityFishes.get(rod.getExtraFish());
                for(BRarity rarity:map.keySet()){
                    if(curFish.containsKey(rarity)){
                        curFish.get(rarity).addAll(map.get(rarity));
                    }else {
                        curFish.put(rarity,new ArrayList<>(map.get(rarity)));
                    }
                }
            }
        }


        // 防止/emf admin重载导致插件无法获得稀有性
        if (curFish.get(r) == null)
            r = randomWeightedRarity(p, 1, null, curFish.keySet());

        for (FishItem f : curFish.get(r)) {

            if (!(boostRate != -1 || boostedFish == null || boostedFish.contains(f))) {
                continue;
            }

            available.add(f);
        }

        // if the config doesn't define any fish that can be fished in this biome.
        if (available.isEmpty()) {
            BetterFishing.logger.log(Level.WARNING, "There are no fish of the rarity " + r.getName() + " that can be fished at (x=" + l.getX() + ", y=" + l.getY() + ", z=" + l.getZ() + ")");
            return null;
        }

        FishItem returningFish;

        // 检查是否需要为鱼做权重计算
        returningFish = randomWeightedFish(available, boostRate, boostedFish);

        return returningFish;
    }

    /**
     * 权重法获取稀有度
     * */
    public static BRarity randomWeightedRarity(Player fisher, double boostRate, Set<BRarity> boostedRarities, Set<BRarity> totalRarities) {

        int idx = 0;
        List<BRarity> allowedRarities = new ArrayList<>(totalRarities);
        double totalWeight = 0;

        for (BRarity r : allowedRarities) {
            int addWeight=0;
            if(fisher!=null){
                ItemStack mainHand=fisher.getInventory().getItemInMainHand();
                NBTItem nbtItem=new NBTItem(mainHand);
                Rod rod=BetterFishing.rodMap.get(NbtUtils.getString(nbtItem,NbtConstant.ROD_NAME));
                if(rod.getRarities().containsKey(r.getName())){
                    addWeight=rod.getRarities().get(r.getName());
                }
            }
            if (boostRate != -1.0 && boostedRarities != null && boostedRarities.contains(r)) {
                totalWeight += ((r.getWeight()+addWeight) * boostRate);
            } else {
                totalWeight += (r.getWeight()+addWeight);
            }
        }

        for (double r = Math.random() * totalWeight; idx < allowedRarities.size() - 1; ++idx) {
            BRarity rarity=allowedRarities.get(idx);
            int addWeight=0;
            if(fisher!=null){
                ItemStack mainHand=fisher.getInventory().getItemInMainHand();
                NBTItem nbtItem=new NBTItem(mainHand);
                Rod rod=BetterFishing.rodMap.get(NbtUtils.getString(nbtItem,NbtConstant.ROD_NAME));
                if(rod.getRarities().containsKey(rarity.getName())){
                    addWeight=rod.getRarities().get(rarity.getName());
                }
            }
            if (boostRate != -1.0 && boostedRarities != null && boostedRarities.contains(rarity)) {
                r -= (rarity.getWeight()+addWeight) * boostRate;
            } else {
                r -= rarity.getWeight()+addWeight;
            }
            if (r <= 0.0) break;
        }

        if (allowedRarities.isEmpty()) {
            BetterFishing.logger.log(Level.SEVERE, "There are no rarities for the user " + fisher.getName() + " to fish. They have received no fish.");
            return null;
        }

        return allowedRarities.get(idx);
    }

    /**
     * 权重法获取钓鱼结果
     * */
    private static FishItem randomWeightedFish(List<FishItem> fishList, double boostRate, List<FishItem> boostedFish) {
        double totalWeight = 0;

        for (FishItem fish : fishList) {
            // when boostRate is -1, we need to guarantee a fish, so the fishList has already been moderated to only contain
            // boosted fish. The other 2 check that the plugin wants the bait calculations too.
            if (boostRate != -1 && boostedFish != null && boostedFish.contains(fish)) {

                if (fish.getWeight() == 0.0d) totalWeight += (1 * boostRate);
                else
                    totalWeight += fish.getWeight() * boostRate;
            } else {
                if (fish.getWeight() == 0.0d) totalWeight += 1;
                else totalWeight += fish.getWeight();
            }
        }

        int idx = 0;
        for (double r = Math.random() * totalWeight; idx < fishList.size() - 1; ++idx) {

            if (fishList.get(idx).getWeight() == 0.0d) {
                if (boostRate != -1 && boostedFish != null && boostedFish.contains(fishList.get(idx))) {
                    r -= 1 * boostRate;
                } else {
                    r -= 1;
                }
            } else {
                if (boostRate != -1 && boostedFish != null && boostedFish.contains(fishList.get(idx))) {
                    r -= fishList.get(idx).getWeight() * boostRate;
                } else {
                    r -= fishList.get(idx).getWeight();
                }
            }

            if (r <= 0.0) break;
        }

        return fishList.get(idx);
    }



}
