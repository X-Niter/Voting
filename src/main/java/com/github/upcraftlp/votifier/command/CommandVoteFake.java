package com.github.upcraftlp.votifier.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.*;
import com.github.upcraftlp.votifier.ForgeVotifier;
import com.github.upcraftlp.votifier.api.VoteReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommandVoteFake extends CommandBase {
    @Override
    public String getName() {
        return "fake";
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
        return "/vote fake - Create a fake vote to test rewards";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);
        
        playerMP.sendMessage(new TextComponentString(TextFormatting.GOLD + "Initiating fakevote"));
        server.getPlayerList().sendMessage(new TextComponentString(TextFormatting.GREEN + playerMP.getName() + " has initiated a fakevote"));
        
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> { //ensure we are not handling the event on the network thread
        ForgeVotifier.getLogger().info("[{}] received vote from {} (service: {})", "", playerMP, "FAKE");
        MinecraftForge.EVENT_BUS.post(new VoteReceivedEvent(playerMP, "FAKE", "LOCAL", "NOW"));

        });   

    }

}
