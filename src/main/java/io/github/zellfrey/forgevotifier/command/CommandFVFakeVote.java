package io.github.zellfrey.forgevotifier.command;

import io.github.zellfrey.forgevotifier.ForgeVotifier;
import io.github.zellfrey.forgevotifier.api.VoteReceivedEvent;
import io.github.zellfrey.forgevotifier.api.reward.StoredReward;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;

import io.github.zellfrey.forgevotifier.api.reward.StoredPlayer;

import javax.annotation.Nullable;

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

        EntityPlayerMP playerMP;

        if(args.length >= 1){
                playerMP = server.getPlayerList().getPlayerByUsername(args[0].toLowerCase());
        }else{
            if(!(sender instanceof EntityPlayerMP)){
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
