package top.zhangsiyao.betterfishing.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.constant.MessageKey;
import top.zhangsiyao.betterfishing.item.BaitItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Command implements CommandExecutor , TabCompleter {

    private final BetterFishing plugin;

    public Command(BetterFishing plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        Player player=(Player) sender;
        if(args.length>0&&args[0].equals("give")&&player.isOp()){
            new GiveCommand(plugin).onCommand(sender,command,label,args);
        }else if(args.length>0&&args[0].equals("gui")&&player.isOp()){
            new GUICommand(plugin).onCommand(sender,command,label,args);
        } else if(args.length>0&&args[0].equals("reload")&&player.isOp()){
            plugin.reload();
            player.sendMessage("配置文件重新加载成功");
            player.sendMessage("加载鱼竿："+ BetterFishing.rodMap.size()+"个");
            player.sendMessage("加载鱼饵："+BetterFishing.baitMap.size()+"个");
            player.sendMessage("加载fish："+BetterFishing.allFishes.keySet()+"个");
        } else {
            List<String> commandInfo = BetterFishing.messageConfig.getList(MessageKey.command_info);
            StringBuilder message= new StringBuilder();
            for(String m:commandInfo){
                message.append(m).append("\n");
            }
            player.sendMessage(message.toString());
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            return new ArrayList<>();
        }
        Player player=(Player) sender;
        if(args.length==1){
            return new ArrayList<>(Arrays.asList("give","gui","shop","reload"));
        }else if(args[0].equals("give")&&player.isOp()){
            if(args.length==2){
                return new ArrayList<>(Arrays.asList("rod","bait","fish"));
            }
            if(args.length==3){
                if (args[1].equals("rod")){
                    return new ArrayList<>(BetterFishing.rodMap.keySet());
                }else if(args[1].equals("bait")){
                    return new ArrayList<>(BetterFishing.baitMap.keySet());
                }else if(args[1].equals("fish")){
                    return new ArrayList<>(BetterFishing.allFishes.keySet());
                }
            }
        }else if(args[0].equals("gui")){
            if(args.length==2){
                return new ArrayList<>(Arrays.asList("bait","fish","rarity","rod"));
            }
        }
        return new ArrayList<>();
    }

}
