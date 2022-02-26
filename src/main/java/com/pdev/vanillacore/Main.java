package com.pdev.vanillacore;

import java.io.File;

import com.pdev.vanillacore.listeners.Chat;
import com.pdev.vanillacore.managers.CommandManager;
import com.pdev.vanillacore.managers.PlayerManager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.luckperms.api.LuckPerms;

public class Main extends JavaPlugin {
    private static Main instance;

    private PlayerManager playerManager;
    private CommandManager commandManager;
    private LuckPerms luckPerms;

    public Main() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Timings
        long start = System.currentTimeMillis();

        // Logging
        Bukkit.getConsoleSender().sendMessage("§eCYTCore §6v" + getDescription().getVersion() + " §7by pdev enabling...");

        // Config
        File file = new File(getDataFolder(), "config.yml");

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }

        // Managers
        playerManager = new PlayerManager(this);
        commandManager = new CommandManager(this);

        // LuckPerms
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
            Bukkit.getConsoleSender().sendMessage("LuckPerms successfully hooked into");
        } else {
            onDisable();
            Bukkit.getConsoleSender().sendMessage("Error hooking into LuckPerms");
        }

        // Listeners
        getServer().getPluginManager().registerEvents(new Chat(this), this);

        // Log load time
        Bukkit.getConsoleSender().sendMessage("§aEnabled §7in " + (System.currentTimeMillis() - start) + "ms");
    }

    @Override
    public void onDisable() {
        // Logging
        Bukkit.getConsoleSender().sendMessage("§eCYTCore §6v" + getDescription().getVersion() + " §7by pdev disabled.");
    }

    public Main getInstance() {
        return instance;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }
}
