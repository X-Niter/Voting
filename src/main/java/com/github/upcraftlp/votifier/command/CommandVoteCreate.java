package com.github.upcraftlp.votifier.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.*;
import com.github.upcraftlp.votifier.config.RewardParser;

public class CommandVoteCreate extends CommandBase {
    @Override
    public String getName() {
        return "createDefaultRewards";
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
        return "/vote createDefaultRewards - Creates a new default_rewards.json file";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);
        
        playerMP.sendMessage(new TextComponentString(TextFormatting.BLUE + "Creating fresh new default_rewards.json file"));
        RewardParser.createRewardData();
        playerMP.sendMessage(new TextComponentString(TextFormatting.GREEN + "Reward file created!"));
    }

}
