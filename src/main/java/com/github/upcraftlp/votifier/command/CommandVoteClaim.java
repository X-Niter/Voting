package com.github.upcraftlp.votifier.command;

import com.github.upcraftlp.votifier.reward.store.RewardStoreWorldSavedData;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandVoteClaim extends CommandBase {
    @Override
    public String getName() {
        return "claim";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/vote claim";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);
        RewardStoreWorldSavedData wsd = RewardStoreWorldSavedData.get(playerMP.getServerWorld());
        if(wsd.getOutStandingRewardCount(playerMP.getName()) == 0) {
            throw new CommandException("You have no outstanding rewards!");
        }
        else {
            wsd.claimRewards(playerMP.getName());
        }
    }
}
