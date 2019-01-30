package com.github.upcraftlp.votifier.api;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class VoteReceivedEvent extends PlayerEvent {

    private final String service;
    private final String address;
    private final String timestamp;

    public VoteReceivedEvent(EntityPlayerMP player, String service, String address, String timestamp) {
        super(player);
        this.service = service;
        this.address = address;
        this.timestamp = timestamp;
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
