package io.github.zellfrey.forgevotifier.event;

import io.github.zellfrey.forgevotifier.ForgeVotifier;
import io.github.zellfrey.forgevotifier.api.RewardException;
import io.github.zellfrey.forgevotifier.api.VoteReceivedEvent;
import io.github.zellfrey.forgevotifier.command.CommandVote;
//import com.github.upcraftlp.votifier.util.ModUpdateHandler;
import io.github.zellfrey.forgevotifier.reward.Reward;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
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
            server.addScheduledTask(() -> {
                try {
                    reward.activate(server, event.getEntityPlayer(), event.getTimestamp(), event.getServiceDescriptor(), event.getRemoteAddress());

                }
                catch (RewardException e) {
                    ForgeVotifier.getLogger().error("Error executing votifier reward, removing reward from reward list!", e);
                    iterator.remove();
                }
            });
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
//        int rewardCount = RewardStore.getStore().getOutStandingRewardCount(event.player.getName());
        int rewardCount = 1;
        if(rewardCount > 0) {
            event.player.sendMessage(CommandVote.getOutstandingRewardsText(rewardCount));
        }

        if(ForgeVotifier.config.getUpdateCheck() && ForgeVotifier.isOpped(event.player.getGameProfile())) { //player is opped
//            ForgeVersion.CheckResult result = ModUpdateHandler.getResult();
//            if(ModUpdateHandler.hasUpdate(result)) {
                String updateString = "There's an update available for Forge Votifier, check the server log for details!";
                event.player.sendMessage(new TextComponentString(updateString));
//            }
        }
    }
}
