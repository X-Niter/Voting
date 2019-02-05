package com.github.upcraftlp.votifier;

import com.github.upcraftlp.votifier.command.CommandVote;
import com.github.upcraftlp.votifier.config.RewardParser;
import com.github.upcraftlp.votifier.config.VotifierConfig;
import com.github.upcraftlp.votifier.net.NetworkListenerThread;
import com.github.upcraftlp.votifier.util.ModUpdateHandler;
import com.github.upcraftlp.votifier.util.RSAUtil;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.github.upcraftlp.votifier.ForgeVotifier.DEPENDENCIES;
import static com.github.upcraftlp.votifier.ForgeVotifier.FINGERPRINT_KEY;
import static com.github.upcraftlp.votifier.ForgeVotifier.MCVERSIONS;
import static com.github.upcraftlp.votifier.ForgeVotifier.MODID;
import static com.github.upcraftlp.votifier.ForgeVotifier.MODNAME;
import static com.github.upcraftlp.votifier.ForgeVotifier.UPDATE_JSON;
import static com.github.upcraftlp.votifier.ForgeVotifier.VERSION;

@SuppressWarnings("WeakerAccess")
@Mod(certificateFingerprint = FINGERPRINT_KEY, name = MODNAME, version = VERSION, acceptedMinecraftVersions = MCVERSIONS, modid = MODID, dependencies = DEPENDENCIES, updateJSON = UPDATE_JSON, serverSideOnly = true, acceptableRemoteVersions = "*")
public class ForgeVotifier {

    //Version
    public static final String MCVERSIONS = "[1.12, 1.13)";
    public static final String VERSION = "@VERSION@";

    //Meta Information
    public static final String MODNAME = "Forge Votifier";
    public static final String MODID = "votifier";
    public static final String DEPENDENCIES = "after:thutessentials@[2.2.16,)";
    public static final String UPDATE_JSON = "@UPDATE_JSON@";

    public static final String FINGERPRINT_KEY = "@FINGERPRINTKEY@";
    private static final Logger log = LogManager.getLogger(MODID);
    private static boolean GLASSPANE_LOADED;
    private static boolean THUTESSENTIALS_LOADED;
    private NetworkListenerThread networkListener;

    public static Logger getLogger() {
        return log;
    }

    public static boolean isThutessentialsLoaded() {
        return THUTESSENTIALS_LOADED;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        GLASSPANE_LOADED = Loader.isModLoaded("glasspane");
        THUTESSENTIALS_LOADED = Loader.isModLoaded("thutessentials");
        if(VotifierConfig.updates.enableUpdateChecker) {
            if(isGlasspaneLoaded()) {
                com.github.upcraftlp.glasspane.util.ModUpdateHandler.registerMod(MODID);
            }
        }
        RewardParser.init(event);
        if(isDebugMode()) {
            log.info("initiated vote listeners!");
        }
    }

    public static boolean isGlasspaneLoaded() {
        return GLASSPANE_LOADED;
    }

    public static boolean isDebugMode() {
        return VotifierConfig.debugMode;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        RSAUtil.init();
        if(isDebugMode()) {
            log.info("Loaded Votifier RSA Key!");
        }
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        if(VotifierConfig.voteCommandEnabled) {
            event.registerServerCommand(new CommandVote());
        }
        log.info("starting votifier thread...");
        String address = VotifierConfig.host;
        if(StringUtils.isNullOrEmpty(address)) {
            address = event.getServer().getServerHostname(); //get the server-ip from the server.properties file
        }
        if(StringUtils.isNullOrEmpty(address)) {
            address = "0.0.0.0"; //fallback address
        }
        networkListener = new NetworkListenerThread(address, VotifierConfig.port);
        networkListener.setName("Vote-Listener");
        networkListener.setPriority(Thread.MIN_PRIORITY);
        networkListener.setDaemon(true);
        networkListener.start();
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        ModUpdateHandler.notifyServer();
        if(isDebugMode()) {
            log.info("server started successfully!");
        }
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        log.info("stopping votifier thread...");
        networkListener.shutdown();
    }
}
