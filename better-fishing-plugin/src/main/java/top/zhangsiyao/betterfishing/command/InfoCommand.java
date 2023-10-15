package top.zhangsiyao.betterfishing.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.gui.FishItemsGui;
import top.zhangsiyao.betterfishing.item.FishItem;
import top.zhangsiyao.betterfishing.utils.FishUtils;

public class InfoCommand implements CommandExecutor {

    final BetterFishing plugin;

    public InfoCommand(BetterFishing plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        Player player=(Player) sender;
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        FishItem fish = FishUtils.getFish(itemInMainHand);
        if(fish!=null){
            player.closeInventory();
            player.openInventory(FishItemsGui.getItemsGui(fish,1));
        }
        return true;
    }
}
