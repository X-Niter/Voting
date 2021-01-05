package io.github.zellfrey.forgevotifier.command;

import io.github.zellfrey.forgevotifier.ForgeVotifier;
import io.github.zellfrey.forgevotifier.api.reward.Reward;
import io.github.zellfrey.forgevotifier.util.TextUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandForgeVotifier extends CommandTreeBase {

    public CommandForgeVotifier() {
        addSubcommand(new CommandFVBroadcast());
        addSubcommand(new CommandFVFakeVote());
        addSubcommand(new CommandFVReload());
    }

    @Override
    public int getRequiredPermissionLevel() { return 0; }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public String getName() {
        return "forgevotifier";
    }

    @Override
    public String getUsage(ICommandSender sender) { return "/forgevotifier - [help:reload:fakevote:broadcast]"; }

    @Override
    public List<String> getAliases()
    {
        ArrayList<String> aliases = new ArrayList<>();
        aliases.add("fv");
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if(args.length == 0 || args[0].toLowerCase().equals("help")) {
            TextUtils.getHelpUsage(sender, this.getUsage(sender), super.getSubCommands());
        }
        else {
            super.execute(server, sender, args);
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        String [] subCmdNames = new String[] {"help", "reload", "fakevote", "broadcast"};
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, subCmdNames) : Collections.emptyList();
    }
}
