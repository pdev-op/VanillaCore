package com.pdev.vanillacore.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MessageEvent extends Event implements Cancellable {
    public static HandlerList handlers;
    private Player player;
    private Player target;
    private String message;
    private boolean canceled;
    private String cancelMessage;

    public MessageEvent(Player player, Player target, String message, boolean cancelled, String cancelMessage) {
        this.player = player;
        this.target = target;
        this.message = message;
        this.canceled = cancelled;
        this.cancelMessage = cancelMessage;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.canceled = cancel;        
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getTarget() {
        return target;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCancelMessage() {
        return cancelMessage;
    }

    static {
        MessageEvent.handlers = new HandlerList();
    }
}
