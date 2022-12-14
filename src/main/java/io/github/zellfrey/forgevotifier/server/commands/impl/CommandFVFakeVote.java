package io.github.zellfrey.forgevotifier.server.commands.impl;

import io.github.zellfrey.forgevotifier.ForgeVotifier;
import io.github.zellfrey.forgevotifier.api.VoteReceivedEvent;
import io.github.zellfrey.forgevotifier.api.reward.StoredPlayer;
import net.minecraft.command.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;

public class CommandFVFakeVote extends CommandSource {
    @Override
    public String getName() {
        return "fakevote";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/forgevotifier fakevote <playerName> - Creates a fake vote for the specified player";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSource sender, String[] args) throws CommandException {

        ServerPlayerEntity playerMP;

        if(args.length >= 1){
                playerMP = server.getPlayerList().getPlayerByUsername(args[0].toLowerCase());
        }else{
            if(!(sender instanceof ServerPlayerEntity)){
                throw new SyntaxErrorException("Executing command in console, need to specify a player");
            }
            playerMP = getCommandSenderAsPlayer(sender);
        }

        sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "Creating fake vote"));

        if(playerMP != null){
            ForgeVotifier.getLogger().info("[{}] received vote from {} (service: {})", "", playerMP.getName(), "FAKE");
            StoredPlayer targetedPlayer = findOrCreateOnlinePlayer(playerMP.getName(), playerMP.getGameProfile().getId().toString());
            targetedPlayer.voteCount++;
            MinecraftForge.EVENT_BUS.post(new VoteReceivedEvent(playerMP, "FAKE", "LOCAL", "NOW"));
            sender.sendMessage(new TextComponentString("Player:" + playerMP.getName() + " has " + targetedPlayer.voteCount + " votes"));

        }else{
            String offlineUsername = args[0].toLowerCase();
//            RewardStore.getStore().storePlayerReward(username, service, address, timestamp);
//            PlayerRewardStore.storePlayerReward(offlineUsername, "FAKE", "LOCAL", "NOW");

        }
    }

    public StoredPlayer findOrCreateOnlinePlayer(String name, String uuid){

        for(StoredPlayer player : StoredPlayer.storedPlayers){
            if(player.username.equals(name)){
                return player;
            }
        }
        StoredPlayer newPlayer = new StoredPlayer(name, uuid);
        StoredPlayer.storedPlayers.add(newPlayer);
        return newPlayer;
    }
}
