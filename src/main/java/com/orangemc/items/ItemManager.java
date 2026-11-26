package com.orangemc.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public final class ItemManager {
    public static final String TRIPLE_TOTEM_ID = "triple_totem";
    public static final String GOLDEN_APPLE_PLUS_ID = "golden_apple_plus";
    private static final int DEFAULT_TOTEM_USES = 3;

    private final NamespacedKey itemIdKey;
    private final NamespacedKey usesKey;

    public ItemManager(OrangeCustomItems plugin) {
        this.itemIdKey = new NamespacedKey(plugin, "custom_item_id");
        this.usesKey = new NamespacedKey(plugin, "remaining_uses");
    }

    public ItemStack createTripleTotem() {
        return createTripleTotem(DEFAULT_TOTEM_USES);
    }

    public ItemStack createTripleTotem(int remainingUses) {
        ItemStack item = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Triple Totem");
        meta.setLore(List.of(
                ChatColor.GRAY + "A totem with three lives.",
                ChatColor.YELLOW + "Remaining uses: " + remainingUses + "/3"
        ));
        meta.getPersistentDataContainer().set(itemIdKey, PersistentDataType.STRING, TRIPLE_TOTEM_ID);
        meta.getPersistentDataContainer().set(usesKey, PersistentDataType.INTEGER, remainingUses);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createGoldenApplePlus() {
        ItemStack item = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Golden Apple+");
        meta.setLore(List.of(
                ChatColor.GRAY + "A golden apple with enhanced protection.",
                ChatColor.YELLOW + "+4 absorption hearts"
        ));
        meta.getPersistentDataContainer().set(itemIdKey, PersistentDataType.STRING, GOLDEN_APPLE_PLUS_ID);
        item.setItemMeta(meta);
        return item;
    }

    public boolean isCustomItem(ItemStack item, String expectedId) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) {
            return false;
        }
        String id = item.getItemMeta().getPersistentDataContainer()
                .get(itemIdKey, PersistentDataType.STRING);
        return expectedId.equals(id);
    }

    public int getRemainingUses(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return 0;
        }
        Integer uses = item.getItemMeta().getPersistentDataContainer()
                .get(usesKey, PersistentDataType.INTEGER);
        return uses == null ? 0 : uses;
    }

    public void consumeTotemUse(ItemStack item) {
        int remaining = getRemainingUses(item);
        if (remaining <= 1) {
            item.setType(Material.AIR);
            return;
        }

        ItemMeta meta = item.getItemMeta();
        int updatedUses = remaining - 1;
        meta.getPersistentDataContainer().set(usesKey, PersistentDataType.INTEGER, updatedUses);
        meta.setLore(List.of(
                ChatColor.GRAY + "A totem with three lives.",
                ChatColor.YELLOW + "Remaining uses: " + updatedUses + "/3"
        ));
        item.setItemMeta(meta);
    }
}
