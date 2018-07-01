package com.github.upcraftlp.votifier.event;

import com.github.upcraftlp.votifier.ForgeVotifier;
import com.github.upcraftlp.votifier.api.VoteReceivedEvent;
import com.github.upcraftlp.votifier.api.reward.Reward;
import com.github.upcraftlp.votifier.api.reward.RewardStore;
import com.github.upcraftlp.votifier.command.CommandVote;
import com.github.upcraftlp.votifier.config.VotifierConfig;
import com.github.upcraftlp.votifier.util.ModUpdateHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

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
                reward.activate(server, event.getEntityPlayer(), event.getTimestamp(), event.getServiceDescriptor(), event.getRemoteAddress());
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
        int rewardCount = RewardStore.getStore().getOutStandingRewardCount(event.player.getName());
        if(rewardCount > 0) event.player.addChatComponentMessage(CommandVote.getOutstandingRewardsText(rewardCount));
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if(VotifierConfig.enableUpdateChecker && server.getPlayerList().getOppedPlayers().getPermissionLevel(event.player.getGameProfile()) == server.getOpPermissionLevel()) { //player is opped
            ForgeVersion.CheckResult result = ModUpdateHandler.getResult();
            if(VotifierConfig.enableUpdateChecker && ModUpdateHandler.hasUpdate(result)) {
                event.player.addChatComponentMessage(new TextComponentString("There's an update available for " + ForgeVotifier.MODNAME + ", check the server log for details!"));
            }
        }
    }
}
