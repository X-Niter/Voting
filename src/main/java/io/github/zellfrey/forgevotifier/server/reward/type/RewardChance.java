package io.github.zellfrey.forgevotifier.server.reward.type;

import io.github.zellfrey.forgevotifier.api.RewardException;
import io.github.zellfrey.forgevotifier.server.reward.Reward;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;

public class RewardChance extends Reward {

    private final RewardDefault rewardBasic;
    private final double chanceToBeat;

    public RewardChance(RewardDefault rewardBasic, double chance) {
        this.rewardBasic = rewardBasic;
        this.chanceToBeat = chance;
    }

    @Override
    public String getType() {
        return "chance";
    }

    @Override
    public void activate(MinecraftServer server, ServerPlayerEntity player, String timestamp, String service, String address) throws RewardException {
        double playerChance = ((Math.random() * (100)) + 0);

        if(playerChance <= this.chanceToBeat){
            rewardBasic.activate(server, player, timestamp, service, address);
        }
    }
}
