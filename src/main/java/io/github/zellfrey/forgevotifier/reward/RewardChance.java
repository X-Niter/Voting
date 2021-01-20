package io.github.zellfrey.forgevotifier.reward;

import io.github.zellfrey.forgevotifier.ForgeVotifier;
import io.github.zellfrey.forgevotifier.api.RewardException;
import io.github.zellfrey.forgevotifier.api.reward.Reward;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class RewardChance extends Reward {

    private final double chanceToBeat;

    public RewardChance(String command, String voteMessage, boolean broadcast, boolean parseAsTellraw, double chance) {
        super(command, voteMessage, broadcast, parseAsTellraw);
        this.chanceToBeat = chance;
    }

    @Override
    public String getType() {
        return "chance";
    }

    @Override
    public void activate(MinecraftServer server, EntityPlayer player, String timestamp, String service, String address) throws RewardException {
        double playerChance = ((Math.random() * (100 - 0)) + 0);

        if(playerChance <= chanceToBeat){
            this.sendMessage(server, player, service, "0");
            this.executeCommand(server,  player,  service,  "0");
        }
    }
}
