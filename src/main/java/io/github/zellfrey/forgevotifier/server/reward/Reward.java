package io.github.zellfrey.forgevotifier.server.reward;

import io.github.zellfrey.forgevotifier.api.RewardException;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;

public abstract class Reward {

    public static String replace(String input, ICommandSource commandSource, String service, String voteCount) {
        return input.replace("@PLAYER@", commandSource.toString()).replace("@SERVICE@", service).replace("@VOTECOUNT@", voteCount);
    }

    public abstract String getType();

    public abstract void activate(MinecraftServer server, ServerPlayerEntity serverPlayer, String timestamp, String service, String address) throws RewardException;
}
