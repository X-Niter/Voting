package io.github.zellfrey.forgevotifier.reward.type;

import io.github.zellfrey.forgevotifier.api.RewardException;
import io.github.zellfrey.forgevotifier.reward.Reward;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentUtils;

public class RewardDefault extends Reward{

    private boolean broadcastMessage, parseAsTellraw;
    private String voteMessage, command;

    public RewardDefault(String command, String voteMessage, boolean broadcast, boolean parseAsTellraw) {
        this.command = command;
        this.voteMessage = voteMessage;
        this.broadcastMessage = broadcast;
        this.parseAsTellraw = parseAsTellraw;
    }

    private void sendMessage(MinecraftServer server, EntityPlayer player, String service, String voteCount) throws RewardException {
        if(this.voteMessage.isEmpty()){
            return;
        }
        String msgToSend = replace(this.voteMessage, player, service, voteCount);
        if(this.parseAsTellraw) {
            try {
                ITextComponent textComponent = ITextComponent.Serializer.jsonToComponent(msgToSend);
                if(textComponent == null) {
                    throw new CommandException("parsed component from message was null: " + msgToSend);
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
            String[] messages = msgToSend.split("\n");
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

    private void executeCommand(MinecraftServer server, EntityPlayer player, String service, String voteCount){
        server.commandManager.executeCommand(server, replace(this.command, player, service, voteCount));
    }

    @Override
    public String getType() {
        return "default";
    }

    @Override
    public void activate(MinecraftServer server, EntityPlayer player, String timestamp, String service, String address) throws RewardException{
        sendMessage(server, player, service, "0");
        executeCommand(server,  player,  service,  "0");
    }
}


