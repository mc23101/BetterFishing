package top.zhangsiyao.betterfishing.utils;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;
import top.zhangsiyao.betterfishing.constant.NbtConstant;
import top.zhangsiyao.betterfishing.item.FishItem;

public class BFWorthNBT {
    public static ItemStack setNBT(ItemStack fishItem, FishItem fish) {
        // creates key and plops in the value of "value"
        NBTItem nbtItem = new NBTItem(fishItem);

        NBTCompound emfCompound = nbtItem.getOrCreateCompound(NbtConstant.BF_COMPOUND);
        if (fish.getLength() > 0)
            emfCompound.setFloat(NbtConstant.BF_FISH_LENGTH, fish.getLength());
        if ( fish.getFisherman() != null)
            emfCompound.setString(NbtConstant.BF_FISH_PLAYER, fish.getFisherman().toString());
        emfCompound.setString(NbtConstant.BF_FISH_NAME, fish.getFishName());
        emfCompound.setInteger(NbtConstant.BF_FISH_RANDOM_INDEX, fish.getItemFactory().getChosenRandomIndex());
        return nbtItem.getItem();
    }

}
