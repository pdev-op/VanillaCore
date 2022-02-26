package com.pdev.vanillacore.commands.chat;

import java.util.HashMap;

import com.pdev.vanillacore.Main;
import com.pdev.vanillacore.api.VCommand;
import com.pdev.vanillacore.api.events.MessageEvent;
import com.pdev.vanillacore.utils.ChatUtils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reply extends VCommand {
    public Reply(Main plugin) {
        super(plugin);

        this.addAlias("reply");
        this.addAlias("r");
        this.setUsage("/reply <message>");
    }

    @Override
    public HashMap<String, Integer> getSuggestions(CommandSender sender) {
        return new HashMap<>();
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender instanceof Player;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {
        if (args.length > 0) {
            Player player = (Player) sender;
            Player target = plugin.getPlayerManager().getPlayer(player) != null ? plugin.getPlayerManager().getPlayer(player).getLastMessaged() : null;

            if (target == null) {
                throw new Exception("no_reply");
            }

            String message = String.join(" ", args);

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
