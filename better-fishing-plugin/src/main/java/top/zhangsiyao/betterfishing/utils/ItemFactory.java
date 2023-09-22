package top.zhangsiyao.betterfishing.utils;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.item.AbstractItem;
import top.zhangsiyao.betterfishing.item.FishItem;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;

public class ItemFactory {

    AbstractItem fishItem;

    private File file;
    private ItemStack product;

    @Getter
    private int chosenRandomIndex = -1;
    private boolean itemRandom;
    private boolean rawMaterial;
    private String displayName;

    public ItemFactory(AbstractItem fishItem, File file) {
        this.fishItem = fishItem;
        this.file=file;
        this.product=getType(null);
    }


    public ItemStack createItem(OfflinePlayer player, int randomIndex){
        if (rawMaterial) return this.product;
        if (itemRandom && player != null) {
            if (randomIndex == -1) this.product = getType(player);
            else this.product = setType(randomIndex);
        }

//      applyModelData();
        applyDamage();
        applyDisplayName();
//        applyDyeColour();
        applyGlow();
        applyLore();
        applyPotionMeta();

        applyFlags();

        return product;
    }

    public ItemStack setType(int randomIndex) {
//        ItemStack oneHeadDB = checkRandomHeadDB(randomIndex);
//        if (oneHeadDB != null) return oneHeadDB;

        ItemStack oneMaterial = checkRandomMaterial(randomIndex);
        if (oneMaterial != null) return oneMaterial;

//        ItemStack oneHead64 = checkRandomHead64(randomIndex);
//        if (oneHead64 != null) return oneHead64;
//
//        ItemStack oneHeadUUID = checkRandomHeadUUID(randomIndex);
//        if (oneHeadUUID != null) return oneHeadUUID;

        return new ItemStack(Material.COD);
    }

    public ItemStack getType(OfflinePlayer player) {

        //头颅数据库
//        ItemStack oneHeadDB = checkRandomHeadDB(-1);
//        if (oneHeadDB != null) return oneHeadDB;

        ItemStack oneMaterial = checkRandomMaterial(-1);
        if (oneMaterial != null) return oneMaterial;
        // 随机head64
//        ItemStack oneHead64 = checkRandomHead64(-1);
//        if (oneHead64 != null) return oneHead64;
//
//        ItemStack oneHeadUUID = checkRandomHeadUUID(-1);
//        if (oneHeadUUID != null) return oneHeadUUID;
//
        ItemStack oneOwnHead = checkOwnHead(player);
        if (oneOwnHead != null) return oneOwnHead;
//
//        ItemStack headDB = checkHeadDB();
//        if (headDB != null) return headDB;
//
        ItemStack material = checkMaterial();
        if (material != null) return material;
//
//        ItemStack head64 = checkHead64();
//        if (head64 != null) return head64;
//
//        ItemStack headUUID = checkHeadUUID();
//        if (headUUID != null) return headUUID;

        ItemStack rawMaterial = checkRawMaterial();
        if (rawMaterial != null) return rawMaterial;

        // The fish has no item type specified
        return new ItemStack(Material.COD);

    }


    private ItemStack checkRandomMaterial(int randomIndex) {

        List<String> lValues = fishItem.getItemProperties().getMaterials();
        if (!lValues.isEmpty()) {

            final Random rand = BetterFishing.getInstance().getRandom();

            if (randomIndex == -1 || randomIndex + 1 > lValues.size()) {
                randomIndex = rand.nextInt(lValues.size());
                this.chosenRandomIndex = randomIndex;
            }

            Material m = Material.getMaterial(lValues.get(randomIndex).toUpperCase());
            itemRandom = true;

            if (m == null) {
                BetterFishing.logger.log(Level.SEVERE, file.getName() + "'s has an incorrect material name in its materials list.");
                for (String material : lValues) {
                    if (Material.getMaterial(material.toUpperCase()) != null) {
                        return new ItemStack(Objects.requireNonNull(Material.getMaterial(material.toUpperCase())));
                    }
                }

                return new ItemStack(Material.COD);
            } else {
                return new ItemStack(m);
            }
        }

        return null;
    }

    private ItemStack checkOwnHead(OfflinePlayer player) {
        boolean ownHead = fishItem.getItemProperties().getOwnHead();
        // Causes this to run each turn the create() is called.
        itemRandom = ownHead;

        if (ownHead && player != null) {
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            if (meta != null) {
                meta.setOwningPlayer(player);
            } else {
                return new ItemStack(Material.COD);
            }

            skull.setItemMeta(meta);
            return skull;
        } else return null;
    }

    private ItemStack checkMaterial(){
        return checkItem(fishItem.getItemProperties().getMaterial());
    }

