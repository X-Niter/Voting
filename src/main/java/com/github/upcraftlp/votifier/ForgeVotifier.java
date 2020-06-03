package com.github.upcraftlp.votifier;

import api.reward.RewardStore;
import com.github.upcraftlp.votifier.command.CommandVote;
import com.github.upcraftlp.votifier.config.RewardParser;
import com.github.upcraftlp.votifier.config.VotifierConfig;
import com.github.upcraftlp.votifier.event.VoteEventHandler;
import com.github.upcraftlp.votifier.net.NetworkListenerThread;
import com.github.upcraftlp.votifier.reward.store.RewardStoreWorldSavedData;
import com.github.upcraftlp.votifier.util.RSAUtil;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.MinecraftForge;
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
        acceptableRemoteVersions = "*"
)
public class ForgeVotifier {

    //Version
    public static final String MCVERSIONS = "1.7.10";
    public static final String VERSION = "@VERSION@";

    //Meta Information
    public static final String MODNAME = "Forge Votifier";
    public static final String MODID = "votifier";
    public static final String DEPENDENCIES = "";

    public static final String FINGERPRINT_KEY = "@FINGERPRINTKEY@";
    private NetworkListenerThread networkListener;

    private static final Logger log = LogManager.getLogger(MODID);
    private static boolean debugMode = false;

    public static Logger getLogger() {
        return log;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if(!event.getSide().isServer()) {
            FMLLog.bigWarning("FORGE VOTIFIER IS SERVERSIDE ONLY, ABORTING LAUNCH!");
            FMLCommonHandler.instance().exitJava(1, false);
        }
        log.info("Votifier - Forge Version: Flea Fork");
        VotifierConfig.init(event);
        MinecraftForge.EVENT_BUS.register(new VoteEventHandler());
        RewardParser.init(event);
        if(isDebugMode()) log.info("initiated vote listeners!");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        RSAUtil.init();
        if(isDebugMode()) log.info("Loaded Votifier RSA Key!");
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        if(VotifierConfig.voteCommandEnabled) event.registerServerCommand(new CommandVote());
        log.info("starting votifier thread...");
        String address = VotifierConfig.host;
        if(StringUtils.isNullOrEmpty(address)) address = event.getServer().getServerHostname(); //get the server-ip from the server.properties file
        if(StringUtils.isNullOrEmpty(address)) address = "0.0.0.0"; //fallback address
        networkListener = new NetworkListenerThread(address, VotifierConfig.port);
        networkListener.setName("Vote-Listener");
        networkListener.setPriority(Thread.MIN_PRIORITY);
        networkListener.setDaemon(true);
        networkListener.start();
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        ReflectionHelper.setPrivateValue(RewardStore.class, null, RewardStoreWorldSavedData.get(), "INSTANCE");
        if(isDebugMode()) log.info("server started successfully!");
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        log.info("stopping votifier thread...");
        networkListener.shutdown();
    }
}
