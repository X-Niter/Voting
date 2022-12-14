package io.github.zellfrey.forgevotifier.server.event;

import io.github.zellfrey.forgevotifier.ForgeVotifier;
import io.github.zellfrey.forgevotifier.api.RewardException;
import io.github.zellfrey.forgevotifier.api.VoteReceivedEvent;
import io.github.zellfrey.forgevotifier.server.commands.impl.CommandVote;
import io.github.zellfrey.forgevotifier.server.reward.Reward;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ForgeVotifier.MODID, value = Dist.DEDICATED_SERVER)
public class VoteEventHandler {

    private static final List<Reward> REWARDS = new LinkedList<>();

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void voteMade(VoteReceivedEvent event) {
        Iterator<Reward> iterator = REWARDS.iterator();
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

        while(iterator.hasNext()) {
            Reward reward = iterator.next();
            server.deferTask(() -> {
                try {
                    reward.activate(server, (ServerPlayerEntity) event.getPlayer(), event.getTimestamp(), event.getServiceDescriptor(), event.getRemoteAddress());

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

        UUID uuid = event.getPlayer().getUniqueID();
        int rewardCount = 1;
        if(rewardCount > 0) {
            //event.player.sendMessage(CommandVote.getOutstandingRewardsText(rewardCount));
            event.getPlayer().sendMessage(CommandVote.getOutstandingRewardsText(rewardCount), uuid);
        }

        if(ForgeVotifier.config.getUpdateCheck() && ForgeVotifier.isOpped(event.getPlayer().getGameProfile())) {
            //player is opped
//            ForgeVersion.CheckResult result = ModUpdateHandler.getResult();
//            if(ModUpdateHandler.hasUpdate(result)) {

                String updateString = "There's an update available for Forge Votifier, check the server log for details!";
                event.getPlayer().sendMessage(new StringTextComponent(updateString), uuid);

//            }
        }
    }
}
