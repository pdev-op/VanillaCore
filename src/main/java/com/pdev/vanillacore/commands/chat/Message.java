package com.pdev.vanillacore.commands.chat;

import java.util.Arrays;
import java.util.HashMap;

import com.pdev.vanillacore.Main;
import com.pdev.vanillacore.api.VCommand;
import com.pdev.vanillacore.api.events.MessageEvent;
import com.pdev.vanillacore.utils.ChatUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Message extends VCommand {

    public Message(Main plugin) {
        super(plugin);

        this.addAlias("message");
        this.addAlias("m");
        this.addAlias("msg");
        this.addAlias("whisper");
        this.addAlias("w");
        this.setUsage("/message <player> <message>");
    }

    @Override
    public HashMap<String, Integer> getSuggestions(CommandSender sender) {
        HashMap<String, Integer> suggestions = new HashMap<>();

        suggestions.put("players", 1);

        return suggestions;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender instanceof Player;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {
        if (args.length > 1) {
            Player player = (Player) sender;
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                throw new Exception("player_not_found");
            }

            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

            // Anti Advertising
            if (ChatUtils.shouldRemoveAd(player, message)) {
                throw new Exception("advertising");
            }

            // Anti Ascii
            if (ChatUtils.shouldReplaceAscii(player, message)) {
                throw new Exception("ascii");
            }

            MessageEvent me = new MessageEvent(player, target, message, false, null);

            plugin.getServer().getPluginManager().callEvent(me);
        } else {
            throw new Exception("usage");
        }

        return true;
    }
}
