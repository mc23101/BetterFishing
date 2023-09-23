package top.zhangsiyao.betterfishing.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import top.zhangsiyao.betterfishing.item.BRarity;
import top.zhangsiyao.betterfishing.item.BaitItem;
import top.zhangsiyao.betterfishing.item.FishItem;
import top.zhangsiyao.betterfishing.item.Rod;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils
{
    private static final Pattern HEX_PATTERN = Pattern.compile("&#" + "([A-Fa-f0-9]{6})");
    private static final char COLOR_CHAR = '§';
    public static String translateHexColorCodes(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    public static String parseTest(String text,Object...objects){
        String result=text;
        for(Object o:objects){
            if(o instanceof Player){
                Player player=(Player) o;
                result=result.replaceAll("\\{player}",player.getName());
            }else if(o instanceof BaitItem){
                BaitItem baitItem=(BaitItem) o;
                result=result.replaceAll("\\{baitName}",baitItem.getDisplayName());
            }else if(o instanceof Rod){
                Rod rod=(Rod) o;
                result=result.replaceAll("\\{rodName}",rod.getDisplayName());
            }else if(o instanceof FishItem){
                FishItem fishItem=(FishItem) o;
                result=result.replaceAll("\\{fishName}", fishItem.getDisplayName());
            }else if(o instanceof BRarity){
                BRarity rarity=(BRarity) o;
                result=result.replaceAll("\\{rarityName}",rarity.getName());
            }
        }
        return result;
    }
}
