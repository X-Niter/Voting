package com.github.upcraftlp.votifier;

import com.github.upcraftlp.votifier.config.VotifierConfig;
import com.github.upcraftlp.votifier.event.VoteEventHandler;
import com.github.upcraftlp.votifier.net.NetworkListenerThread;
import com.github.upcraftlp.votifier.util.RSAUtil;
import core.upcraftlp.craftdev.api.util.ModHelper;
import core.upcraftlp.craftdev.api.util.UpdateChecker;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.github.upcraftlp.votifier.ForgeVotifier.*;

@SuppressWarnings("WeakerAccess")
@Mod(
        certificateFingerprint = FINGERPRINT_KEY,
        name = MODNAME,
        version = VERSION,
        acceptedMinecraftVersions = MCVERSIONS,
        modid = MODID,
        dependencies = DEPENDENCIES,
        updateJSON = UPDATE_JSON,
        serverSideOnly = true,
        acceptableRemoteVersions = "*"
)
public class ForgeVotifier {

    //Version
    public static final String MCVERSIONS = "[1.12.2, 1.13)";
    public static final String VERSION = "@VERSION@";

    //Meta Information
    public static final String MODNAME = "Forge Votifier";
    public static final String MODID = "votifier";
    public static final String DEPENDENCIES = "";
    public static final String UPDATE_JSON = "@UPDATE_JSON@";

    public static final String FINGERPRINT_KEY = "@FINGERPRINTKEY@";
    private NetworkListenerThread networkListener;

    private static final Logger log = LogManager.getLogger(MODID);
    public static boolean debugMode = false;

    public static Logger getLogger() {
        return log;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if(Loader.isModLoaded("craftdev-core")) {
            UpdateChecker.registerMod(MODID);
            debugMode = ModHelper.isDebugMode();
        }
        VoteEventHandler.init(event);
        if(debugMode) log.info("initiated vote listeners!");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        RSAUtil.init();
        if(debugMode) log.info("Loaded Votifier RSA Key!");
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        log.info("starting votifier thread...");
        String address = VotifierConfig.host;
        if(StringUtils.isNullOrEmpty(address)) address = event.getServer().getServerHostname(); //get the server-ip from the server.properties file
        if(StringUtils.isNullOrEmpty(address)) address = "0.0.0.0"; //fallback address
        networkListener = new NetworkListenerThread(address, VotifierConfig.port);
        networkListener.start();
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        log.info("stopping votifier thread...");
        networkListener.shutdown();
    }
}
