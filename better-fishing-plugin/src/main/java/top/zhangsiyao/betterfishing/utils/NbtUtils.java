package top.zhangsiyao.betterfishing.utils;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.zhangsiyao.betterfishing.constant.NbtConstant;

public class NbtUtils {

    public static void setFloat(final @NotNull NBTCompound nbtCompound, final String key,final float value){
        NamespacedKey namespacedKey = getNamespacedKey(key);
        NBTCompound publicBukkitValues;
        if(nbtCompound.hasTag(NbtConstant.PUBLIC_BUKKIT_VALUES)){
            publicBukkitValues = nbtCompound.getCompound(NbtConstant.PUBLIC_BUKKIT_VALUES);
        }else {
            publicBukkitValues = nbtCompound.addCompound(NbtConstant.PUBLIC_BUKKIT_VALUES);
        }
        publicBukkitValues.setFloat(namespacedKey.toString(),value);
    }


    public static void setDouble(final @NotNull NBTCompound nbtCompound, final String key,final double value){
        NamespacedKey namespacedKey = getNamespacedKey(key);
        NBTCompound publicBukkitValues;
        if(nbtCompound.hasTag(NbtConstant.PUBLIC_BUKKIT_VALUES)){
            publicBukkitValues = nbtCompound.getCompound(NbtConstant.PUBLIC_BUKKIT_VALUES);
        }else {
            publicBukkitValues = nbtCompound.addCompound(NbtConstant.PUBLIC_BUKKIT_VALUES);
        }
        publicBukkitValues.setDouble(namespacedKey.toString(),value);
    }

    public static void setInteger(final @NotNull NBTCompound nbtCompound, final String key,final int value){
        NamespacedKey namespacedKey = getNamespacedKey(key);
        NBTCompound publicBukkitValues;
        if(nbtCompound.hasTag(NbtConstant.PUBLIC_BUKKIT_VALUES)){
            publicBukkitValues = nbtCompound.getCompound(NbtConstant.PUBLIC_BUKKIT_VALUES);
        }else {
            publicBukkitValues = nbtCompound.addCompound(NbtConstant.PUBLIC_BUKKIT_VALUES);
        }
        publicBukkitValues.setInteger(namespacedKey.toString(),value);
    }

    public static void setString(final @NotNull NBTCompound nbtCompound, final String key,final String value){
        NamespacedKey namespacedKey = getNamespacedKey(key);
        NBTCompound publicBukkitValues;
        if(nbtCompound.hasTag(NbtConstant.PUBLIC_BUKKIT_VALUES)){
            publicBukkitValues = nbtCompound.getCompound(NbtConstant.PUBLIC_BUKKIT_VALUES);
        }else {
            publicBukkitValues = nbtCompound.addCompound(NbtConstant.PUBLIC_BUKKIT_VALUES);
        }
        publicBukkitValues.setString(namespacedKey.toString(),value);
    }


    public static boolean hasKey(final @NotNull NBTCompound nbtCompound, final String key) {
        NamespacedKey namespacedKey = getNamespacedKey(key);
        //Pre NBT-API PR
        if (Boolean.TRUE.equals(nbtCompound.hasTag(NbtConstant.PUBLIC_BUKKIT_VALUES))) {
            NBTCompound publicBukkitValues = nbtCompound.getCompound(NbtConstant.PUBLIC_BUKKIT_VALUES);
            if (Boolean.TRUE.equals(publicBukkitValues.hasTag(namespacedKey.toString())))
                return true;
        }

        //NBT API PR
        if (Boolean.TRUE.equals(nbtCompound.hasTag(namespacedKey.toString())))
            return true;

        //NBT COMPAT
        if (Boolean.TRUE.equals(nbtCompound.hasTag(namespacedKey.getNamespace()))) {
            NBTCompound emfCompound = nbtCompound.getCompound(namespacedKey.getNamespace());
            return Boolean.TRUE.equals(emfCompound.hasTag(namespacedKey.getKey()));
        }

        return false;
    }
    
    public static @Nullable String getString(final @NotNull NBTCompound nbtCompound, final String key) {
        NamespacedKey namespacedKey = getNamespacedKey(key);
        if (Boolean.TRUE.equals(nbtCompound.hasTag(NbtConstant.PUBLIC_BUKKIT_VALUES))) {
            NBTCompound publicBukkitValues = nbtCompound.getCompound(NbtConstant.PUBLIC_BUKKIT_VALUES);
            if (Boolean.TRUE.equals(publicBukkitValues.hasTag(namespacedKey.toString()))) {
                return publicBukkitValues.getString(namespacedKey.toString());
            }
        }

        //NBT API PR
        if (Boolean.TRUE.equals(nbtCompound.hasTag(namespacedKey.toString()))) {
            return nbtCompound.getString(namespacedKey.toString());
        }

        //NBT COMPAT
        if (Boolean.TRUE.equals(nbtCompound.hasTag(namespacedKey.getNamespace()))) {
            NBTCompound emfCompound = nbtCompound.getCompound(namespacedKey.getNamespace());
            if (Boolean.TRUE.equals(emfCompound.hasTag(namespacedKey.getKey()))) {
                return emfCompound.getString(namespacedKey.getKey());
            }
        }

        return null;
    }

    public static @Nullable Float getFloat(final @NotNull NBTCompound nbtCompound, final String key) {
        NamespacedKey namespacedKey = getNamespacedKey(key);
        if (Boolean.TRUE.equals(nbtCompound.hasTag(NbtConstant.PUBLIC_BUKKIT_VALUES))) {
            NBTCompound publicBukkitValues = nbtCompound.getCompound(NbtConstant.PUBLIC_BUKKIT_VALUES);
            if (Boolean.TRUE.equals(publicBukkitValues.hasTag(namespacedKey.toString())))
                return publicBukkitValues.getFloat(namespacedKey.toString());
        }

        //NBT API PR
        if (Boolean.TRUE.equals(nbtCompound.hasTag(namespacedKey.toString())))
            return nbtCompound.getFloat(namespacedKey.toString());

        //NBT COMPAT
        if (Boolean.TRUE.equals(nbtCompound.hasTag(NbtConstant.BF_COMPOUND))) {
            NBTCompound emfCompound = nbtCompound.getCompound(NbtConstant.BF_COMPOUND);
            if (Boolean.TRUE.equals(emfCompound.hasTag(key)))
                return emfCompound.getFloat(key);
        }

        return null;
    }

    public static @Nullable Integer getInteger(final @NotNull NBTCompound nbtCompound, final String key) {
        NamespacedKey namespacedKey = getNamespacedKey(key);
        if (Boolean.TRUE.equals(nbtCompound.hasTag(NbtConstant.PUBLIC_BUKKIT_VALUES))) {
            NBTCompound publicBukkitValues = nbtCompound.getCompound(NbtConstant.PUBLIC_BUKKIT_VALUES);
            if (Boolean.TRUE.equals(publicBukkitValues.hasTag(namespacedKey.toString())))
                return publicBukkitValues.getInteger(namespacedKey.toString());
        }

        //NBT API PR
        if (Boolean.TRUE.equals(nbtCompound.hasTag(namespacedKey.toString())))
            return nbtCompound.getInteger(namespacedKey.toString());

        //NBT COMPAT
        if (Boolean.TRUE.equals(nbtCompound.hasTag(NbtConstant.BF_COMPOUND))) {
            NBTCompound emfCompound = nbtCompound.getCompound(NbtConstant.BF_COMPOUND);
            if (Boolean.TRUE.equals(emfCompound.hasTag(key)))
                return emfCompound.getInteger(key);
        }

        return null;
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
