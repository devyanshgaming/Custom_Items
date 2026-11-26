package com.orangemc.items;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerResurrectEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class TotemListener implements Listener {
    private final JavaPlugin plugin;
    private final ItemManager itemManager;

    public TotemListener(JavaPlugin plugin, ItemManager itemManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerResurrect(PlayerResurrectEvent event) {
        Player player = event.getEntity();
        EquipmentSlot hand = event.getHand();
        ItemStack usedItem = hand == EquipmentSlot.OFF_HAND
                ? player.getInventory().getItemInOffHand()
                : player.getInventory().getItemInMainHand();

        if (!itemManager.isCustomItem(usedItem, ItemManager.TRIPLE_TOTEM_ID)) {
            return;
        }

        int before = itemManager.getRemainingUses(usedItem);
        if (before <= 0) {
            return;
        }

        // Vanilla may consume the held totem as part of the resurrection process.
        // Restore our updated copy on the next tick so one activation only removes one custom use.
        ItemStack updatedItem = usedItem.clone();
        itemManager.consumeTotemUse(updatedItem);
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            if (!player.isOnline()) {
                return;
            }
            if (hand == EquipmentSlot.OFF_HAND) {
                player.getInventory().setItemInOffHand(updatedItem);
            } else {
                player.getInventory().setItemInMainHand(updatedItem);
            }
        });

        if (before == 1) {
            player.sendMessage(ChatColor.RED + "Your Triple Totem has used its final life and vanished!");
        } else {
            player.sendMessage(ChatColor.GOLD + "Triple Totem activated! "
                    + ChatColor.YELLOW + (before - 1) + " use(s) remaining.");
        }
    }
}
