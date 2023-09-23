package top.zhangsiyao.betterfishing.fishing;


import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
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
import top.zhangsiyao.betterfishing.event.FishTitleEvent;
import top.zhangsiyao.betterfishing.item.BRarity;
import top.zhangsiyao.betterfishing.item.BaitItem;
import top.zhangsiyao.betterfishing.item.FishItem;
import top.zhangsiyao.betterfishing.item.Rod;
import top.zhangsiyao.betterfishing.utils.FishUtils;
import top.zhangsiyao.betterfishing.utils.NbtUtils;

import java.util.*;
import java.util.logging.Level;


public class FishingBaitProcessor implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public static void process(PlayerFishEvent event) {
        ItemStack rodInHand = event.getPlayer().getInventory().getItemInMainHand();
        FishUtils.refreshRodLore(rodInHand);
        Rod rod=FishUtils.getRod(rodInHand);
        if(rod==null){
            return;
        }
        if(!FishUtils.useBait(rodInHand)){
            return;
        }
        BaitItem bait=FishUtils.getBaitByRod(rodInHand);
        if(bait == null || !FishUtils.decreaseBait(event.getPlayer(), bait.getBaitName())){
            if(bait==null){
                event.getPlayer().sendMessage(BetterFishing.messageConfig.getBaitNotExistMessage());
            }else {
                event.getPlayer().sendMessage(BetterFishing.messageConfig.getBaitNotEnoughMessage(bait.getBaitName()));
            }
            NBTItem nbtItem=new NBTItem(rodInHand,true);
            NbtUtils.removeNbt(nbtItem,NbtConstant.USE_BAIT_NAME);
            FishUtils.refreshRodLore(rodInHand);
            Bukkit.getServer().getPluginManager().callEvent(new PlayerFishEvent(event.getPlayer(), event.getCaught(), event.getHook(),event.getState()));
            return;
        }


        // 判断鱼竿有没有时间加成
        int maxTime= BetterFishing.mainConfig.getFishingMaxWaitTime();
        int minTime= BetterFishing.mainConfig.getFishingMinWaitTime();
        if(rod.getFishingSpeed() != null){
            maxTime= (int)Math.floor(BetterFishing.mainConfig.getFishingMaxWaitTime()*(1-Float.parseFloat(rod.getFishingSpeed())));
            minTime=(int)Math.floor(BetterFishing.mainConfig.getFishingMinWaitTime()*(1-Float.parseFloat(rod.getFishingSpeed())));
        }
        event.getHook().setMinWaitTime(minTime);
        event.getHook().setMaxWaitTime(maxTime);


        ItemStack fish = null;
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            // 获取掉到的鱼
            fish = getRandomFish(event.getPlayer(), event.getHook().getLocation(), rod,bait);


            if (fish == null) {
                return;
            }

