package com.github.upcraftlp.votifier.command;

import com.github.upcraftlp.votifier.reward.store.RewardStoreWorldSavedData;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandVoteGet extends CommandBase {
    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/vote get";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);
        int outstandingRewards = RewardStoreWorldSavedData.get(playerMP.getServerWorld()).getOutStandingRewardCount(playerMP.getName());
        if(outstandingRewards == 0) {
            sender.sendMessage(new TextComponentString("You have no outstanding rewards!"));
        }
        else {
            sender.sendMessage(CommandVote.getOutstandingRewardsText(outstandingRewards));
        }
    }
}
