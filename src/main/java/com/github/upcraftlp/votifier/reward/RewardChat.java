package com.github.upcraftlp.votifier.reward;

import com.github.upcraftlp.votifier.ForgeVotifier;
import api.reward.Reward;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.List;

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

    @SuppressWarnings("unchecked")
    @Override
    public void activate(MinecraftServer server, EntityPlayer player, String timestamp, String service, String address) {
        String msg = replace(messageRaw, player, service);
        if(this.parseAsTellraw) {
            try {
                IChatComponent textComponent = IChatComponent.Serializer.jsonToComponent(msg);
                if(this.broadcastMessage) {
                    for(EntityPlayerMP playerMP : (List<EntityPlayerMP>) server.getConfigurationManager().playerEntityList) {
                        playerMP.addChatComponentMessage(textComponent);
                    }
                }
                else player.addChatComponentMessage(textComponent);
            }
            catch(Exception e) {
                ForgeVotifier.getLogger().error("error parsing chat reward!", e);
            }
        }
        else {
            String[] messages = msg.split("\n");
            for(String messageString : messages) {
                IChatComponent textComponent = new ChatComponentText(messageString);
                if(this.broadcastMessage) server.getConfigurationManager().sendChatMsg(textComponent);
                else player.addChatComponentMessage(textComponent);
            }
        }
    }
}
