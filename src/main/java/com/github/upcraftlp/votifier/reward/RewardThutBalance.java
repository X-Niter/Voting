package com.github.upcraftlp.votifier.reward;

import com.github.upcraftlp.votifier.ForgeVotifier;
import com.github.upcraftlp.votifier.api.reward.Reward;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import thut.essentials.economy.EconomyManager;

public class RewardThutBalance extends Reward {

    private final int amount;

    public RewardThutBalance(int amount) {
        this.amount = amount;
    }

    @Override
    public String getType() {
        return "thut_pay";
    }

    @Override
    public void activate(MinecraftServer server, EntityPlayer player, String timestamp, String service, String address) {
        if(ForgeVotifier.isThutessentialsLoaded()) {
            EconomyManager.addBalance(player, this.amount);
        }
        else {
            server.sendMessage(new TextComponentString("Thut essentials not loaded!"));
        }
    }
}
