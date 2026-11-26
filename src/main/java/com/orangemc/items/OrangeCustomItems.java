package com.orangemc.items;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class OrangeCustomItems extends JavaPlugin {
    private ItemManager itemManager;

    @Override
    public void onEnable() {
        this.itemManager = new ItemManager(this);
        getServer().getPluginManager().registerEvents(new TotemListener(this, itemManager), this);
        getServer().getPluginManager().registerEvents(new GoldenAppleListener(this, itemManager), this);

        PluginCommand command = getCommand("oci");
        if (command != null) {
            AdminCommands adminCommands = new AdminCommands(this, itemManager);
            command.setExecutor(adminCommands);
            command.setTabCompleter(adminCommands);
        }

        getLogger().info("OrangeCustomItems enabled!");
    }

    public ItemManager getItemManager() {
        return itemManager;
    }
}
