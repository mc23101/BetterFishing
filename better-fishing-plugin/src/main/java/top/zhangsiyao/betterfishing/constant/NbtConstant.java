package top.zhangsiyao.betterfishing.constant;

import org.bukkit.plugin.java.JavaPlugin;
import top.zhangsiyao.betterfishing.utils.NbtUtils;

import java.util.Locale;

public class NbtConstant {
    public static final String BF_COMPOUND = JavaPlugin.getProvidingPlugin(NbtUtils.class).getName().toLowerCase(Locale.ROOT);
    public static final String BF_FISH_PLAYER = "bf-fish-player";
    public static final String BF_FISH_LENGTH = "bf-fish-length";
    public static final String BF_FISH_NAME = "bf-fish-name";
    public static final String BF_FISH_RANDOM_INDEX = "bf-fish-random-index";
    public static final String BF_BAIT_NAME = "bf-bait-name";
    public static final String PUBLIC_BUKKIT_VALUES = "PublicBukkitValues";
    public static final String ROD_NAME="rod-name";
    public static final String EXTRA_FISH="extra-fish";
    public static final String FISHING_SPEED = "fishing-speed";
    public static final String DOUBLE_DROP="double-drop";

}
