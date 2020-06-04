package com.github.upcraftlp.votifier.config;

import com.github.upcraftlp.votifier.ForgeVotifier;
import api.RewardCreatedEvent;
import api.reward.Reward;
import com.github.upcraftlp.votifier.event.VoteEventHandler;
import com.github.upcraftlp.votifier.reward.RewardChat;
import com.github.upcraftlp.votifier.reward.RewardCommand;
import com.github.upcraftlp.votifier.reward.RewardItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.command.CommandBase;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

public class RewardParser {

    public static File configDir;

    private static JsonParser parser = new JsonParser();

    public static void init(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), "votifier/rewards");
        if(!configDir.exists()) {
            setupDefaultRewards(configDir);
        }
        reloadRewardData();
    }

    public static void reloadRewardData(){
        ForgeVotifier.getLogger().info("Loading Votifier rewards");
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
                            String type = object.get("type").getAsString();
                            Reward reward;
                            switch(type) {
                                case "command":
                                    String commandRaw = object.get("command").getAsString();
                                    reward = new RewardCommand(commandRaw);
                                    break;
                                case "chat":
                                    String msgRaw = object.get("message").getAsString();
                                    boolean broadcast = object.has("broadcast") && object.get("broadcast").getAsBoolean();
                                    boolean parseAsTellraw = object.has("tellraw") && object.get("tellraw").getAsBoolean();
                                    reward = new RewardChat(msgRaw, broadcast, parseAsTellraw);
                                    break;
                                case "item":
                                    Item item = CommandBase.getItemByText(null, object.get("name").getAsString());
                                    int count = object.has("count") ? object.get("count").getAsInt() : 1;
                                    int meta = object.has("damage") ? object.get("damage").getAsInt() : 0;
                                    String nbtRaw = object.has("nbt") ? object.get("nbt").getAsString() : null;
                                    reward = new RewardItem(item, count, meta, nbtRaw);
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
                            else ForgeVotifier.getLogger().warn("ignoring unknown votifier reward type: {}", type);
                        }
                    }
                    
                }
                catch(Exception e) {
                    ForgeVotifier.getLogger().error("error parsing reward file " + jsonFile.getName() + "!", e);
                }
            }
            if(regCount == 0){
                ForgeVotifier.getLogger().error("Votifier found no rewards. Is this intentional?");
            }
            ForgeVotifier.getLogger().info("Votifier registered a total of {} rewards in {} files!", regCount, jsonFiles.length);

        }
    }

    public static void createRewardData(){
        ForgeVotifier.getLogger().info("Creating fresh new default_rewards.json file");
        setupDefaultRewards(configDir);
        reloadRewardData();
    }

    private static void setupDefaultRewards(File rewardsDir) {
        File defaultConfig = new File(rewardsDir, "default_rewards.json");
        try {
            FileUtils.forceMkdir(rewardsDir);
            FileUtils.copyInputStreamToFile(MinecraftServer.class.getClassLoader().getResourceAsStream("assets/" + ForgeVotifier.MODID +"/reward/default_rewards.json"), defaultConfig);
        } catch(IOException e) {
            ForgeVotifier.getLogger().error("Exception setting up the default reward config!", e);
        }
    }
}
