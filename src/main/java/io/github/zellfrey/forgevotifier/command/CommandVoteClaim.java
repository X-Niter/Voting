package io.github.zellfrey.forgevotifier.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandVoteClaim extends CommandBase {

    @Override
    public String getName() {
        return "claim";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/vote claim - Claim current outstanding rewards";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);
//        RewardStoreWorldSavedData wsd = RewardStoreWorldSavedData.get(playerMP.getServerWorld());
//        if(wsd.getOutStandingRewardCount(playerMP.getName()) == 0) {
//            throw new CommandException("You have no outstanding rewards!");
//        }
//        else {
//            wsd.claimRewards(playerMP.getName());
//        }
        sender.sendMessage(new TextComponentString("vote claim"));
    }
}
