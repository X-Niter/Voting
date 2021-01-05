package io.github.zellfrey.forgevotifier;

import io.github.zellfrey.forgevotifier.command.*;
import io.github.zellfrey.forgevotifier.config.*;

import io.github.zellfrey.forgevotifier.util.TextUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.IOException;

@Mod(
        modid = ForgeVotifier.MODID,
        name = ForgeVotifier.NAME,
        version = ForgeVotifier.VERSION,
        acceptedMinecraftVersions = ForgeVotifier.MCVERSIONS,
        updateJSON = ForgeVotifier.UPDATE_JSON,
        acceptableRemoteVersions = "*",
        serverSideOnly = true
)
public class ForgeVotifier {

    public static final String MODID = "forgevotifier";
    public static final String NAME = "Forge Votifier";
    public static final String MCVERSIONS = "[1.12, 1.13)";
    public static final String VERSION = "@VERSION@";
    public static final String UPDATE_JSON = "@UPDATE_JSON@";
    @Mod.Instance(MODID)
    public static ForgeVotifier instance;
    public static ForgeVotifierConfig config;
    File modConfigDictionary;

    private static final Logger log = LogManager.getLogger(MODID);

    public static Logger getLogger() {
        return log;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        this.modConfigDictionary = event.getModConfigurationDirectory();

        loadConfig();
        TextUtils.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {

    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandForgeVotifier());
        event.registerServerCommand(new CommandVote());
    }

    public void loadConfig() {
        ForgeVotifier.config = new ForgeVotifierConfig();
        try {
            ForgeVotifier.config.load(new File(this.modConfigDictionary, "forgevotifier"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
