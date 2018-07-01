package com.github.upcraftlp.votifier.command;

import com.github.upcraftlp.votifier.api.reward.Reward;
import com.github.upcraftlp.votifier.config.VotifierConfig;
import com.github.upcraftlp.votifier.reward.store.RewardStoreWorldSavedData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import javax.annotation.Nullable;
import java.util.List;

public class CommandVote extends CommandBase {

    @Override
    public String getCommandName() {
        return "vote";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/vote [claim|get]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);
        if(args.length == 0) {
            playerMP.addChatComponentMessage(ChatComponentProcessor.processComponent(MinecraftServer.getServer(), IChatComponent.Serializer.jsonToComponent(Reward.replace(VotifierConfig.voteCommand, sender, "")), playerMP));
            return;
        } else if(args.length == 1) {
            if("claim".equalsIgnoreCase(args[0])) {
                RewardStoreWorldSavedData wsd = RewardStoreWorldSavedData.get(playerMP.getServerForPlayer());
                if(wsd.getOutStandingRewardCount(playerMP.getDisplayNameString()) == 0) throw new CommandException("You have no outstanding rewards!");
                else wsd.claimRewards(playerMP.getDisplayNameString());
                return;
            }
            else if("get".equalsIgnoreCase(args[0])) {
                int outstandingRewards = RewardStoreWorldSavedData.get(playerMP.getServerForPlayer()).getOutStandingRewardCount(playerMP.getDisplayNameString());
                if(outstandingRewards == 0) sender.addChatMessage(new ChatComponentText("You have no outstanding rewards!"));
                else {
                    sender.addChatMessage(getOutstandingRewardsText(outstandingRewards));
                }
                return;
            }
        }
        throw new WrongUsageException(this.getCommandUsage(sender));
    }

    public static IChatComponent getOutstandingRewardsText(int rewardsOutstanding) {
        IChatComponent comp1 = new ChatComponentText("You have " + rewardsOutstanding + " rewards outstanding, use ");
        IChatComponent comp2 = new ChatComponentText("/vote claim").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
        IChatComponent comp3 = new ChatComponentText(" to claim or ");
        IChatComponent clickToVote = new ChatComponentText("click to vote").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA).setItalic(false));
        IChatComponent comp4 = new ChatComponentText("click here").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA).setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, clickToVote)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote claim")));
        IChatComponent comp5 = new ChatComponentText(" to claim them!");
        comp1.appendSibling(comp2).appendSibling(comp3).appendSibling(comp4).appendSibling(comp5);
        return comp1;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length == 1) return getListOfStringsMatchingLastWord(args, "claim");
        return super.addTabCompletionOptions(sender, args, targetPos);
    }
}