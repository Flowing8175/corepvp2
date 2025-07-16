package com.hagg.corepvp.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;

public class ItemFactory {

    public static ItemStack createStarOfRecovery() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§l회복의 별");
        meta.getPersistentDataContainer().set(new NamespacedKey("corepvp", "enchantment_glint_override"), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createBeaconOfLife() {
        ItemStack item = new ItemStack(Material.BEACON);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§l생명의 신호기");
        meta.getPersistentDataContainer().set(new NamespacedKey("corepvp", "enchantment_glint_override"), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }
}
