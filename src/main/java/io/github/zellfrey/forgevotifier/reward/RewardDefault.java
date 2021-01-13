package io.github.zellfrey.forgevotifier.reward;

import io.github.zellfrey.forgevotifier.api.RewardException;
import io.github.zellfrey.forgevotifier.api.reward.Reward;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class RewardDefault extends Reward{

    public RewardDefault(String command, String voteMessage, boolean broadcast, boolean parseAsTellraw) {
        super(command, voteMessage, broadcast, parseAsTellraw);
    }

    @Override
    public String getType() {
        return "default";
    }

    @Override
    public void activate(MinecraftServer server, EntityPlayer player, String timestamp, String service, String address) throws RewardException{
        this.sendMessage(server, player, service, "0");
        this.executeCommand(server,  player,  service,  "0");
    }
}


