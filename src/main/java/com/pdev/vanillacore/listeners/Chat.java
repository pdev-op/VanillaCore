package com.pdev.vanillacore.listeners;

import com.pdev.vanillacore.Main;
import com.pdev.vanillacore.api.events.MessageEvent;
import com.pdev.vanillacore.utils.ChatUtils;
import com.pdev.vanillacore.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Chat implements Listener {
    private Main plugin;

    public Chat(Main plugin) {
        this.plugin = plugin;
    }

    // Join Quit things
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // Player
        Player player = e.getPlayer();

        // Add to PlayerManager
        plugin.getPlayerManager().addPlayer(player);

        // Join Broadcast
        e.setJoinMessage(StringUtils.colorize(plugin.getConfig().getString("join-format").replaceAll("%player%", player.getName())));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        // Player
        Player player = e.getPlayer();

        // Remove player
        plugin.getPlayerManager().removePlayer(player);

        // Quit Message
        e.setQuitMessage(StringUtils.colorize(plugin.getConfig().getString("quit-format").replaceAll("%player%", player.getName())));
    }

    // Global Chat
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        // LuckPerms User
        Player player = e.getPlayer();
        LuckPerms lp = plugin.getLuckPerms();
        User user = lp.getUserManager().getUser(player.getUniqueId());
        CachedMetaData data = user.getCachedData().getMetaData();

        String mprefix = plugin.getConfig().getString("prefix");

        // Has Moved
        if (!plugin.getPlayerManager().getPlayer(player).hasMovedSinceLogin()) {
            String message = mprefix + "You must move before sending messages in chat!";

            e.setCancelled(true);
            player.sendMessage(StringUtils.colorize(message));
        }

        // Anti Advertising
        if (ChatUtils.shouldRemoveAd(player, e.getMessage())) {
            String message = mprefix + "You cannot advertise or send links in chat!";

            e.setCancelled(true);
            player.sendMessage(StringUtils.colorize(message));
        }

        // Anti Ascii
        if (ChatUtils.shouldReplaceAscii(player, e.getMessage())) {
            String message = mprefix + "You cannot use ascii characters in chat!";

            e.setCancelled(true);
            player.sendMessage(StringUtils.colorize(message));
        }

        // Format Strings
        // NOTE
        // - All of these values need to be adjusted based on how the server handles
        // prefixes & chat colors
        String group = user.getPrimaryGroup();
        String levels = group.equalsIgnoreCase("default") || player.hasPermission("vanillacore.staff")
                || data.getMetaValue("level") == null ? ""
                        : data.getMetaValue("level") + " ";
        String prefix = data.getPrefix();
        String chatColor = data.getMetaValue("chatcolor");
        String caretColor = data.getMetaValue("caretcolor");
        String nameColor = data.getMetaValue("namecolor");

        // Mentions
        if (e.getMessage().contains("@")) {
            String[] parts = e.getMessage().split(" ");

            for (String p : parts) {
                if (p.contains("@")) {
                    Player mentioned = Bukkit.getPlayer(p.substring(1));

                    if (mentioned != null) {
                        mentioned.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                                StringUtils.colorize("&e" + player.getName() + " &fmentioned you in chat!")));
                        mentioned.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS,
                                3.0f, 2.0f);
                    }
                }
            }
        }

        // Chat Format
        String message = player.hasPermission("vanillacore.chat.format")
                ? StringUtils.colorize(chatColor + e.getMessage())
                : StringUtils.colorize(chatColor) + e.getMessage();
        String format = StringUtils.colorize(
                levels + prefix + " " + nameColor + player.getDisplayName() + caretColor + " » ") + message;

        e.setFormat(format);
    }

    // Message sending & social spy
    @EventHandler
    public void onPrivateMessage(MessageEvent e) {
        String senderFormat = plugin.getConfig().getString("message-sender")
                .replaceAll("%player%", e.getTarget().getDisplayName());
        String targetFormat = plugin.getConfig().getString("message-target")
                .replaceAll("%player%", e.getPlayer().getDisplayName());

        e.getPlayer().sendMessage(StringUtils.colorize(senderFormat) + e.getMessage());
        e.getTarget().sendMessage(StringUtils.colorize(targetFormat) + e.getMessage());

        plugin.getPlayerManager().updateLastMessage(e.getPlayer(), e.getTarget(), e.getMessage());

        for (Player player : plugin.getPlayerManager().getSocialSpies()) {
            String spyFormat = plugin.getConfig().getString("message-spy")
                .replaceAll("%player%", e.getPlayer().getDisplayName())
                .replaceAll("%target%", e.getTarget().getDisplayName());

            if (player != e.getPlayer() && player != e.getTarget()
                    && !e.getPlayer().hasPermission("vanillacore.socialspy.bypass")
                    && !e.getTarget().hasPermission("vanillacore.socialspy.bypass")
                ) {
                player.sendMessage(StringUtils.colorize(spyFormat) + e.getMessage());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        // Command Spy
        if (!player.hasPermission("vanillacore.bypass.commandspy")) {
            String prefix = plugin.getConfig().getString("spy-prefix");

            for (Player spy : plugin.getPlayerManager().getCommandSpies()) {
                spy.sendMessage(StringUtils.colorize(prefix + player.getName() + "&6: &e" + e.getMessage()
                        + (e.isCancelled() ? " &8(&ccancelled&8)" : "")));
            }
        }
    }
}
