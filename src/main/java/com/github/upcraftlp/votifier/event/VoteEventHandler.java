package com.github.upcraftlp.votifier.event;

import com.github.upcraftlp.votifier.ForgeVotifier;
import com.github.upcraftlp.votifier.api.VoteReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.io.IOException;

@Mod.EventBusSubscriber(value = Side.SERVER, modid = ForgeVotifier.MODID)
public class VoteEventHandler {

    public static void init(FMLPreInitializationEvent event) {
        File configDir = new File(event.getModConfigurationDirectory(), "votifier/rewards.json");
        if(!configDir.exists()) {
            try {
                configDir.createNewFile();
                //TODO read reward config!
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public static void voteMade(VoteReceivedEvent event) {
        //TODO handle votes
    }
}
