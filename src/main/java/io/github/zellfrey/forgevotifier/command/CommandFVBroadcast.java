package io.github.zellfrey.forgevotifier.command;

import io.github.zellfrey.forgevotifier.util.TextUtils;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;


public class CommandFVBroadcast extends CommandBase{

    @Override
    public String getName() {
        return "broadcast";
    }

    @Override
    public String getUsage(ICommandSender sender) { return "/forgevotifier broadcast <message> - Sends a message to all online players"; }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if(args.length == 0){
            throw new CommandException("Message cannot be blank");
        }
        else{
            String message = "";

            for(String word : args){
                message += word + " ";
            }
            ITextComponent msgToBroadCast = new TextComponentString(TextUtils.TransformBroadcastMsg(message));
            server.getPlayerList().sendMessage(broadCastTag().appendSibling(msgToBroadCast));
        }
    }

    private static ITextComponent broadCastTag(){
        ITextComponent msgTag = new TextComponentString(TextFormatting.DARK_RED + "Broadcast");
        ITextComponent borderLeft = new TextComponentString(TextFormatting.GOLD + "[");
        ITextComponent borderRight = new TextComponentString(TextFormatting.GOLD + "]");

        return borderLeft.appendSibling(msgTag).appendSibling(borderRight);
    }
}
