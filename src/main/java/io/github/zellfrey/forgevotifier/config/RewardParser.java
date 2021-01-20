package io.github.zellfrey.forgevotifier.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.zellfrey.forgevotifier.ForgeVotifier;
import io.github.zellfrey.forgevotifier.api.RewardCreatedEvent;
import io.github.zellfrey.forgevotifier.api.RewardException;
import io.github.zellfrey.forgevotifier.api.reward.Reward;
import io.github.zellfrey.forgevotifier.event.VoteEventHandler;
import io.github.zellfrey.forgevotifier.reward.RewardChance;
import io.github.zellfrey.forgevotifier.reward.RewardDefault;

import net.minecraft.command.CommandBase;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Locale;

public class RewardParser {

    public static File configDir;

    private static JsonParser parser = new JsonParser();


    public static void init(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), "forgevotifier/rewards");
        if(!configDir.exists()) {
            setupDefaultRewards(configDir);
        }
        loadRewardData();
    }

    public static void loadRewardData(){
        ForgeVotifier.getLogger().info("Loading Forge Votifier rewards");
        VoteEventHandler.clearRewards();
        File[] jsonFiles = configDir.listFiles((dir, name) -> name.toLowerCase(Locale.ROOT).endsWith(".json"));
        int regCount = 0;

        if(jsonFiles == null) {
            //if this returns null, something is seriously wrong.
            throw new IllegalStateException("error initializing votifier, could not list files for " + configDir.getAbsolutePath());

        }
        else if(configDir.listFiles().length == 0){
            ForgeVotifier.getLogger().error("There were no files in " + configDir.getAbsolutePath());
        }
        else{

            for(File jsonFile : jsonFiles) {
                try {
                    JsonObject root = parser.parse(new FileReader(jsonFile)).getAsJsonObject();
                    if(!root.has("rewards")) {
                        ForgeVotifier.getLogger().error("cannot parse reward file {}!", jsonFile.getName());
                        continue;
                    }
                    JsonArray rewardArray = root.get("rewards").getAsJsonArray();
                    if(rewardArray.size() == 0){
                        ForgeVotifier.getLogger().info("Votifier has found reward file {}. But there appears to be 0 rewards.\nSkipping file", jsonFile.getName());
                    }
                    else{
                        for(int i = 0; i < rewardArray.size(); i++) {
                            JsonObject object = rewardArray.get(i).getAsJsonObject();
                            //Get default reward parameters
                            String msgRaw = object.has("message") ? object.get("message").getAsString() : "";
                            String commandRaw = object.has("command") ? object.get("command").getAsString() : "";
                            boolean broadcast = object.has("broadcast") ? object.get("broadcast").getAsBoolean() : false;
                            boolean parseAsTellraw = object.has("tellraw") ? object.get("tellraw").getAsBoolean() : false;

                            String type = object.get("type").getAsString();
                            Reward reward;
                            switch(type) {
                                case "default":
                                    reward = new RewardDefault(commandRaw, msgRaw, broadcast, parseAsTellraw);
                                    break;
                                case "chance":
                                    double chance = object.get("chance").getAsDouble();
                                    reward = new RewardChance(commandRaw, msgRaw, broadcast, parseAsTellraw, chance);
                                    break;
                                default: //allow for custom rewards from other mods
                                    RewardCreatedEvent rewardEvent = new RewardCreatedEvent(type, object);
                                    MinecraftForge.EVENT_BUS.post(rewardEvent);
                                    reward = rewardEvent.getRewardResult();
                            }
                            if(reward != null) {
                                VoteEventHandler.addReward(reward);
                                regCount++;
                            }
                            else {
                                ForgeVotifier.getLogger().warn("ignoring unknown votifier reward type: {}", type);
                            }
                        }
                    }
                }
                catch (FileNotFoundException e) {
                    ForgeVotifier.getLogger().error("error parsing reward file " + jsonFile.getName() + "!" + e);
                }

            }
        }
        if(regCount == 0){
            ForgeVotifier.getLogger().error("Forge Votifier found no rewards. Is this intentional?");
        }
        ForgeVotifier.getLogger().info("Forge Votifier registered a total of {} rewards in {} files!", regCount, jsonFiles.length);
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
