package io.github.zellfrey.forgevotifier.command;

import io.github.zellfrey.forgevotifier.ForgeVotifier;
import io.github.zellfrey.forgevotifier.config.RewardParser;
import io.github.zellfrey.forgevotifier.event.VoteEventHandler;
import io.github.zellfrey.forgevotifier.util.TextUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;


public class CommandFVReload extends CommandBase {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getUsage(ICommandSender sender) { return "/forgevotifier reload - Reloads config and voting rewards"; }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args){
        sender.sendMessage(new TextComponentString(TextFormatting.BLUE + "Reloading Config and Voting rewards"));

        ForgeVotifier.instance.loadConfig();

        RewardParser.loadRewardData();
        int rewardCount = VoteEventHandler.getRewardsNum();
        int fileCount = RewardParser.configDir.listFiles().length;
        if(rewardCount == 0){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Found 0 rewards!"));
        }
        if(fileCount == 0){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Found 0 reward files!"));
        }
        sender.sendMessage(new TextComponentString(TextFormatting.BLUE + "Votifier registered a total of " + rewardCount + " rewards in " + fileCount + " files!"));
        sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Finished reloading"));
    }
}
