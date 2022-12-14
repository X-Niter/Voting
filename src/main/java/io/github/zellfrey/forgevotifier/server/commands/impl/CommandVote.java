package io.github.zellfrey.forgevotifier.server.commands.impl;

import com.mojang.brigadier.CommandDispatcher;
import io.github.zellfrey.forgevotifier.ForgeVotifier;
import io.github.zellfrey.forgevotifier.server.reward.Reward;
import io.github.zellfrey.forgevotifier.server.util.TextUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ICommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandVote extends Commands {

    private final CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<>();

    public CommandVote() {
        addSubcommand(new CommandVoteClaim());
        addSubcommand(new CommandVoteGet());
        addSubcommand(new CommandVoteTop());
    }


    public int getRequiredPermissionLevel() { return 0; }


    public boolean checkPermission(MinecraftServer server, ICommandSource sender) {
        return true;
    }

    public String getName() {
        return "vote";
    }


    public String getUsage(ICommandSource sender) {
        return "/vote - Show voting information";
    }


    public static ITextComponent getOutstandingRewardsText(int rewardsOutstanding) {
        //List<ITextComponent> tSiblings = Lists.newArrayList();

        ITextComponent comp1 = new StringTextComponent("You have " + rewardsOutstanding + " rewards outstanding, use ");
        ITextComponent comp2 = new StringTextComponent("/vote claim").setStyle(Style.EMPTY.setColor(Color.fromHex("00FF00")));
        ITextComponent comp3 = new StringTextComponent(" to claim or ");
        ITextComponent clickToVote = new StringTextComponent("click to vote").setStyle(new Style().setColor(TextFormatting.AQUA).setItalic(false));
        ITextComponent comp4 = new StringTextComponent("click here").setStyle(new Style().setColor(TextFormatting.AQUA).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, clickToVote)).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote claim")));
        ITextComponent comp5 = new StringTextComponent(" to claim them!");

        comp1.getSiblings().add(comp2);
        comp1.getSiblings().add(comp3);
        comp1.getSiblings().add(comp4);
        comp1.getSiblings().add(comp5);
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
