package top.zhangsiyao.betterfishing.utils;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.zhangsiyao.betterfishing.constant.NbtConstant;
import top.zhangsiyao.betterfishing.item.Attachment;

public class NbtUtils {

    public static void setString(final @NotNull NBTCompound nbtCompound, final String key,final String value){
        NBTCompound bfCompound = nbtCompound.getOrCreateCompound(NbtConstant.BF_COMPOUND);
        bfCompound.setString(key,value);
    }


    public static boolean hasKey(final @NotNull NBTCompound nbtCompound, final String key) {
        NBTCompound bfCompound = nbtCompound.getOrCreateCompound(NbtConstant.BF_COMPOUND);
        return bfCompound.hasTag(key);
    }
    
    public static @Nullable String getString(final @NotNull NBTCompound nbtCompound, final String key) {
        NBTCompound bfCompound = nbtCompound.getOrCreateCompound(NbtConstant.BF_COMPOUND);
        return bfCompound.getString(key);
    }

    public static void removeNbt(final @NotNull NBTCompound nbtCompound, final String key){
        NBTCompound bfCompound = nbtCompound.getOrCreateCompound(NbtConstant.BF_COMPOUND);
        bfCompound.removeKey(key);
    }



    /**
     * Returns the NBT Version of the item
     * It does not mean that this is an emf item.
     *
     * @param compound compound
     * @return nbt version
     */
    public static NbtVersion getNbtVersion(final NBTCompound compound) {
        if (Boolean.TRUE.equals(compound.hasTag(NbtConstant.BF_COMPOUND)))
            return NbtVersion.COMPAT; //def an emf item
        if (Boolean.TRUE.equals(compound.hasTag(NbtConstant.PUBLIC_BUKKIT_VALUES)))
            return NbtVersion.LEGACY;
        return NbtVersion.NBTAPI;
    }

    public static NbtVersion getNbtVersion(final ItemStack itemStack) {
        return getNbtVersion(new NBTItem(itemStack));
    }

    @Contract("_ -> new")
    public static @NotNull NamespacedKey getNamespacedKey(final String key) {
        return new NamespacedKey(JavaPlugin.getProvidingPlugin(NbtUtils.class), key);
    }


    public enum NbtVersion {
        LEGACY, //pre nbt-api pr
        NBTAPI, //nbt-api pr
        COMPAT //compatible with everything :)
    }

}
