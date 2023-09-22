package top.zhangsiyao.betterfishing.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import top.zhangsiyao.betterfishing.BetterFishing;

public class GiveCommand implements CommandExecutor {


    private final BetterFishing plugin;

    public GiveCommand(BetterFishing plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player=(Player) sender;
        if(args.length!=3){
            sender.sendMessage("指令参数长度异常");
            return false;
        }
        if(args[1].equals("fish")){
            String fishName=args[2];
            if(!BetterFishing.allFishes.containsKey(fishName)){
                sender.sendMessage(fishName+"该鱼不存在在");
                return false;
            }
            player.getInventory().addItem(BetterFishing.allFishes.get(fishName).give(player,-1));
            return true;
        }else if(args[1].equals("bait")){
            String baitName=args[2];
            if(!BetterFishing.baitMap.containsKey(baitName)){
                sender.sendMessage(baitName+"该鱼饵不存在在");
                return false;
            }
            player.getInventory().addItem(BetterFishing.baitMap.get(baitName).give(player,-1));
            return true;
        }else if(args[1].equals("rod")){
            String rodName=args[2];
            if(!BetterFishing.rodMap.containsKey(rodName)){
                sender.sendMessage(rodName+"该鱼竿不存在在");
                return false;
            }
            player.getInventory().addItem(BetterFishing.rodMap.get(rodName).give(player,-1));
            return true;
        }

        return false;
    }



}
