package com.github.upcraftlp.votifier.api.reward;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class RewardChat extends Reward {

    private final boolean broadcastMessage;
    private final String messageRaw;
    private boolean parseAsTellraw;

    public RewardChat(String raw, boolean broadcastMessage, boolean parseAsTellraw) {
        this.broadcastMessage = broadcastMessage;
        this.messageRaw = raw;
        this.parseAsTellraw = parseAsTellraw;
    }

    @Override
    public String getType() {
        return "chat";
    }

    @Override
    public void activate(MinecraftServer server, EntityPlayer player, long timestamp, String service, String address) {
        String[] messages = replace(messageRaw, player, service).split("\n");
        for(String msg : messages) {
            ITextComponent textComponent;
            if(this.parseAsTellraw) textComponent = ITextComponent.Serializer.jsonToComponent(msg);
            else textComponent = new TextComponentString(msg);

            if(this.broadcastMessage) server.getPlayerList().sendMessage(textComponent);
            else player.sendMessage(textComponent);
        }
    }
}
