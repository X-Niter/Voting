package io.github.zellfrey.forgevotifier.event;

import io.github.zellfrey.forgevotifier.ForgeVotifier;
import io.github.zellfrey.forgevotifier.api.RewardException;
import io.github.zellfrey.forgevotifier.api.VoteReceivedEvent;
import io.github.zellfrey.forgevotifier.api.reward.*;
import io.github.zellfrey.forgevotifier.command.CommandVote;
import io.github.zellfrey.forgevotifier.config.ForgeVotifierConfig;
//import com.github.upcraftlp.votifier.util.ModUpdateHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
        ForgeVotifier.getLogger().info("Vote made");
        ForgeVotifier.getLogger().info(event.getEntityPlayer());
        ForgeVotifier.getLogger().info(event.getTimestamp());
        ForgeVotifier.getLogger().info(event.getServiceDescriptor());
        ForgeVotifier.getLogger().info(event.getRemoteAddress());
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

//    @SubscribeEvent(priority = EventPriority.LOW)
//    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
//        int rewardCount = RewardStore.getStore().getOutStandingRewardCount(event.player.getName());
//        if(rewardCount > 0) {
//            event.player.sendMessage(CommandVote.getOutstandingRewardsText(rewardCount));
//        }
//        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
//        if(VotifierConfig.updates.enableUpdateChecker && server.getPlayerList().getOppedPlayers().getPermissionLevel(event.player.getGameProfile()) == server.getOpPermissionLevel()) { //player is opped
//            ForgeVersion.CheckResult result = ModUpdateHandler.getResult();
//            if(VotifierConfig.updates.enableUpdateChecker && ModUpdateHandler.hasUpdate(result)) {
//                event.player.sendMessage(new TextComponentString("There's an update available for " + ForgeVotifier.MODNAME + ", check the server log for details!"));
//            }
//        }
//    }
}
