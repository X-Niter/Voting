package io.github.zellfrey.forgevotifier.server.reward.type;

import io.github.zellfrey.forgevotifier.api.RewardException;
import io.github.zellfrey.forgevotifier.server.reward.Reward;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class RewardDefault extends Reward{

    private boolean broadcastMessage, parseAsTellraw;
    private String voteMessage, command;

    public RewardDefault(String command, String voteMessage, boolean broadcast, boolean parseAsTellraw) {
        this.command = command;
        this.voteMessage = voteMessage;
        this.broadcastMessage = broadcast;
        this.parseAsTellraw = parseAsTellraw;
    }

    private void sendMessage(MinecraftServer server, ServerPlayerEntity player, String service, String voteCount) throws RewardException {
        if(this.voteMessage.isEmpty()){
            return;
        }
        String msgToSend = replace(this.voteMessage, player, service, voteCount);
        if(this.parseAsTellraw) {
            try {
                ITextComponent textComponent = ITextComponent.Serializer.getComponentFromJson(msgToSend);
                if(textComponent == null) {
                    throw new CommandException(new StringTextComponent("parsed component from message was null: " + msgToSend));
                }
                if(this.broadcastMessage) {
                    for(ServerPlayerEntity playerMP : server.getPlayerList().getPlayers()) {
                        playerMP.sendMessage(textComponent, playerMP.getUniqueID());
                    }
                }
                else {
                    player.sendMessage(textComponent, player.getUniqueID());
                }
            }
            catch (CommandException e) {
                throw new RewardException("error parsing chat for reward!", e);
            }
        }
        else {
            String[] messages = msgToSend.split("\n");
            for(String messageString : messages) {
                ITextComponent textComponent = new StringTextComponent(messageString);
                if(this.broadcastMessage) {
                    //server.getPlayerList().sendMessage(textComponent);

                    server.getPlayerList().getPlayers().forEach(onlinePlayer -> {
                        onlinePlayer.sendMessage(textComponent, onlinePlayer.getUniqueID());
                    });
                }
                else {
                    player.sendMessage(textComponent, player.getUniqueID());
                }
            }
        }
    }

    private void executeCommand(MinecraftServer server, ServerPlayerEntity player, String service, String voteCount){
        //server.commandManager.executeCommand(server, replace(this.command, player, service, voteCount));

        server.getCommandManager().handleCommand(server.getCommandSource(), replace(this.command, player, service, voteCount));
    }

    @Override
    public String getType() {
        return "default";
    }

    @Override
    public void activate(MinecraftServer server, ServerPlayerEntity player, String timestamp, String service, String address) throws RewardException{
        sendMessage(server, player, service, "0");
        executeCommand(server,  player,  service,  "0");
    }
}


