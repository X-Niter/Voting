package io.github.zellfrey.forgevotifier.reward;

import io.github.zellfrey.forgevotifier.api.RewardException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public abstract class Reward {

    public static String replace(String input, ICommandSender entity, String service, String voteCount) {
        return input.replace("@PLAYER@", entity.getName()).replace("@SERVICE@", service).replace("@VOTECOUNT@", voteCount);
    }

    public abstract String getType();

    public abstract void activate(MinecraftServer server, EntityPlayer player, String timestamp, String service, String address) throws RewardException;
}
