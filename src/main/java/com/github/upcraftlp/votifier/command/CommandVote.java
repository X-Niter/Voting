package com.github.upcraftlp.votifier.command;

import com.github.upcraftlp.votifier.api.reward.Reward;
import com.github.upcraftlp.votifier.config.VotifierConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;

public class CommandVote extends CommandBase {
    @Override
    public String getName() {
        return "vote";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.votifier.vote.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        sender.sendMessage(ITextComponent.Serializer.jsonToComponent(Reward.replace(VotifierConfig.voteCommand, sender, "")));
    }
}