    private ItemStack checkRawMaterial() {
        String materialID = fishItem.getItemProperties().getRawMaterial();
        if(materialID != null) {
            rawMaterial = true;
        }

        return checkItem(materialID);
    }

    private ItemStack checkItem(final String materialID) {
        if (materialID == null) {
            return null;
        }

        rawMaterial = true;

        try {
            return getItem(materialID);
        } catch (Exception e) {
            BetterFishing.logger.severe(() -> String.format("%s 有一个错误的材质名称: %s",
                    file.getName(),
                    materialID));
            return new ItemStack(Material.COD);
        }
    }

    public @NotNull ItemStack getItem(final @NotNull String materialString) throws Exception{
        Material material = Material.matchMaterial(materialString);
        if (material == null) {
            BetterFishing.logger.severe(() -> String.format("%s 有一个错误的材质名称: %s",
                    file.getName(),
                    materialString));
            return new ItemStack(Material.COD);
        }

        return new ItemStack(material);
    }

    private void applyLore() {
        List<String> loreConfig = fishItem.getLore();
        if (loreConfig.isEmpty()) return;

        ItemMeta meta = product.getItemMeta();
        if (meta == null) return;

        meta.setLore(loreConfig);
        product.setItemMeta(meta);
    }

    public void applyDamage() {

        ItemMeta meta = product.getItemMeta();
        if (meta instanceof Damageable) {
            Damageable nonDamaged = (Damageable) meta;

            int predefinedDamage = fishItem.getDurability();
            if (predefinedDamage >= 0 && predefinedDamage <= 100) {
                nonDamaged.setDamage((int) (predefinedDamage / 100.0 * product.getType().getMaxDurability()));
            } else {
                if (BetterFishing.mainConfig.doingRandomDurability()) {
                    int max = product.getType().getMaxDurability();
                    nonDamaged.setDamage(BetterFishing.getInstance().getRandom().nextInt() * (max + 1));
                }
            }

            product.setItemMeta(nonDamaged);
        }
    }

    private void applyDisplayName() {
        String displayName = fishItem.getDisplayName();

        if (displayName == null && this.displayName != null) displayName = this.displayName;

        if (displayName != null) {
            ItemMeta meta = product.getItemMeta();

            if (meta != null) {
                meta.setDisplayName(FishUtils.translateHexColorCodes(displayName));
            }

            product.setItemMeta(meta);
        }
    }

    private void applyGlow() {
        if (fishItem.getGlowing()) {
            this.product.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        }
    }

    private void applyPotionMeta() {
        String potionSettings = fishItem.getItemProperties().getPotion();

        if (potionSettings == null) return;
        if (!(product.getItemMeta() instanceof PotionMeta)) return;

        String[] split = potionSettings.split(":");
        if (split.length != 3) {
            BetterFishing.logger.log(Level.SEVERE, file.getName() + ".item.potion: is formatted incorrectly in the fish.yml file. Use \"potion:duration:amplifier\".");
        }

        PotionMeta meta = ((PotionMeta) product.getItemMeta());
        try {
            meta.addCustomEffect(new PotionEffect(Objects.requireNonNull(PotionEffectType.getByName(split[0])), Integer.parseInt(split[1]) * 20, Integer.parseInt(split[2]) - 1, false), true);
        } catch (NumberFormatException exception) {
            BetterFishing.logger.log(Level.SEVERE, file.getName() + ".item.potion: is formatted incorrectly in the fish.yml file. Use \"potion:duration:amplifier\", where duration & amplifier are integer values.");
        } catch (NullPointerException exception) {
            BetterFishing.logger.log(Level.SEVERE, file.getName() + ".item.potion: " + split[0] + " is not a valid potion name. A list can be found here: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionEffectType.html");
        }

        product.setItemMeta(meta);

    }

    private void applyFlags() {
        ItemMeta meta = product.getItemMeta();

        if (meta != null) {
//            if (itemDyeCheck) meta.addItemFlags(ItemFlag.HIDE_DYE);
            if (fishItem.getGlowing()) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            this.product.setItemMeta(meta);
        }
    }


//    public void applyDyeColour() {
//        String dyeColour = fishItem
//
//        if (dyeColour != null) {
//            try {
//                LeatherArmorMeta meta = (LeatherArmorMeta) product.getItemMeta();
//
//                Color colour = Color.decode(dyeColour);
//
//                if (meta != null) {
//                    meta.setColor(org.bukkit.Color.fromRGB(colour.getRed(), colour.getGreen(), colour.getBlue()));
//                }
//
//                product.setItemMeta(meta);
//            } catch (ClassCastException exception) {
//                BetterFishing.logger.log(Level.SEVERE, "Could not fetch hex value: " + dyeColour + " from config location + " + configLocation + ". Item is likely not a leather material.");
//            }
//        }
//    }


}
