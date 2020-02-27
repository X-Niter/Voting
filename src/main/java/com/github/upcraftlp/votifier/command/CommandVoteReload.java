package com.github.upcraftlp.votifier.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.*;
import com.github.upcraftlp.votifier.config.RewardParser;
import com.github.upcraftlp.votifier.event.VoteEventHandler;

public class CommandVoteReload extends CommandBase {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/vote reload - Reloads the rewards folder";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);
        
        playerMP.sendMessage(new TextComponentString(TextFormatting.BLUE + "Reloading Forge Votifier rewards"));
        
        RewardParser.reloadRewardData();
        int rewardCount = VoteEventHandler.getRewardsNum();
        int fileCount = RewardParser.configDir.listFiles().length;
        if(rewardCount == 0){
            playerMP.sendMessage(new TextComponentString(TextFormatting.RED + "Found 0 rewards!"));
        }
        if(fileCount == 0){
            playerMP.sendMessage(new TextComponentString(TextFormatting.RED + "Found 0 reward files!"));
        }
        playerMP.sendMessage(new TextComponentString(TextFormatting.BLUE + "Votifier registered a total of " + rewardCount + " rewards in " + fileCount + " files!"));
        playerMP.sendMessage(new TextComponentString(TextFormatting.GREEN + "Finished reloading"));
    }

}
