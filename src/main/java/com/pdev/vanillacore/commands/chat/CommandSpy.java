package com.pdev.vanillacore.commands.chat;

import java.util.HashMap;
import java.util.List;

import com.pdev.vanillacore.Main;
import com.pdev.vanillacore.utils.StringUtils;
import com.pdev.vanillacore.api.VCommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpy extends VCommand {

    public CommandSpy(Main plugin) {
        super(plugin);

        this.addAlias("commandspy");
        this.addAlias("cs");
        this.addAlias("cspy");
        this.setUsage("/commandspy [list]");
    }

    @Override
    public HashMap<String, Integer> getSuggestions(CommandSender sender) {
        HashMap<String, Integer> suggestions = new HashMap<>();

        suggestions.put("list", 1);

        return suggestions;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("vanillacore.commandspy") && sender instanceof Player;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {
        String prefix = plugin.getConfig().getString("spy-prefix");

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                throw new Exception("console_sender");
            }

            Player player = (Player) sender;
            String onOff = "&coff";

            if (plugin.getPlayerManager().isCommandSpy(player)) {
                plugin.getPlayerManager().removeCommandSpy(player);
            } else {
                plugin.getPlayerManager().addCommandSpy(player);
                onOff = "&aon";
            }

            player.sendMessage(StringUtils.colorize(prefix + "&7Command spy has been turned " + onOff + "&7."));
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                if (!sender.hasPermission("cyt.commandspy.list")) {
                    throw new Exception("insufficient-permissions");
                }

                List<Player> spies = plugin.getPlayerManager().getCommandSpies();

                if (spies.isEmpty()) {
                    sender.sendMessage(StringUtils.colorize(prefix + "&7Currently no command spies."));
                } else {
                    sender.sendMessage(StringUtils.colorize("&e&l&oCommand Spies:"));

                    for (Player player : spies) {
                        sender.sendMessage(StringUtils.colorize("&7- &f" + player.getName()));
                    }
                }
            } else {
                throw new Exception("usage");
            }
        } else {
            throw new Exception("usage");
        }

        return true;
    }
}
