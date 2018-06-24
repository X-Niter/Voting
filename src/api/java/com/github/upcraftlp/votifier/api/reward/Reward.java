package com.github.upcraftlp.votifier.api.reward;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public abstract class Reward {

    public abstract String getType();

    public abstract void activate(MinecraftServer server, EntityPlayer player, long timestamp, String service, String address);

    public static String replace(String input, ICommandSender entity, String service) {
        return input.replace("@PLAYER@", entity.getName()).replace("@SERVICE@", service);
    }
}
