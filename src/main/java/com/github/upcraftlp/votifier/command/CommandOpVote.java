package com.github.upcraftlp.votifier.command;

import api.VoteReceivedEvent;

import com.github.upcraftlp.votifier.ForgeVotifier;
import com.github.upcraftlp.votifier.config.RewardParser;
import com.github.upcraftlp.votifier.event.VoteEventHandler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;


public class CommandOpVote extends CommandBase {

    @Override
    public String getCommandName() {
        return "opvote";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/opvote [help|reload|create|fakevote]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    public static void getOpHelpUsage(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA +"/opvote help - Shows a list of /opvote commands"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA +"/opvote reload - Reloads the rewards folder"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA +"/opvote create - Create a new default_rewards.json file"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA +"/opvote fakevote - Create a fake vote to test rewards"));
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length > 1){
            throw new SyntaxErrorException("Too many arguments");
        }
        if(args.length == 1){
            switch(args[0].toLowerCase()){
                case "help":
                    getOpHelpUsage(sender);
                break;

                case "reload":
                    reloadData(sender);
                break;

                case "create":
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "Creating fresh new default_rewards.json file"));
                    RewardParser.createRewardData();
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Reward file created!"));
                break;

                case "fakevote":
                    EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);

                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "Initiating fakevote"));

                    ForgeVotifier.getLogger().info("[{}] received vote from {} (service: {})", "", playerMP, "FAKE");
                    MinecraftForge.EVENT_BUS.post(new VoteReceivedEvent(playerMP, "FAKE", "LOCAL", "NOW"));
                break;

                default:
                    throw new WrongUsageException(this.getCommandUsage(sender));
            }
        }
        else{
            throw new WrongUsageException(this.getCommandUsage(sender));
        }
    }

    public static void reloadData(ICommandSender sender){
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Reloading Forge Votifier rewards"));

        RewardParser.reloadRewardData();

        int rewardCount = VoteEventHandler.getRewardsNum();
        int fileCount = RewardParser.configDir.listFiles().length;
        if(rewardCount == 0){
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Found 0 rewards!"));
        }
        if(fileCount == 0){
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Found 0 reward files!"));
        }
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "Votifier registered a total of " + rewardCount + " rewards in " + fileCount + " files!"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Finished reloading"));
    }
    
}