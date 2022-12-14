package io.github.zellfrey.forgevotifier.server.util;

import io.github.zellfrey.forgevotifier.ForgeVotifier;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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

    public static void getHelpUsage(ICommandSource sender, String rootCmd, Collection subCmds){

        ITextComponent[] voteHelpMessage = new ITextComponent[3];
        voteHelpMessage[0] = new StringTextComponent("---------=Forge Votifier=----------");
        voteHelpMessage[0].getStyle().setColor(Color.fromHex("249D9F"));

        voteHelpMessage[1] = getPrettyCommandUsage(rootCmd);

        voteHelpMessage[2] = new StringTextComponent("").setStyle(Style.EMPTY.setColor(Color.fromHex("006400")));
        Iterator voteSubCommands = subCmds.iterator();

        while(voteSubCommands.hasNext()){
            voteHelpMessage[2].getSiblings(getPrettyCommandUsage(voteSubCommands.next().getUsage(sender)));

            if (voteSubCommands.hasNext()){
                voteHelpMessage[2].getSiblings().add(new StringTextComponent("\n"));
            }
        }
        TextUtils.sendMessages(voteHelpMessage, sender.);
    }

    public static ITextComponent getPrettyCommandUsage(String commandUsage){
        String[] usageSplit = commandUsage.split("-");

        ITextComponent cmdInput = new ITextComponent.(usageSplit[0] + "-");
        cmdInput.setStyle(Style.EMPTY.setColor(Color.fromHex("FFD700")));

        ITextComponent cmdInfo = new StringTextComponent(usageSplit[1]);
        cmdInfo.setStyle(Style.EMPTY.setColor(Color.fromHex("023020")));

        return cmdInput.getSiblings().;
    }

    public static void printDebugStrConsole(String... sArgs){
        for(String line : sArgs){
            ForgeVotifier.getLogger().info(line);
        }
    }

    public static void sendMessages(ITextComponent [] msgsToSend, ServerPlayerEntity playerSender){
        for(ITextComponent msg : msgsToSend){
            sendStatusMessage(playerSender, msg, false);
        }
    }

    // Chat msg

    public static void sendChatMessage(ServerPlayerEntity player, IFormattableTextComponent textComponent) {
        sendStatusMessage(player, textComponent, false);
    }

    public static void sendChatMessage(ServerPlayerEntity player, String translationKey, Object... args) {
        sendStatusMessage(player, new TranslationTextComponent(translationKey, args), false);
    }

    private static void sendStatusMessage(ServerPlayerEntity player, IFormattableTextComponent formattableTextComponent, boolean actionBar) {
        player.sendStatusMessage(formattableTextComponent, actionBar);
    }

    private static void sendStatusMessage(ServerPlayerEntity player, ITextComponent textComponent, boolean actionBar) {
        player.sendStatusMessage(textComponent, actionBar);
    }

    public static void sendGlobalMessage(PlayerList players, IFormattableTextComponent textComponent, boolean actionBar) {
        for(int i = 0; i < players.getPlayers().size(); ++i) {
            ServerPlayerEntity player = players.getPlayers().get(i);
            sendStatusMessage(player, textComponent, actionBar);
        }
    }

}