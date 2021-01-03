package io.github.zellfrey.forgevotifier.api.reward;

import io.github.zellfrey.forgevotifier.api.RewardException;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class Reward {

    boolean broadcastMessage, parseAsTellraw;
    String voteMessage, command;

    public static String replace(String input, ICommandSender entity, String service, String voteCount) {
        return input.replace("@PLAYER@", entity.getName()).replace("@SERVICE@", service).replace("@VOTECOUNT@", voteCount);
    }

    public void sendMessage(MinecraftServer server, EntityPlayerMP player) throws RewardException {

        if(this.parseAsTellraw) {
            try {
                ITextComponent textComponent = ITextComponent.Serializer.jsonToComponent(this.voteMessage);
                if(textComponent == null) {
                    throw new CommandException("parsed component from message was null: " + this.voteMessage);
                }
                if(this.broadcastMessage) {
                    for(EntityPlayerMP playerMP : server.getPlayerList().getPlayers()) {
                        playerMP.sendMessage(TextComponentUtils.processComponent(server, textComponent, playerMP));
                    }
                }
                else {
                    player.sendMessage(TextComponentUtils.processComponent(server, textComponent, player));
                }
            }
            catch (CommandException e) {
                throw new RewardException("error parsing chat for reward!", e);
            }
        }
        else {
            String[] messages = this.voteMessage.split("\n");
            for(String messageString : messages) {
                ITextComponent textComponent = new TextComponentString(messageString);
                if(this.broadcastMessage) {
                    server.getPlayerList().sendMessage(textComponent);
                }
                else {
                    player.sendMessage(textComponent);
                }
            }
        }
    }

    @Nullable
    public void executeCommand(MinecraftServer server, EntityPlayer player, String service, String voteCount){
        server.commandManager.executeCommand(server, replace(this.command, player, service, voteCount));
    }

    public abstract String getType();

    public abstract void activate(MinecraftServer server, EntityPlayer player, String timestamp, String service, String address) throws RewardException;
}
