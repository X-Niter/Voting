package io.github.zellfrey.forgevotifier.server.commands.impl;

//import io.github.zellfrey.forgevotifier.reward.store.RewardStoreConfig;

import net.minecraft.command.CommandException;
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
        return "/vote get - Shows current outstanding rewards";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);
//        int outstandingRewards = RewardStoreConfig.get(playerMP.getServerWorld()).getOutStandingRewardCount(playerMP.getName());
//        if(outstandingRewards == 0) {
//            sender.sendMessage(new TextComponentString("You have no outstanding rewards!"));
//        }
//        else {
//            sender.sendMessage(CommandVote.getOutstandingRewardsText(outstandingRewards));
//        }

        sender.sendMessage(new TextComponentString("You have no outstanding rewards!"));
        sender.sendMessage(CommandVote.getOutstandingRewardsText(2));
    }
}
