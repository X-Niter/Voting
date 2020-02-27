package com.github.upcraftlp.votifier.event;

import com.github.upcraftlp.votifier.ForgeVotifier;
import com.github.upcraftlp.votifier.api.RewardException;
import com.github.upcraftlp.votifier.api.VoteReceivedEvent;
import com.github.upcraftlp.votifier.api.reward.*;
import com.github.upcraftlp.votifier.command.CommandVote;
import com.github.upcraftlp.votifier.config.VotifierConfig;
import com.github.upcraftlp.votifier.util.ModUpdateHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

@Mod.EventBusSubscriber(value = Side.SERVER, modid = ForgeVotifier.MODID)
public class VoteEventHandler {

    private static final List<Reward> REWARDS = new LinkedList<>();

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void voteMade(VoteReceivedEvent event) {
        Iterator<Reward> iterator = REWARDS.iterator();
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        while(iterator.hasNext()) {
            Reward reward = iterator.next();
            try {
                reward.activate(server, event.getEntityPlayer(), event.getTimestamp(), event.getServiceDescriptor(), event.getRemoteAddress());
            }
            catch (RewardException e) {
                ForgeVotifier.getLogger().error("Error executing votifier reward, removing reward from reward list!", e);
                iterator.remove();
            }
        }
    }

    public static void addReward(Reward reward) {
        REWARDS.add(reward);
    }

    public static void clearRewards(){
        REWARDS.clear();
    }

    public static int getRewardsNum(){
        return REWARDS.size();
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        int rewardCount = RewardStore.getStore().getOutStandingRewardCount(event.player.getName());
        if(rewardCount > 0) {
            event.player.sendMessage(CommandVote.getOutstandingRewardsText(rewardCount));
        }
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if(VotifierConfig.updates.enableUpdateChecker && server.getPlayerList().getOppedPlayers().getPermissionLevel(event.player.getGameProfile()) == server.getOpPermissionLevel()) { //player is opped
            ForgeVersion.CheckResult result = ModUpdateHandler.getResult();
            if(VotifierConfig.updates.enableUpdateChecker && ModUpdateHandler.hasUpdate(result)) {
                event.player.sendMessage(new TextComponentString("There's an update available for " + ForgeVotifier.MODNAME + ", check the server log for details!"));
            }
        }
    }
}
