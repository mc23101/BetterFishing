package top.zhangsiyao.betterfishing.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.zhangsiyao.betterfishing.BetterFishing;

public class GiveCommand implements CommandExecutor {


    private final BetterFishing plugin;

    public GiveCommand(BetterFishing plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length > 5 || args.length < 3) {
            sender.sendMessage("指令参数长度异常");
            return false;
        }

        Player player;
        if (args.length == 4) {
            String playerName = args[3];
            player = Bukkit.getServer().getPlayer(playerName);
        } else {
            player = (Player) sender;
        }
        if (player == null || !player.isOnline()) {
            sender.sendMessage("该玩家不存在或不在线!");
            return false;
        }
        String itemName = args[2];
        ItemStack itemStack;
        if (args[1].equals("fish")) {
            if (!BetterFishing.allFishes.containsKey(itemName)) {
                sender.sendMessage(itemName + "该鱼不存在在!");
                return false;
            }
            itemStack = BetterFishing.allFishes.get(itemName).give(player, -1);
        } else if (args[1].equals("bait")) {
            if (!BetterFishing.baitMap.containsKey(itemName)) {
                sender.sendMessage("该诱饵不存在");
                return false;
            }
            itemStack = BetterFishing.baitMap.get(itemName).give(player, -1);
        } else if (args[1].equals("rod")) {
            if (!BetterFishing.rodMap.containsKey(itemName)) {
                sender.sendMessage("该鱼竿不存在");
                return false;
            }
            itemStack = BetterFishing.rodMap.get(itemName).give(player, -1);
        }else if(args[1].equals("attachment")){
            if (!BetterFishing.attachments.containsKey(itemName)) {
                sender.sendMessage("该鱼竿不存在");
                return false;
            }
            itemStack = BetterFishing.attachments.get(itemName).give(player, -1);
        }else {
            sender.sendMessage("你指定的物品分类不存在！");
            return false;
        }

        int count = 1;
        if (args.length == 5) {
            count = Integer.parseInt(args[4]);
        }
        itemStack.setAmount(count);
        player.getInventory().addItem(itemStack);
        return true;
    }



}
