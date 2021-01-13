package io.github.zellfrey.forgevotifier.command;

import io.github.zellfrey.forgevotifier.ForgeVotifier;
import io.github.zellfrey.forgevotifier.api.VoteReceivedEvent;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommandFVFakeVote extends CommandBase {
    @Override
    public String getName() {
        return "fakevote";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/forgevotifier fakevote <playerName> - Creates a fake vote for the specified player";
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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        EntityPlayerMP playerMP = args.length == 1 ? server.getPlayerList().getPlayerByUsername(args[0].toLowerCase())
                : getCommandSenderAsPlayer(sender);

        sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "Creating fake vote"));

        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            ForgeVotifier.getLogger().info("[{}] received vote from {} (service: {})", "", playerMP.getName(), "FAKE");
            MinecraftForge.EVENT_BUS.post(new VoteReceivedEvent(playerMP, "FAKE", "LOCAL", "NOW"));
        });
    }
}