            //判断是否触发双倍奖励
            if (rod.getDoubleDrop() != null) {
                Random rand = new Random();
                float randDouble = rand.nextFloat();
                if (randDouble <= Float.parseFloat(rod.getDoubleDrop())) {
                    fish.setAmount(2);
                    event.getPlayer().sendMessage(BetterFishing.messageConfig.getDoubleDropMessage());
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
        Bukkit.getServer().getPluginManager().callEvent(new FishTitleEvent(fish,event.getState(),event.getPlayer()));
    }



    /**
     * 随机获取钓鱼结果
     * */
    private static ItemStack getRandomFish(Player player, Location location, Rod rod,BaitItem bait){

        if (!FishUtils.checkRegion(location, BetterFishing.mainConfig.getAllowedRegions())) {
            return null;
        }

        if (!FishUtils.checkWorld(location)) {
            return null;
        }

        FishItem fish;
        Map<BRarity,List<FishItem>> curFish=new HashMap<>(BetterFishing.globalRarityFishes);

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

        BRarity fishRarity = randomWeightedRarity(player, 1, null, curFish.keySet(),rod,bait);
        if (fishRarity == null) {
            BetterFishing.logger.log(Level.SEVERE,  player.getName()+"无法获取稀有度 " );
            return null;
        }

        fish = randomFish(fishRarity, location, player, 1, null,curFish,rod,bait);
        if (fish == null) {
            BetterFishing.logger.log(Level.SEVERE, player.getName()+"无法获取钓鱼结果 ");
            return null;
        }
        fish.setFisherman(player.getUniqueId());
        return fish.give(player,-1);
    }

    public static FishItem randomFish(BRarity r, Location l, Player p, double boostRate, List<FishItem> boostedFish, Map<BRarity,List<FishItem>> curFish,Rod rod,BaitItem bait) {
        if (r == null) return null;
        // will store all the fish that match the player's biome or don't discriminate biomes

        List<FishItem> available = new ArrayList<>();

        // 防止/emf admin重载导致插件无法获得稀有性
        if (curFish.get(r) == null)
            r = randomWeightedRarity(p, 1, null, curFish.keySet(),rod,bait);

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
        returningFish = randomWeightedFish(available, boostRate, boostedFish,rod,bait);

        return returningFish;
    }

    /**
     * 权重法获取稀有度
     * */
    public static BRarity randomWeightedRarity(Player fisher, double boostRate, Set<BRarity> boostedRarities, Set<BRarity> totalRarities,Rod rod,BaitItem bait) {

        int idx = 0;
        List<BRarity> allowedRarities = new ArrayList<>(totalRarities);
        double totalWeight = 0;

        for (BRarity r : allowedRarities) {
            int addWeight=0;
            if(rod!=null&&rod.getRarities().containsKey(r.getName())){
                addWeight+=rod.getRarities().get(r.getName());
            }
            if(bait!=null&&bait.getRarity()!=null){
                Set<BRarity> addWeightRarity=new HashSet<>(bait.getRarity());
                if(addWeightRarity.contains(r)){
                    addWeight+=bait.getRarityWeight();
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
            if(rod.getRarities().containsKey(rarity.getName())){
                addWeight+=rod.getRarities().get(rarity.getName());
            }
            if(bait!=null&&bait.getRarity()!=null){
                Set<BRarity> addWeightRarity=new HashSet<>(bait.getRarity());
                if(addWeightRarity.contains(rarity)){
                    addWeight+=bait.getRarityWeight();
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
    private static FishItem randomWeightedFish(List<FishItem> fishList, double boostRate, List<FishItem> boostedFish,Rod rod,BaitItem bait) {
        double totalWeight = 0;

        Set<FishItem> addFishWeight=bait==null?new HashSet<>():new HashSet<>(bait.getFish());

        for (FishItem fish : fishList) {

            double addWeight=0;
            if(addFishWeight.contains(fish)){
                addWeight=bait.getFishWeight();
            }

            // when boostRate is -1, we need to guarantee a fish, so the fishList has already been moderated to only contain
            // boosted fish. The other 2 check that the plugin wants the bait calculations too.
            if (boostRate != -1 && boostedFish != null && boostedFish.contains(fish)) {

                if (fish.getWeight() == 0.0d) totalWeight += (1 * boostRate);
                else
                    totalWeight += (fish.getWeight()+addWeight) * boostRate;
            } else {
                if (fish.getWeight() == 0.0d) totalWeight += 1;
                else totalWeight += (fish.getWeight()+addWeight);
            }
        }

        int idx = 0;
        for (double r = Math.random() * totalWeight; idx < fishList.size() - 1; ++idx) {

            double addWeight=0;
            if(addFishWeight.contains(fishList.get(idx))){
                addWeight=bait.getFishWeight();
            }
            if ((fishList.get(idx).getWeight()+addWeight) == 0.0d) {
                if (boostRate != -1 && boostedFish != null && boostedFish.contains(fishList.get(idx))) {
                    r -= 1 * boostRate;
                } else {
                    r -= 1;
                }
            } else {
                if (boostRate != -1 && boostedFish != null && boostedFish.contains(fishList.get(idx))) {
                    r -= (fishList.get(idx).getWeight()+addWeight) * boostRate;
                } else {
                    r -= (fishList.get(idx).getWeight()+addWeight);
                }
            }

            if (r <= 0.0) break;
        }

        return fishList.get(idx);
    }



}
