package top.zhangsiyao.betterfishing.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.gui.BaitGui;
import top.zhangsiyao.betterfishing.gui.FishGui;
import top.zhangsiyao.betterfishing.gui.RodGui;

public class GUICommand implements CommandExecutor {

    private final BetterFishing plugin;

    public GUICommand(BetterFishing plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        Player player=(Player) sender;
        if(args.length==2){
            if(args[1].equals("bait")){
                player.closeInventory();
                player.openInventory(BaitGui.getBaitGui(player,1));
            }else if(args[1].equals("fish")){
                player.closeInventory();
                player.openInventory(FishGui.getFishGui(player,1));
            }else if(args[1].equals("rod")){
                player.closeInventory();
                player.openInventory(RodGui.getRodGui(player,1));
            }
        }
        return false;
    }
}
