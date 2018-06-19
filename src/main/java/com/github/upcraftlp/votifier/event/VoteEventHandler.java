package com.github.upcraftlp.votifier.event;

import com.github.upcraftlp.votifier.ForgeVotifier;
import com.github.upcraftlp.votifier.api.VoteReceivedEvent;
import com.github.upcraftlp.votifier.api.reward.Reward;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber(value = Side.SERVER, modid = ForgeVotifier.MODID)
public class VoteEventHandler {

    private static final List<Reward> REWARDS = new LinkedList<>();

    @SubscribeEvent
    public static void voteMade(VoteReceivedEvent event) {
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
}
