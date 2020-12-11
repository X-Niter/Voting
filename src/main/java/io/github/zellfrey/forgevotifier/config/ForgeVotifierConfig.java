package io.github.zellfrey.forgevotifier.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.io.IOException;
/**
 * @author Beardedflea
 */

public class ForgeVotifierConfig {

    //config variables
    String hostAddress;
    int listenerPort;
    boolean debug, updateCheck, betaUpdates;


    public void load(File configDictionary) throws IOException {
        if (!configDictionary.exists()) {
            configDictionary.mkdir();
        }
        this.loadConfig(configDictionary);
    }

    private void loadConfig(File configDictionary) throws IOException {
        final Configuration configuration = new Configuration(new File(configDictionary, "ForgeVotifier.cfg"));

        loadGeneralConfig("general", configuration);
        loadUpdateCheckConfig("update-checker", configuration);

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    private void loadGeneralConfig(String category, Configuration configuration) {

        String varComment =
                "The port for Votifier to listen on, make sure your server provider allows the port!\n";

        this.listenerPort = configuration.getInt("Listener Port", category, 8192, 0, 65535, varComment);

        varComment =
                "The Server's Host address if different from the address set in the server.properties file.\n" +
                "Leave EMPTY for default value:\n";

        this.hostAddress = configuration.getString("Host Address", category,"0.0.0.0", varComment);

        varComment = "Enable more verbose output of what's going on";

        this.debug = configuration.get(category,"debug mode",false, varComment).getBoolean();
    }

    private void loadUpdateCheckConfig(String category, Configuration configuration){
        String varComment =
                "Whether to announce updates to opped players \nNote: available updates will be logged to console regardless";

        this.updateCheck = configuration.get(category,"Enable Update Checker",false, varComment).getBoolean();

        varComment = "Whether or not to also show beta updates";

        this.betaUpdates = configuration.get(category,"Show Beta Updates",false, varComment).getBoolean();
    }
}


//# Configuration file
//
//
//        # How many rewards a player can receive while offline.
//        # Must be claimed via "/vote claim"
//        # set to 0 to disable
//        # Min: 0
//        # Max: 100
//        I:"Offline Reward Count"=5
//
//        # The text that is shown when a player types /vote
//        # must be formatted in /tellraw nbt format
//        # For better /tellraw visual editing, go to the link: https://minecraft.tools/en/tellraw.php
//        S:"Vote Command"=["",{"text":"--------------------------------------------------","strikethrough":true,"color":"dark_aqua"},{"text":"\n"},{"text":"Vote for us every day for in game rewards and extras","color":"green"},{"text":"\n"},{"text":"--------------------------------------------------","strikethrough":true,"color":"dark_aqua"},{"text":"\n"},{"text":"#1","color":"gold"},{"text":" "},{"text":"Minecraft-MP","color":"yellow","clickEvent":{"action":"open_url","value":"https://minecraft-mp.com/server/227841/vote/"}},{"text":"\n"},{"text":"#2","color":"gold"},{"text":" "},{"text":"FTBServers","color":"yellow","clickEvent":{"action":"open_url","value":"https://ftbservers.com/server/vOi4aL4x/vote"}},{"text":"\n"},{"text":"--------------------------------------------------","strikethrough":true,"color":"dark_aqua"}]
//
//        # Whether or not the /vote command will be available
//        # WARNING: disabling this will also prevent players from claiming their reward
//        B:"Vote Command Enabled"=true
//
//
//        ##########################################################################################################
//        # update-checker
//        #--------------------------------------------------------------------------------------------------------#
//        # Configure the update checker
//        ##########################################################################################################
//
//        update-checker {
//        # Whether to announce updates to opped players
//        # Note: available updates will be logged to console regardless
//        B:"Enable Update Checker"=true
//
//        # Whether or not to also show beta updates
//        B:"Show Beta Updates"=false
//        }
//
//        }
//

