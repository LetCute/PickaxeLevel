package net.letcute.pickaxeLevel.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.Collections;

import net.letcute.pickaxeLevel.PickaxeLevel;
import net.letcute.pickaxeLevel.config.Config;

public class ItemPickaxeLevel {

    private static Config config = PickaxeLevel.getInstance().getData(); 

    private static final NamespacedKey NAME_KEY = new NamespacedKey(PickaxeLevel.getInstance(), "name");

    public static ItemStack createPickaxe(Material type, String name, String customname, String description) {
        ItemStack itemStack = new ItemStack(type);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName(customname);
            itemMeta.setLore(Collections.singletonList(description));
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            container.set(NAME_KEY, PersistentDataType.STRING, name);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public static boolean isPickaxe(ItemStack itemStack, String name) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            return container.has(NAME_KEY, PersistentDataType.STRING)
                    && name.equals(container.get(NAME_KEY, PersistentDataType.STRING));

        }
        return false;
    }

    public static ItemStack uplevel(ItemStack itemStack, int level) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            int efficiencyLevel = level / config.getRatioEfficiency();
            int fortuneLevel = level / config.getRatioFortuneLevel();
            int unbreakingLevel = level / config.getRatioUnbreaking();

            if (efficiencyLevel > 0) {
                itemMeta.addEnchant(Enchantment.DIG_SPEED, efficiencyLevel, true);
            }

            if (fortuneLevel > 0) {
                itemMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, fortuneLevel, true);
            }

            if (unbreakingLevel > 0) {
                itemMeta.addEnchant(Enchantment.DURABILITY, unbreakingLevel, true);
            }

            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

}
