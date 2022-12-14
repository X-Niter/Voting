package io.github.zellfrey.forgevotifier;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.zellfrey.forgevotifier.api.reward.RewardStore;
import io.github.zellfrey.forgevotifier.server.commands.impl.CommandForgeVotifier;
import io.github.zellfrey.forgevotifier.server.commands.impl.CommandVote;
import io.github.zellfrey.forgevotifier.server.config.ForgeVotifierConfig;
import io.github.zellfrey.forgevotifier.server.config.RewardParser;
import io.github.zellfrey.forgevotifier.server.util.TextUtils;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = ForgeVotifier.MODID, value = Dist.DEDICATED_SERVER)
public class ForgeVotifier {

    public static final String MODID = "forgevotifier";

    //public static final String NAME = "Forge Votifier";
    //public static final String MCVERSIONS = "[1.16, 1.17)";
    public static final String VERSION = "@VERSION@";
    //public static final String UPDATE_JSON = "@UPDATE_JSON@";

    public static ForgeVotifier instance;

    public static ForgeVotifier getInstance() {
        return instance;
    }

    public static ForgeVotifierConfig config;
    File modConfigDirectory;
    private static final Logger LOGGER = LogManager.getLogger(ForgeVotifier.MODID);

    public static Logger getLogger() {
        return LOGGER;
    }

    public ForgeVotifier() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

        this.modConfigDirectory = event.getModConfigurationDirectory();
        loadConfig();
        RewardParser.init(event);
        RewardStore.getStore();
        TextUtils.init();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("1165Exmaple", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");

        event.registerServerCommand(new CommandForgeVotifier());
        event.registerServerCommand(new CommandVote());

        event.getServer().getCommandManager().getDispatcher().register(LiteralArgumentBuilder.literal(new CommandForgeVotifier()));
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
//    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
//    public static class RegistryEvents {
//        @SubscribeEvent
//        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
//            // register a new block here
//            LOGGER.info("HELLO from Register Block");
//        }
//    }

    public static boolean isOpped(GameProfile uuid){
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        int playerLvl = server.getPlayerList().getOppedPlayers().getEntry(uuid).getPermissionLevel();

        return server.getOpPermissionLevel() == playerLvl;
    }

    public void loadConfig() {
        ForgeVotifier.config = new ForgeVotifierConfig();

        try {
            ForgeVotifier.config.load(new File(this.modConfigDirectory, "forgevotifier"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
