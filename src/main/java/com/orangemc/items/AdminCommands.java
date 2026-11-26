package com.orangemc.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class AdminCommands implements CommandExecutor, TabCompleter {
    private final JavaPlugin plugin;
    private final ItemManager itemManager;

    public AdminCommands(JavaPlugin plugin, ItemManager itemManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("orangecustomitems.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender, label);
            return true;
        }

        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "give" -> giveItem(sender, args);
            case "list" -> {
                sender.sendMessage(ChatColor.GOLD + "OrangeCustomItems:");
                sender.sendMessage(ChatColor.YELLOW + "- triple_totem");
                sender.sendMessage(ChatColor.YELLOW + "- golden_apple_plus");
            }
            case "reload" -> {
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "OrangeCustomItems config reloaded.");
            }
            default -> sendHelp(sender, label);
        }
        return true;
    }

    private void giveItem(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /oci give <player> <triple_totem|golden_apple_plus> [amount]");
            return;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "That player is not online.");
            return;
        }

        int amount = 1;
        if (args.length >= 4) {
            try {
                amount = Integer.parseInt(args[3]);
            } catch (NumberFormatException exception) {
                sender.sendMessage(ChatColor.RED + "Amount must be a number.");
                return;
            }
        }
        if (amount < 1 || amount > 64) {
            sender.sendMessage(ChatColor.RED + "Amount must be between 1 and 64.");
            return;
        }

        String itemName = args[2].toLowerCase(Locale.ROOT);
        ItemStack item;
        switch (itemName) {
            case "triple_totem" -> {
                if (amount != 1) {
                    sender.sendMessage(ChatColor.RED + "Triple Totems are unique items. Amount is fixed at 1.");
                    return;
                }
                item = itemManager.createTripleTotem();
            }
            case "golden_apple_plus" -> {
                item = itemManager.createGoldenApplePlus();
                item.setAmount(amount);
            }
            default -> {
                sender.sendMessage(ChatColor.RED + "Unknown item. Use triple_totem or golden_apple_plus.");
                return;
            }
        }

        target.getInventory().addItem(item).values().forEach(leftover ->
                target.getWorld().dropItemNaturally(target.getLocation(), leftover));
        sender.sendMessage(ChatColor.GREEN + "Gave " + itemName + " to " + target.getName() + ".");
        if (!sender.equals(target)) {
            target.sendMessage(ChatColor.GOLD + "You received a custom OrangeMC item!");
        }
    }

    private void sendHelp(CommandSender sender, String label) {
        sender.sendMessage(ChatColor.GOLD + "OrangeCustomItems commands:");
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " give <player> <item> [amount]");
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " list");
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " reload");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return startsWith(List.of("give", "list", "reload"), args[0]);
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            return startsWith(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList(), args[1]);
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            return startsWith(List.of("triple_totem", "golden_apple_plus"), args[2]);
        }
        return List.of();
    }

    private List<String> startsWith(List<String> options, String input) {
        String lower = input.toLowerCase(Locale.ROOT);
        List<String> matches = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase(Locale.ROOT).startsWith(lower)) {
                matches.add(option);
            }
        }
        return matches;
    }
}
