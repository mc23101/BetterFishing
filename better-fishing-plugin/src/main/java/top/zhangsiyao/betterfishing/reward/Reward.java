package top.zhangsiyao.betterfishing.reward;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.utils.FishUtils;
import top.zhangsiyao.betterfishing.utils.MaterialUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.logging.Level;

public class Reward {

    RewardType type;
    String action;

    Vector fishVelocity;

    Plugin plugin = JavaPlugin.getProvidingPlugin(getClass());

    public Reward(String value) {
        String[] split = value.split(":");

        if (split.length < 2) {
            BetterFishing.logger.log(Level.WARNING, value + " 我无法解析这个奖励");
            this.type = RewardType.BAD_FORMAT;
        } else {
            try {

                this.type = RewardType.valueOf(split[0].toUpperCase());
                this.action = StringUtils.join(split, ":", 1, split.length);

            } catch (IllegalArgumentException e) {
                this.type = RewardType.OTHER;
                this.action = value;
            }

        }
    }
    public String getAction() {
        return action;
    }

    public void run(OfflinePlayer player, Location hookLocation) {
        Player p = null;

        if (player.isOnline()) p = (Player) player;

        switch (type) {
            case COMMAND:
                String inputCommand = this.action
                        .replace("{player}", player.getName());
                if (hookLocation != null) {
                    inputCommand = inputCommand
                            .replace("{x}", Double.toString(hookLocation.getX()))
                            .replace("{y}", Double.toString(hookLocation.getY()))
                            .replace("{z}", Double.toString(hookLocation.getZ()))
                            .replace("{world}", hookLocation.getWorld().getName());
                }

                // 执行指令
                String finalCommand = inputCommand;
                Bukkit.getScheduler().callSyncMethod(plugin, () ->
                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), finalCommand));
                break;
            case EFFECT:
                if (p != null) {
                    String[] parsedEffect = action.split(",");
                    // Adds a potion effect in accordance to the config.yml "EFFECT:" value

                    p.addPotionEffect(new PotionEffect(Objects.requireNonNull(PotionEffectType.getByName(parsedEffect[0])), Integer.parseInt(parsedEffect[2]) * 20, Integer.parseInt(parsedEffect[1])));
                }

                break;
            case HEALTH:
                // checking the player doesn't have a special effect thingy on

                if (p != null) {
                    double newhealth = p.getHealth() + Integer.parseInt(action);
                    // checking the new health won't go above 20
                    p.setHealth(newhealth > 20 ? 20 : newhealth < 0 ? 0 : newhealth);
                }

                break;
            case HUNGER:

                if (p != null) {
                    p.setFoodLevel(p.getFoodLevel() + Integer.parseInt(action));
                }

                break;
            case ITEM:
                if (p != null) {
                    String[] parsedItem = action.split(" ");
                    FishUtils.giveItems(Collections.singletonList(new ItemStack(MaterialUtils.getMaterial(parsedItem[0]),parsedItem.length>1?Integer.parseInt(parsedItem[1]):1)), p);
                }
                break;
            case MESSAGE:
                if (p != null) {
                    p.sendMessage(action);
                }

                break;
            case MONEY:
                if (BetterFishing.econ != null) BetterFishing.econ.depositPlayer(player, Integer.parseInt(action));
                break;
            default:
                BetterFishing.logger.log(Level.SEVERE, "执行奖励发生异常.");
        }
    }

    public RewardType getType() {
        return type;
    }

    public void setFishVelocity(Vector fishVelocity) {
        this.fishVelocity = fishVelocity;
    }
}
