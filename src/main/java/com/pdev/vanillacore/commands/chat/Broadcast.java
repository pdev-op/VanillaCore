package com.pdev.vanillacore.commands.chat;

import java.util.HashMap;

import com.pdev.vanillacore.Main;
import com.pdev.vanillacore.api.VCommand;
import com.pdev.vanillacore.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Broadcast extends VCommand {
    public Broadcast(Main plugin) {
        super(plugin);

        this.addAlias("broadcast");
        this.addAlias("bc");
        this.setUsage("/broadcast <message>");
    }

    @Override
    public HashMap<String, Integer> getSuggestions(CommandSender sender) {
        return new HashMap<>();
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("vanillacore.broadcast");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {
        if (args.length > 0) {
            
            String message = String.join(" ", args);
            String format = plugin.getConfig().getString("broadcast")
                    .replaceAll("%message%", message);
            
            Bukkit.broadcastMessage(StringUtils.colorize(format));
        } else {
            throw new Exception("usage");
        }

        return true;
    }
}