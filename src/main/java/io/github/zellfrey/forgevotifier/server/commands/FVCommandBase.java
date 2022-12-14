package io.github.zellfrey.forgevotifier.server.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.zellfrey.forgevotifier.server.util.TextUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponent;

public abstract class FVCommandBase {

    protected LiteralArgumentBuilder<CommandSource> builder;
    boolean enabled;

    public FVCommandBase(String name, int permissionLevel, boolean enabled) {
        this.builder = Commands.literal(name).requires(source -> source.hasPermissionLevel(permissionLevel));
        this.enabled = enabled;
    }

    public LiteralArgumentBuilder<CommandSource> getBuilder() {
        return builder;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return null;
    }

    public void sendMessage(ServerPlayerEntity player, String translationKey, Object... args) {
        TextUtils.sendChatMessage(player, translationKey, args);
    }

    public void sendMessage(ServerPlayerEntity player, TextComponent textComponent) {
        TextUtils.sendChatMessage(player, textComponent);
    }

    public void sendGlobalMessage(PlayerList players, String translationKey, Object... args) {
        TextUtils.sendGlobalMessage(players, translationKey + args, false);
    }

}