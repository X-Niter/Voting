package io.github.zellfrey.forgevotifier.command;

import io.github.zellfrey.forgevotifier.ForgeVotifier;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandVoteTop extends CommandBase {

    @Override
    public String getName() {
        return "top";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/vote top <count> - Shows the top voters on the server";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0){
            sender.sendMessage(new TextComponentString("Shows top vote count of top 5"));
        }
        else{
            int topVoters = tryParse(args[0], 5);
            sender.sendMessage(new TextComponentString("Shows top vote count of top " + topVoters));
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
