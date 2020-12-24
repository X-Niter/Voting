package io.github.zellfrey.forgevotifier.command;

import io.github.zellfrey.forgevotifier.ForgeVotifier;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class CommandVote extends CommandBase{

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

    private static ITextComponent getHelpUsage(){
        ITextComponent comp1 = new TextComponentString("------Forge Votifier------" + "\n");
        ITextComponent comp2 = new TextComponentString("/vote - Shows vote message" + "\n");
        ITextComponent comp3 = new TextComponentString("/vote help - Displays more information of vote command" + "\n");
        ITextComponent comp4 = new TextComponentString("/vote get - Checks for outstanding rewards & your vote count" + "\n");
        ITextComponent comp5 = new TextComponentString("/vote claim - Claim outstanding rewards" + "\n");
        ITextComponent comp6 = new TextComponentString("/vote top <count> - Shows the top voters on the server" + "\n");

        comp1.appendSibling(comp2).appendSibling(comp3).appendSibling(comp4).appendSibling(comp5).appendSibling(comp6);

        return comp1;
    }

    @Override
    public String getName() {
        return "vote";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/vote [help:get:claim:top]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);

        if(args.length > 2){
            throw new SyntaxErrorException("Too many arguments");
        }

        if(args.length == 0) {
//            Reward.replace(ForgeVotifier.config.getVoteCommand(), sender, "")), playerMP));
            ITextComponent voteTellRaw = ITextComponent.Serializer.jsonToComponent(ForgeVotifier.config.getVoteCommand());
            playerMP.sendMessage(TextComponentUtils.processComponent(sender, voteTellRaw, playerMP));
        }
        if(args.length == 1 || args.length == 2){
            switch(args[0].toLowerCase()){
                case "help":
                    playerMP.sendMessage(getHelpUsage());
                    break;
                case "get":
                    playerMP.sendMessage(new TextComponentString("check rewards, and check vote count"));
                    break;

                case "claim":
                    playerMP.sendMessage(new TextComponentString("Claim rewards"));
                    break;

                case "top":
                    if(args.length == 1){
                        playerMP.sendMessage(new TextComponentString("Shows top vote count of top 5"));
                    }
                    else{
                        int topVoters = tryParse(args[1], 5);
                        playerMP.sendMessage(new TextComponentString("Shows top vote count of top " + topVoters));
                    }
                    break;

                default:
                    throw new WrongUsageException(getUsage(sender));
            }
        }
        else{
            throw new WrongUsageException(getUsage(sender));
        }
    }

    private static Integer tryParse(String text, int defaultVal) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            ForgeVotifier.getLogger().error("Tried to parse string as integer. Falling onto default value");
            return defaultVal;
        }
    }
}
