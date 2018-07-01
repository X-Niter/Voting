package com.github.upcraftlp.votifier.event;

import com.github.upcraftlp.votifier.ForgeVotifier;
import api.VoteReceivedEvent;
import api.reward.Reward;
import api.reward.RewardStore;
import com.github.upcraftlp.votifier.command.CommandVote;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.server.MinecraftServer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class VoteEventHandler {

    private static final List<Reward> REWARDS = new LinkedList<>();

    @SubscribeEvent(priority = EventPriority.LOW)
    public void voteMade(VoteReceivedEvent event) {
        Iterator<Reward> iterator = REWARDS.iterator();
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        while(iterator.hasNext()) {
            Reward reward = iterator.next();
            try {
                reward.activate(server, event.entityPlayer, event.getTimestamp(), event.getServiceDescriptor(), event.getRemoteAddress());
            }
            catch(Exception e) {
                ForgeVotifier.getLogger().error("Error executing votifier reward, removing reward from reward list!", e);
                iterator.remove();
            }
        }
    }

    public static void addReward(Reward reward) {
        REWARDS.add(reward);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        int rewardCount = RewardStore.getStore().getOutStandingRewardCount(event.player.getDisplayName());
        if(rewardCount > 0) event.player.addChatComponentMessage(CommandVote.getOutstandingRewardsText(rewardCount));
    }
}
