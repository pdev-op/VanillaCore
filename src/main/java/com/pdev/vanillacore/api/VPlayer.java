package com.pdev.vanillacore.api;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class VPlayer {
    private UUID id;
    private String lastMessage;
    private UUID lastMessagedPlayer;
    private Location loginLocation;
    private boolean hasMoved;

    public VPlayer(UUID id) {
        this.id = id;
        this.hasMoved = false;
    }

    public void setLoginLocation(Location loginLocation) {
        this.loginLocation = loginLocation;
    }

    public boolean hasMovedSinceLogin() {
        if (hasMoved) {
            return true;
        }

        Player player = Bukkit.getPlayer(id);

        if ((player != null && !player.getLocation().equals(loginLocation)) || player.hasPermission("vanillacore.bypass.move")) {
            hasMoved = true;
            return true;
        }

        return false;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(id);
    }

    public void setLastMessaged(UUID lastMessagedPlayer) {
        this.lastMessagedPlayer = lastMessagedPlayer;
    }
    public String getLastMessage() {
        return lastMessage;
    }

    public Player getLastMessaged() {
        return Bukkit.getPlayer(lastMessagedPlayer);
    }
}
