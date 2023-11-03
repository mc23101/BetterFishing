package top.zhangsiyao.betterfishing.utils;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

import top.zhangsiyao.betterfishing.constant.NbtConstant;
import top.zhangsiyao.betterfishing.item.Attachment;
import top.zhangsiyao.betterfishing.item.BaitItem;
import top.zhangsiyao.betterfishing.item.FishItem;
import top.zhangsiyao.betterfishing.item.Rod;

public class BFWorthNBT {
    public static ItemStack setFishNBT(ItemStack fishItem, FishItem fish) {
        // creates key and plops in the value of "value"
        NBTItem nbtItem = new NBTItem(fishItem);
        NBTCompound bfCompound = nbtItem.getOrCreateCompound(NbtConstant.BF_COMPOUND);
        bfCompound.setString(NbtConstant.BF_FISH_NAME, fish.getName());
        bfCompound.setInteger(NbtConstant.BF_FISH_RANDOM_INDEX, fish.getItemFactory().getChosenRandomIndex());
        return nbtItem.getItem();
    }

    public static ItemStack setBaitNBT(ItemStack baitItem, BaitItem bait){
        NBTItem nbtItem = new NBTItem(baitItem);
        NBTCompound bfCompound = nbtItem.getOrCreateCompound(NbtConstant.BF_COMPOUND);
        bfCompound.setString(NbtConstant.BF_BAIT_NAME,bait.getName());
        return nbtItem.getItem();
    }

    public static ItemStack setRodNBT(ItemStack rodItem, Rod rod){
        NBTItem nbtItem = new NBTItem(rodItem);
        NBTCompound bfCompound = nbtItem.getOrCreateCompound(NbtConstant.BF_COMPOUND);
        bfCompound.setString(NbtConstant.ROD_NAME,rod.getName());
        return nbtItem.getItem();
    }

    public static ItemStack setAttachmentNBT(ItemStack itemStack, Attachment attachment){
        NBTItem nbtItem=new NBTItem(itemStack);
        NBTCompound bfCompound=nbtItem.getOrCreateCompound(NbtConstant.BF_COMPOUND);
        bfCompound.setString(NbtConstant.BF_ATTACHMENT_NAME,attachment.getName());
        return nbtItem.getItem();
    }



}
