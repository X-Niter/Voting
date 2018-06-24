package com.github.upcraftlp.votifier.command;

import com.github.upcraftlp.votifier.api.reward.Reward;
import com.github.upcraftlp.votifier.config.VotifierConfig;
import com.github.upcraftlp.votifier.reward.store.RewardStoreWorldSavedData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandVote extends CommandBase {

    @Override
    public String getName() {
        return "vote";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/vote [claim|get]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);
        if(args.length == 0) {
            playerMP.sendMessage(ITextComponent.Serializer.jsonToComponent(Reward.replace(VotifierConfig.voteCommand, sender, "")));
            return;
        } else if(args.length == 1) {
            if("claim".equalsIgnoreCase(args[0])) {
                RewardStoreWorldSavedData wsd = RewardStoreWorldSavedData.get(playerMP.getServerWorld());
                if(wsd.getOutStandingRewardCount(playerMP.getName()) == 0) throw new CommandException("You have no outstanding rewards!");
                else wsd.claimRewards(playerMP.getName());
                return;
            }
            else if("get".equalsIgnoreCase(args[0])) {
                int outstandingRewards = RewardStoreWorldSavedData.get(playerMP.getServerWorld()).getOutStandingRewardCount(playerMP.getName());
                if(outstandingRewards == 0) sender.sendMessage(new TextComponentString("You have no outstanding rewards!"));
                else sender.sendMessage(new TextComponentString("You have " + outstandingRewards + " rewards outstanding, use " + TextFormatting.GREEN + "/vote claim" + TextFormatting.RESET + " to claim!"));
                return;
            }
        }
        throw new WrongUsageException(getUsage(sender));
    }
}