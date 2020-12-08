package io.github.zellfrey.forgevotifier;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = ForgeVotifier.MODID,
        name = ForgeVotifier.NAME,
        version = ForgeVotifier.VERSION,
        acceptedMinecraftVersions = ForgeVotifier.MCVERSIONS,
        updateJSON = ForgeVotifier.UPDATE_JSON,
        acceptableRemoteVersions = "*",
        serverSideOnly = true
)
public class ForgeVotifier
{
    public static final String MODID = "forgevotifier";
    public static final String NAME = "Forge Votifier";
    public static final String MCVERSIONS = "[1.12, 1.13)";
    public static final String VERSION = "@VERSION@";
    public static final String UPDATE_JSON = "@UPDATE_JSON@";

    private static final Logger log = LogManager.getLogger(MODID);

    public static Logger getLogger() {
        return log;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        log.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
