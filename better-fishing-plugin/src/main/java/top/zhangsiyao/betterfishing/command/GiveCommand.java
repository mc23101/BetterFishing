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
        if(args[1].equals("fish")&&args.length==5){
            String fishName=args[2];
            String playerName=args[3];
            int count=Integer.parseInt(args[4]);
            Player player = Bukkit.getServer().getPlayer(playerName);
            if(player==null||!player.isOnline()){
                sender.sendMessage(playerName+"该玩家不在线!");
                return false;
            }
            if(!BetterFishing.allFishes.containsKey(fishName)){
                sender.sendMessage(fishName+"该鱼不存在在!");
                return false;
            }
            ItemStack give = BetterFishing.allFishes.get(fishName).give(player, -1);
            give.setAmount(count);
            player.getInventory().addItem(give);
            return true;
        }else if(args[1].equals("bait")&&args.length==5){
            String baitName=args[2];
            String playerName=args[3];
            int count=Integer.parseInt(args[4]);
            Player player = Bukkit.getServer().getPlayer(playerName);
            if(player==null||!player.isOnline()){
                sender.sendMessage(playerName+"该玩家不在线!");
                return false;
            }
            ItemStack give = BetterFishing.baitMap.get(baitName).give(player, -1);
            give.setAmount(count);
            player.getInventory().addItem(give);
            return true;
        }else if(args[1].equals("rod")&&args.length==4){
            String rodName=args[2];
            String playerName=args[3];
            Player player = Bukkit.getServer().getPlayer(playerName);
            if(player==null||!player.isOnline()){
                sender.sendMessage(playerName+"该玩家不在线!");
                return false;
            }
            if(!BetterFishing.rodMap.containsKey(rodName)){
                sender.sendMessage(rodName+"该鱼竿不存在在");
                return false;
            }
            player.getInventory().addItem(BetterFishing.rodMap.get(rodName).give(player,-1));
            return true;
        }else {
            sender.sendMessage("指令参数长度异常");
            return false;
        }
    }



}
