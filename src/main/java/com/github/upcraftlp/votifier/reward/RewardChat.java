package com.github.upcraftlp.votifier.reward;

import com.github.upcraftlp.votifier.ForgeVotifier;
import com.github.upcraftlp.votifier.api.reward.Reward;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentUtils;

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
        String msg = replace(messageRaw, player, service);
        ForgeVotifier.getLogger().error("tellraw @p {}", msg);
        if(this.parseAsTellraw) {
            try {
                ITextComponent textComponent = ITextComponent.Serializer.jsonToComponent(msg.replace("\"", "\\\""));
                textComponent = ITextComponent.Serializer.jsonToComponent("[\"\",{\"text\":\"Test\",\"color\":\"aqua\"},{\"text\":\" just voted on \",\"color\":\"gold\"},{\"text\":\"MinecraftServers.biz\",\"color\":\"aqua\"},{\"text\":\" and received their daily reward!\",\"color\":\"gold\"},{\"text\":\"\"},{\"text\":\"Get yours using \",\"color\":\"gold\"},{\"text\":\"/vote\",\"color\":\"green\"},{\"text\":\" or by clicking \",\"color\":\"gold\"},{\"text\":\"here\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://minecraft.curseforge.com/projects/229905\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[{\"text\":\"click to vote!\",\"color\":\"aqua\"}]}},{\"text\":\"!\",\"color\":\"gold\"}]");
                if(this.broadcastMessage) {
                    for(EntityPlayerMP playerMP : server.getPlayerList().getPlayers()) {
                        playerMP.sendMessage(TextComponentUtils.processComponent(server, textComponent, playerMP));
                    }
                }
                else player.sendMessage(TextComponentUtils.processComponent(server, textComponent, player));
            }
            catch(CommandException e) {
                ForgeVotifier.getLogger().error("error parsing chat reward!", e);
            }
        }
        else {
            String[] messages = msg.split("\n");
            for(String messageString : messages) {
                ITextComponent textComponent = new TextComponentString(messageString);
                if(this.broadcastMessage) server.getPlayerList().sendMessage(textComponent);
                else player.sendMessage(textComponent);
            }
        }
    }
}
