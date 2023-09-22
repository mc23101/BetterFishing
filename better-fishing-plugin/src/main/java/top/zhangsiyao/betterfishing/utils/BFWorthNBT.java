package top.zhangsiyao.betterfishing.utils;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;
import org.omg.CORBA.PUBLIC_MEMBER;
import top.zhangsiyao.betterfishing.constant.NbtConstant;
import top.zhangsiyao.betterfishing.item.BaitItem;
import top.zhangsiyao.betterfishing.item.FishItem;
import top.zhangsiyao.betterfishing.item.Rod;

public class BFWorthNBT {
    public static ItemStack setFishNBT(ItemStack fishItem, FishItem fish) {
        // creates key and plops in the value of "value"
        NBTItem nbtItem = new NBTItem(fishItem);

        NBTCompound bfCompound = nbtItem.getOrCreateCompound(NbtConstant.BF_COMPOUND);
        if (fish.getLength() > 0)
            bfCompound.setFloat(NbtConstant.BF_FISH_LENGTH, fish.getLength());
        if ( fish.getFisherman() != null)
            bfCompound.setString(NbtConstant.BF_FISH_PLAYER, fish.getFisherman().toString());
        bfCompound.setString(NbtConstant.BF_FISH_NAME, fish.getFishName());
        bfCompound.setInteger(NbtConstant.BF_FISH_RANDOM_INDEX, fish.getItemFactory().getChosenRandomIndex());
        return nbtItem.getItem();
    }

    public static ItemStack setBaitNBT(ItemStack baitItem, BaitItem bait){
        NBTItem nbtItem = new NBTItem(baitItem);
        NBTCompound bfCompound = nbtItem.getOrCreateCompound(NbtConstant.BF_COMPOUND);
        bfCompound.setString(NbtConstant.BF_BAIT_NAME,bait.getBaitName());
        return nbtItem.getItem();
    }

    public static ItemStack setRodNBT(ItemStack rodItem, Rod rod){
        NBTItem nbtItem = new NBTItem(rodItem);
        NBTCompound bfCompound = nbtItem.getOrCreateCompound(NbtConstant.BF_COMPOUND);
        bfCompound.setString(NbtConstant.ROD_NAME,rod.getRodName());
        return nbtItem.getItem();
    }

}
