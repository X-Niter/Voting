package io.github.zellfrey.forgevotifier.command;

import io.github.zellfrey.forgevotifier.ForgeVotifier;
import io.github.zellfrey.forgevotifier.reward.Reward;
import io.github.zellfrey.forgevotifier.util.TextUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandVote extends CommandTreeBase {

    public CommandVote() {
        addSubcommand(new CommandVoteClaim());
        addSubcommand(new CommandVoteGet());
        addSubcommand(new CommandVoteTop());
    }

    @Override
    public int getRequiredPermissionLevel() { return 0; }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public String getName() {
        return "vote";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/vote - Show voting information";
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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if(args.length == 0) {
            EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);
            String voteCmd = Reward.replace(ForgeVotifier.config.getVoteCommand(), sender, "", "");
            ITextComponent voteJSON = ITextComponent.Serializer.jsonToComponent(voteCmd);

            playerMP.sendMessage(TextComponentUtils.processComponent(sender, voteJSON, playerMP));
        }
        else {
            if(args[0].toLowerCase().equals("help")){
                TextUtils.getHelpUsage(sender, this.getUsage(sender), super.getSubCommands());

            }else{
                super.execute(server, sender, args);
            }
        }
    }
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        String [] subCmdNames = new String[] {"help", "get", "claim", "top"};
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, subCmdNames) : Collections.emptyList();
    }
}
