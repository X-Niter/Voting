package io.github.zellfrey.forgevotifier.config;

import io.github.zellfrey.forgevotifier.ForgeVotifier;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class RewardParser {

    public static File configDir;

    private static JsonParser parser = new JsonParser();


    public static void init(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), "forgevotifier/rewards");
        if(!configDir.exists()) {
            setupDefaultRewards(configDir);
        }
//        loadRewardData();
    }

    private static void setupDefaultRewards(File rewardsDir) {
        File defaultConfig = new File(rewardsDir, "default_rewards.json");
        try {
            FileUtils.forceMkdir(rewardsDir);
            //noinspection ConstantConditions
            String defaultRewardPath = "assets/" + ForgeVotifier.MODID + "/reward/default_rewards.json";
            InputStream resourceFolder = MinecraftServer.class.getClassLoader().getResourceAsStream(defaultRewardPath);
            FileUtils.copyToFile(resourceFolder, defaultConfig);
        }
        catch (IOException e) {
            ForgeVotifier.getLogger().error("Exception setting up the default reward config!", e);
        }
    }
}
