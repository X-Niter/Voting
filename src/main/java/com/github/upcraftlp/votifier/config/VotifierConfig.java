package com.github.upcraftlp.votifier.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.github.upcraftlp.votifier.ForgeVotifier.*;

/**
 * @author UpcraftLP
 */
@Config.LangKey("config.votifier.title")
@Config(modid = MODID, name = "votifier/ForgeVotifier") //--> /config/votifier/ForgeVotifier.cfg
public class VotifierConfig {

    @Config.RangeInt(min = 0, max = 65535)
    @Config.Name("ListenerPort")
    @Config.LangKey("config.votifier.port")
    @Config.Comment({"The port for votifier to listen on,", "make sure your server provider allwos the port!", "Default: 8192"})
    public static int port = 8192;

    @Config.Name("Host Address")
    @Config.LangKey("config.votifier.address")
    @Config.Comment({"The Server's Host address if different from the address set in the server.properties file.", "Leave EMPTY for default value of 0.0.0.0"})
    public static String host = "";

    @Mod.EventBusSubscriber(modid = MODID)
    public static class Handler {

        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(MODID)) {
                ConfigManager.load(MODID, Config.Type.INSTANCE);
            }
        }
    }
}
