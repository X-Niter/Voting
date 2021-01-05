package io.github.zellfrey.forgevotifier.util;

import io.github.zellfrey.forgevotifier.ForgeVotifier;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    private static final Pattern COLOUR_CODE_PATTERN = Pattern.compile("&[0-9A-flmnork]");

    private static HashMap<String, String> COLOUR_MAP = new HashMap <>();

    private static void populateColourMap(){
        COLOUR_MAP.put("&0", "\u00A70");
        COLOUR_MAP.put("&1", "\u00A71");
        COLOUR_MAP.put("&2", "\u00A72");
        COLOUR_MAP.put("&3", "\u00A73");
        COLOUR_MAP.put("&4", "\u00A74");
        COLOUR_MAP.put("&5", "\u00A75");
        COLOUR_MAP.put("&6", "\u00A76");
        COLOUR_MAP.put("&7", "\u00A77");
        COLOUR_MAP.put("&8", "\u00A78");
        COLOUR_MAP.put("&9", "\u00A79");
        COLOUR_MAP.put("&a", "\u00A7a");
        COLOUR_MAP.put("&b", "\u00A7b");
        COLOUR_MAP.put("&c", "\u00A7c");
        COLOUR_MAP.put("&d", "\u00A7d");
        COLOUR_MAP.put("&e", "\u00A7e");
        COLOUR_MAP.put("&f", "\u00A7f");
        COLOUR_MAP.put("&l", "\u00A7l");
        COLOUR_MAP.put("&m", "\u00A7m");
        COLOUR_MAP.put("&n", "\u00A7n");
        COLOUR_MAP.put("&o", "\u00A7o");
        COLOUR_MAP.put("&r", "\u00A7r");
        COLOUR_MAP.put("&k", "\u00A7k");
        // &K MAGIC             //   OBFUSCATED(null,' ',0),
        //Using bukkit colour code formatting with motd colour code
        //As seen here >>> https://minecraft.gamepedia.com/Formatting_codes
    }

    public static void init(){
        populateColourMap();
    }

    //Iterate through strings to get MOTD colour format
    public static String TransformBroadcastMsg(String msg){

        ArrayList<String> colourCodes = new ArrayList<>();
        Matcher codeMatch = COLOUR_CODE_PATTERN.matcher(msg);

        while (codeMatch.find()) {
            if(!colourCodes.contains(codeMatch.group())){
                colourCodes.add(codeMatch.group());
            }
        }
        if(colourCodes.size() != 0){
            for(String colour : colourCodes){
                msg = msg.replace(colour, COLOUR_MAP.get(colour));
            }
        }
        return msg;
    }

    public static void getHelpUsage(ICommandSender sender, String rootCmd, Collection subCmds){

        ITextComponent[] voteHelpMessage = new ITextComponent[3];
        voteHelpMessage[0] = new TextComponentString("---------=Forge Votifier=----------");
        voteHelpMessage[0].setStyle(new Style().setColor(TextFormatting.DARK_AQUA));

        voteHelpMessage[1] = getPrettyCommandUsage(rootCmd);

        voteHelpMessage[2] = new TextComponentString("").setStyle(new Style().setColor(TextFormatting.DARK_GREEN));
        Iterator<ICommand> voteSubCommands = subCmds.iterator();

        while(voteSubCommands.hasNext()){
            voteHelpMessage[2].appendSibling(getPrettyCommandUsage(voteSubCommands.next().getUsage(sender)));

            if (voteSubCommands.hasNext()){
                voteHelpMessage[2].appendText("\n");
            }
        }
        TextUtils.sendMessages(voteHelpMessage, sender);
    }

    public static ITextComponent getPrettyCommandUsage(String commandUsage){
        String[] usageSplit = commandUsage.split("-");

        ITextComponent cmdInput = new TextComponentString(usageSplit[0] + "-");
        cmdInput.setStyle(new Style().setColor(TextFormatting.GOLD));

        ITextComponent cmdInfo = new TextComponentString(usageSplit[1]);
        cmdInfo.setStyle(new Style().setColor(TextFormatting.DARK_GREEN));

        return cmdInput.appendSibling(cmdInfo);
    }

    public static void printDebugStrConsole(String... sArgs){
        for(String line : sArgs){
            ForgeVotifier.getLogger().info(line);
        }
    }

    public static void sendMessages(ITextComponent [] msgsToSend, ICommandSender sender){
        for(ITextComponent msg : msgsToSend){
            sender.sendMessage(msg);
        }
    }

}