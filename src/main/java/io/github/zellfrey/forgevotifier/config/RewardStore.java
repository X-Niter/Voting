/*package io.github.zellfrey.forgevotifier.config;

import com.google.gson.JsonParser;
import io.github.zellfrey.forgevotifier.ForgeVotifier;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class RewardStore {

    public static File configDir;

    private static JsonParser parser = new JsonParser();


    public static void init(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), "forgevotifier/PlayerRewardData");
        if(!configDir.exists()) {
            try {
                FileUtils.forceMkdir(configDir);
            }
            catch (IOException e) {
                ForgeVotifier.getLogger().error("Exception setting up the PlayerRewardData folder!", e);
            }
        }
    }
}
*/