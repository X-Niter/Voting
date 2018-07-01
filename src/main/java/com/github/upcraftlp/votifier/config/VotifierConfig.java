package com.github.upcraftlp.votifier.config;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

import static com.github.upcraftlp.votifier.ForgeVotifier.*;

/**
 * @author UpcraftLP
 */
public class VotifierConfig {

    private static Configuration config;

    public static void init(FMLPreInitializationEvent event) {
        config = new Configuration(new File(event.getModConfigurationDirectory(), "votifier/ForgeVotifier.cfg")); //--> /config/votifier/ForgeVotifier.cfg
        config.load();
        syncConfig();
        MinecraftForge.EVENT_BUS.register(new Handler());
    }

    private static void syncConfig() {
        port = config.getInt("port", Configuration.CATEGORY_GENERAL, 8192, 0, 65535, "The port for votifier to listen on");
        host = config.getString("host", Configuration.CATEGORY_GENERAL, "", "The Server's Host address if different from the address set in the server.properties file.");
        voteCommand = config.getString("vote command text", Configuration.CATEGORY_GENERAL, "{\"text\":\"Vote here!\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":[{\"text\":\"Curseforge\",\"color\":\"aqua\"}]},\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://minecraft.curseforge.com/projects/293830\"}}", "the text that is shown when a player types /vote (must be formatted in /tellraw nbt format)");
        voteCommandEnabled = config.getBoolean("vote command enabled", Configuration.CATEGORY_GENERAL, true, "Whether or not the /vote command will be available");
        maxOfflineRewards = config.getInt("maximum offline rewards", Configuration.CATEGORY_GENERAL, 5, 0, 100, "How many rewards a player can receive while offline, must be claimed via \"/vote claim\"");
        config.addCustomCategoryComment("updater", "configure the update checker");
        enableUpdateChecker = config.getBoolean("enable update checker", "updater", true, "whether to announce updates to opped players");
        showBetaUpdates = config.getBoolean("show beta updates", "updater", false, "whether or not to also show beta updates");
        if(config.hasChanged()) config.save();
    }

    public static int port;
    public static String host;
    public static String voteCommand;
    public static boolean voteCommandEnabled;
    public static int maxOfflineRewards;
    public static boolean enableUpdateChecker;
    public static boolean showBetaUpdates;

    public static class Handler {

        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.modID.equals(MODID)) {
                syncConfig();
            }
        }
    }
}
