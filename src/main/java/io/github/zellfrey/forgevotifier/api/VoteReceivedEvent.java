package io.github.zellfrey.forgevotifier.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Original author: UpcraftLP
 */
public class VoteReceivedEvent extends PlayerEvent {

    private final String service;
    private final String address;
    private final String timestamp;

    public VoteReceivedEvent(ServerPlayerEntity player, String service, String address, String timestamp) {
        super(player);
        this.service = service;
        this.address = address;
        this.timestamp = timestamp;
    }

    public String getPlayerStringify() {
        return getPlayer().toString();
    }

    public PlayerEntity entityPlayer() {
        return getPlayer();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getServiceDescriptor() {
        return service;
    }

    public String getRemoteAddress() {
        return address;
    }
}
