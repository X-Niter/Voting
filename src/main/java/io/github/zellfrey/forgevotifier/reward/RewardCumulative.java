package io.github.zellfrey.forgevotifier.reward;

import io.github.zellfrey.forgevotifier.ForgeVotifier;
import io.github.zellfrey.forgevotifier.api.RewardException;
import io.github.zellfrey.forgevotifier.api.reward.Reward;
import io.github.zellfrey.forgevotifier.api.reward.StoredPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class RewardCumulative extends Reward {

    private final RewardDefault rewardBasic;
    private final int votesToParse;

    public RewardCumulative(RewardDefault rewardBasic, int votesToParse) {
        this.rewardBasic = rewardBasic;
        this.votesToParse = votesToParse;
    }

    @Override
    public String getType() {
        return "chance";
    }

    @Override
    public void activate(MinecraftServer server, EntityPlayer player, String timestamp, String service, String address) throws RewardException {
        int playerVotes = StoredPlayer.cumulativeTest;
        ForgeVotifier.getLogger().info(playerVotes);

        if(this.votesToParse == playerVotes){
            rewardBasic.activate(server, player, timestamp, service, address);
        }
    }
}
