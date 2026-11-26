package com.orangemc.items;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class GoldenAppleListener implements Listener {
    private static final int ABSORPTION_DURATION_TICKS = 20 * 60 * 2;
    private final JavaPlugin plugin;
    private final ItemManager itemManager;

    public GoldenAppleListener(JavaPlugin plugin, ItemManager itemManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGoldenAppleConsume(PlayerItemConsumeEvent event) {
        if (!itemManager.isCustomItem(event.getItem(), ItemManager.GOLDEN_APPLE_PLUS_ID)) {
            return;
        }

        Player player = event.getPlayer();
        // Vanilla consumption still supplies the normal golden-apple food and regeneration effects.
        // On the next tick, replace its normal absorption with Absorption II (4 extra hearts).
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            if (!player.isOnline()) {
                return;
            }
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.ABSORPTION,
                    ABSORPTION_DURATION_TICKS,
                    1,
                    false,
                    true,
                    true
            ));
            player.sendMessage(ChatColor.GOLD + "Golden Apple+ consumed! "
                    + ChatColor.YELLOW + "You gained 4 absorption hearts.");
        });
    }
}
