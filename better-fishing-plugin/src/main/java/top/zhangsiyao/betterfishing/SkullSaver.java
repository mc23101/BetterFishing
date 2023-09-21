package top.zhangsiyao.betterfishing;

import org.bukkit.event.Listener;

public class SkullSaver implements Listener {
    
//    // EventPriority.HIGHEST makes this run last so it can listen to the cancels of protection plugins like Towny
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onBreak(BlockBreakEvent event) {
//        if (event.isCancelled()) return;
//        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
//        Block block = event.getBlock();
//
//        if (!isHead(block)) return;
//        if (block.getDrops().isEmpty()) return;
//
//        BlockState state = event.getBlock().getState();
//        Skull skullMeta = (Skull) state;
//        if (!FishUtils.isFish(skullMeta)) return;
//
//
//        ItemStack stack = block.getDrops().iterator().next().clone();
//        event.setCancelled(true);
//
//        try {
//            Fish f = FishUtils.getFish(skullMeta, event.getPlayer());
//
//            stack.setItemMeta(f.give(f.getFactory().getChosenRandomIndex()).getItemMeta());
//            block.setType(Material.AIR);
//            block.getWorld().dropItem(block.getLocation(), stack);
//            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_BONE_BLOCK_BREAK, 1, 1);
//        } catch (InvalidFishException exception) {
//            BetterFishing.logger.severe(() -> String.format("Error fetching fish from config at location: " +
//                "x:%d y:%d z:%d world:%s",
//                block.getLocation().getBlockX(),
//                block.getLocation().getBlockY(),
//                block.getLocation().getBlockZ(),
//                block.getLocation().getBlock().getWorld().getName()
//                ));
//        }
//
//
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onPlace(BlockPlaceEvent event) {
//
//        if (event.isCancelled()) {
//            return;
//        }
//
//        Block block = event.getBlock();
//        ItemStack stack = event.getItemInHand();
//
//        if (stack.getAmount() == 0 || !stack.hasItemMeta()) {
//            return;
//        }
//
//        if (FishUtils.isFish(stack)) {
//            if (BetterFishing.mainConfig.blockPlacingHeads()) {
//                event.setCancelled(true);
//                new Message(ConfigMessage.FISH_CANT_BE_PLACED).broadcast(event.getPlayer(), true, false);
//                return;
//            }
//
//            if (block.getState() instanceof Skull) {
//                Fish fish = FishUtils.getFish(stack);
//                if (fish != null) {
//                    BlockState state = block.getState();
//                    Skull sm = (Skull) state;
//                    WorthNBT.setNBT(sm, fish);
//                    sm.update();
//                }
//            } else {
//                event.setCancelled(true);
//            }
//        }
//    }
//
//    private boolean isHead(final Block block) {
//        return block.getType() == Material.PLAYER_HEAD || block.getType() == Material.PLAYER_WALL_HEAD;
//    }
    
}
