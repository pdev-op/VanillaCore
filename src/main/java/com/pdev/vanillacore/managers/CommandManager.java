package com.pdev.vanillacore.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pdev.vanillacore.*;
import com.pdev.vanillacore.api.VCommand;
import com.pdev.vanillacore.commands.chat.Broadcast;
import com.pdev.vanillacore.commands.chat.CommandSpy;
import com.pdev.vanillacore.commands.chat.Message;
import com.pdev.vanillacore.commands.chat.Reply;
import com.pdev.vanillacore.commands.chat.SocialSpy;
import com.pdev.vanillacore.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor, TabCompleter {
    private Main plugin;
    private ArrayList<VCommand> commands;

    public CommandManager(Main plugin) {
        // Plugin
        this.plugin = plugin;

        // Commands List
        commands = new ArrayList<VCommand>();

        // Chat
        commands.add(new Broadcast(plugin));
        commands.add(new Message(plugin));
        commands.add(new Reply(plugin));
        commands.add(new SocialSpy(plugin));
        commands.add(new CommandSpy(plugin));

        // Register the commands
        registerCommands();
    }

    public void registerCommands() {
        for (VCommand c : commands) {
            for (String s : c.getAliases()) {
                if (plugin.getCommand(s) != null) {
                    plugin.getCommand(s).setExecutor(this);
                    plugin.getCommand(s).setTabCompleter(this);
                }
            }
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> suggestions = new ArrayList<String>();

        for (VCommand c : commands) {
            for (String al : c.getAliases()) {
                if (al.equalsIgnoreCase(alias) && c.hasPermission(sender)) {
                    HashMap<String, Integer> suggestionsMap = c.getSuggestions(sender);

                    for (String s : suggestionsMap.keySet()) {
                        if (args.length == suggestionsMap.get(s)) {
                            if (!s.equalsIgnoreCase("players")) {
                                suggestions.add(s);
                            } else {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    suggestions.add(player.getName());
                                }
                            }
                        }
                    }
                }
            }
        }

        return suggestions;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        for (VCommand c : commands) {
            if (c.getAliases().contains(label.toLowerCase())) {

                String prefix = plugin.getConfig().getString("prefix");

                // Check Permissions
                if (!c.hasPermission(sender)) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(StringUtils.colorize(prefix + " &7This command cannot be run from the console."));
                    } else {
                        sender.sendMessage(StringUtils.colorize(prefix + " &7Insufficient permissions."));
                    }

                    return false;
                }

                // Send it
                try {
                    c.execute(sender, args);
                } catch (Exception e) {
                    if (e.getMessage().equalsIgnoreCase("insufficient-permissions")) {
                        sender.sendMessage(StringUtils.colorize(prefix + " &7Insufficient permissions."));
                    } else if (e.getMessage().equalsIgnoreCase("usage")) {
                        sender.sendMessage(
                                StringUtils.colorize(prefix + " &7Incorrect usage (try &e" + c.getUsage() + ")."));
                    } else if (e.getMessage().equalsIgnoreCase("no-player")) {
                        sender.sendMessage(StringUtils.colorize(prefix + " &7Player not found."));
                    } else if (e.getMessage().equalsIgnoreCase("advertising")) {
                        sender.sendMessage(StringUtils.colorize(prefix + "You cannot advertise or send links in private messages!"));
                    } else if (e.getMessage().equalsIgnoreCase("ascii")) {
                        sender.sendMessage(StringUtils.colorize(prefix + "You cannot use ascii characters in private messages!"));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cAn internal error has occured, please contact an admin. We are sorry for the inconvenience!"));

                        e.printStackTrace();
                    }
                }

                return true;
            }
        }

        return false;
    }
}
