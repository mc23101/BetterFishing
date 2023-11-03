package top.zhangsiyao.betterfishing.command;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.constant.MessageKey;
import top.zhangsiyao.betterfishing.gui.RodAttachmentGUI;
import top.zhangsiyao.betterfishing.utils.FishUtils;

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
        if(args.length>0&&args[0].equals("give")&&sender.isOp()){
            new GiveCommand(plugin).onCommand(sender,command,label,args);
        }else if(args.length>0&&args[0].equals("gui")&&sender.isOp()){
            new GUICommand(plugin).onCommand(sender,command,label,args);
        } else if(args.length>0&&args[0].equals("reload")&&sender.isOp()){
            plugin.reload();
            sender.sendMessage("配置文件重新加载成功");
            sender.sendMessage("加载鱼竿："+ BetterFishing.rodMap.size()+"个");
            sender.sendMessage("加载鱼饵："+BetterFishing.baitMap.size()+"个");
            sender.sendMessage("加载fish："+BetterFishing.allFishes.size()+"个");
        }else if(args.length>0&&args[0].equals("info")){
            new InfoCommand(plugin).onCommand(sender,command,label,args);
        }else if(args.length>0&&args[0].equals("attachment")){
            if(!(sender instanceof Player)){
                return false;
            }
            Player player=(Player) sender;
            if(FishUtils.isRod(player.getInventory().getItemInMainHand())){
                player.openInventory(new RodAttachmentGUI(player).getInventory());
            }else {
                player.sendMessage("请手持鱼竿！");
            }
        } else {
            List<String> commandInfo = BetterFishing.messageConfig.getList(MessageKey.command_info);
            StringBuilder message= new StringBuilder();
            for(String m:commandInfo){
                message.append(m).append("\n");
            }
            sender.sendMessage(message.toString());
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
            return new ArrayList<>(Arrays.asList("give","gui","shop","reload","info","attachment"));
        }else if(args[0].equals("give")&&player.isOp()){
            if(args.length==2){
                return new ArrayList<>(Arrays.asList("rod","bait","fish","attachment"));
            }
            if(args.length==3){
                switch (args[1]) {
                    case "rod":
                        return new ArrayList<>(BetterFishing.rodMap.keySet());
                    case "bait":
                        return new ArrayList<>(BetterFishing.baitMap.keySet());
                    case "fish":
                        return new ArrayList<>(BetterFishing.allFishes.keySet());
                    case "attachment":
                        return new ArrayList<>(BetterFishing.attachments.keySet());
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
