package io.github.zellfrey.forgevotifier.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

public class CommandForgeVotifier extends CommandBase {

    @Override
    public String getName() {
        return "forgevotifier";
    }

    @Override
    public String getUsage(ICommandSender sender) { return "/forgevotifier [help:reload:fakevote]"; }

    @Override
    public List<String> getAliases()
    {
        ArrayList<String> aliases = new ArrayList<>();
        aliases.add("fv");
        return aliases;
    }

    private static ITextComponent getHelpUsage(){
        ITextComponent comp1 = new TextComponentString("------Forge Votifier------" + "\n");
        ITextComponent comp2 = new TextComponentString("/forgevotifier help - Displays more information of commands" + "\n");
        ITextComponent comp3 = new TextComponentString("/forgevotifier reload - Reloads config and voting rewards" + "\n");
        ITextComponent comp4 = new TextComponentString("/forgevotifier fakevote <playerName> - Creates a fake vote for the specified player" + "\n");

        comp1.appendSibling(comp2).appendSibling(comp3).appendSibling(comp4);

        return comp1;
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

        if(args.length > 2){
            throw new SyntaxErrorException("Too many arguments");
        }

        if(args.length == 1 || args.length == 2){
            switch(args[0].toLowerCase()){
                case "help":
                    sender.sendMessage(getHelpUsage());
                    break;

                case "reload":
                    sender.sendMessage(new TextComponentString("reloading forge votifier"));
                    break;

                case "fakevote":
                    EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);
                    if(args.length == 1){
                        playerMP.sendMessage(new TextComponentString("Creating vote for: " + playerMP.getName()));
                    }
                    else{
                        String playerName = args[1].toLowerCase();
                        playerMP.sendMessage(new TextComponentString("Creating vote for: " + playerName));
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
}
