package com.pdev.vanillacore.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.pdev.vanillacore.*;
import com.pdev.vanillacore.api.VPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerManager {
    private HashMap<UUID, VPlayer> players;
    private Set<UUID> commandSpies;
    private Set<UUID> socialSpies;

    public PlayerManager(Main plugin) {
        players = new HashMap<>();
        socialSpies = new HashSet<>();
        commandSpies = new HashSet<>();
    }

    public void updateLastMessage(Player player, Player target, String message) {
        VPlayer vplayer = getPlayer(player);
        VPlayer vtarget = getPlayer(target);

        vplayer.setLastMessaged(target.getUniqueId());
        vtarget.setLastMessaged(player.getUniqueId());
    }

    public void addPlayer(Player player) {
        VPlayer vplayer = new VPlayer(player.getUniqueId());
        vplayer.setLoginLocation(player.getLocation());

        players.put(player.getUniqueId(), vplayer);
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
    }

    public VPlayer getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public VPlayer getPlayer(Player player) {
        return players.get(player.getUniqueId());
    }

    public void addCommandSpy(Player player) {
        commandSpies.add(player.getUniqueId());
    }

    public void addSocialSpy(Player player) {
        socialSpies.add(player.getUniqueId());
    }

    public void removeCommandSpy(Player player) {
        commandSpies.remove(player.getUniqueId());
    }

    public void removeSocialSpy(Player player) {
        socialSpies.remove(player.getUniqueId());
    }

    public boolean isSocialSpy(Player player) {
        return socialSpies.contains(player.getUniqueId());
    }

    public boolean isCommandSpy(Player player) {
        return commandSpies.contains(player.getUniqueId());
    }

    public List<Player> getSocialSpies() {
        ArrayList<Player> players = new ArrayList<>();

        for (UUID uuid : socialSpies) {
            Player spy = Bukkit.getPlayer(uuid);
            if (spy != null) {
                players.add(spy);
            }
        }

        return players;
    }

    public List<Player> getCommandSpies() {
        ArrayList<Player> players = new ArrayList<>();

        for (UUID uuid : commandSpies) {
            Player spy = Bukkit.getPlayer(uuid);
            if (spy != null) {
                players.add(spy);
            }
        }

        return players;
    }
}
