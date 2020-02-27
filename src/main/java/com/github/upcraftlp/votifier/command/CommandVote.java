package com.github.upcraftlp.votifier.command;

import com.github.upcraftlp.votifier.api.reward.Reward;
import com.github.upcraftlp.votifier.config.VotifierConfig;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.*;
import net.minecraftforge.server.command.*;

public class CommandVote extends CommandTreeBase {

    public CommandVote() {
        addSubcommand(new CommandVoteClaim());
        addSubcommand(new CommandVoteGet());
        addSubcommand(new CommandVoteFake());
        addSubcommand(new CommandVoteReload());
        addSubcommand(new CommandVoteCreate());
        addSubcommand(new CommandTreeHelp(this));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    public static ITextComponent getOutstandingRewardsText(int rewardsOutstanding) {
        ITextComponent comp1 = new TextComponentString("You have " + rewardsOutstanding + " rewards outstanding, use ");
        ITextComponent comp2 = new TextComponentString("/vote claim").setStyle(new Style().setColor(TextFormatting.GREEN));
        ITextComponent comp3 = new TextComponentString(" to claim or ");
        ITextComponent clickToVote = new TextComponentString("click to vote").setStyle(new Style().setColor(TextFormatting.AQUA).setItalic(false));
        ITextComponent comp4 = new TextComponentString("click here").setStyle(new Style().setColor(TextFormatting.AQUA).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, clickToVote)).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote claim")));
        ITextComponent comp5 = new TextComponentString(" to claim them!");
        comp1.appendSibling(comp2).appendSibling(comp3).appendSibling(comp4).appendSibling(comp5);
        return comp1;
    }

    @Override
    public String getName() {
        return "vote";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/vote - Show voting information";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);
        if(args.length == 0) {
            playerMP.sendMessage(TextComponentUtils.processComponent(server, ITextComponent.Serializer.jsonToComponent(Reward.replace(VotifierConfig.voteCommand, sender, "")), playerMP));
        }
        else {
            super.execute(server, sender, args);
        }
    }
}