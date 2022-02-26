package com.pdev.vanillacore.commands.chat;

import java.util.HashMap;
import java.util.List;

import com.pdev.vanillacore.Main;
import com.pdev.vanillacore.api.VCommand;
import com.pdev.vanillacore.utils.StringUtils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class SocialSpy extends VCommand {
    public SocialSpy(Main plugin) {
        super(plugin);

        this.addAlias("socialspy");
        this.addAlias("sspy");
        this.addAlias("ss");
        this.setUsage("/socialspy [list]");
    }

    @Override
    public HashMap<String, Integer> getSuggestions(CommandSender sender) {
        HashMap<String, Integer> suggestions = new HashMap<>();

        suggestions.put("list", 1);

        return suggestions;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("vanillacore.socialspy") && sender instanceof Player;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {
        String prefix = plugin.getConfig().getString("spy-prefix");

        if (args.length == 0) {
            Player player = (Player) sender;
            String onOff = "&coff";

            if (plugin.getPlayerManager().isSocialSpy(player)) {
                plugin.getPlayerManager().removeSocialSpy(player);
            } else {
                plugin.getPlayerManager().addSocialSpy(player);
                onOff = "&aon";
            }

            player.sendMessage(StringUtils.colorize(prefix + "Social spy has been turned " + onOff + "&7."));
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                if (!sender.hasPermission("cyt.commandspy.list")) {
                    throw new Exception("insufficient-permissions");
                }

                List<Player> spies = plugin.getPlayerManager().getSocialSpies();

                if (spies.isEmpty()) {
                    sender.sendMessage(StringUtils.colorize(prefix + "Currently no social spies."));
                } else {
                    sender.sendMessage(StringUtils.colorize("&e&l&oSocial Spies:"));

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
