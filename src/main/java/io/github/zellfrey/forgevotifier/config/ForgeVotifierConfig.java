package io.github.zellfrey.forgevotifier.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.io.IOException;
/**
 * @author Beardedflea
 */

public class ForgeVotifierConfig {

    //config variables
    String host, voteCommand;
    int port, maxOfflineRewards;
    boolean debug, updateCheck, betaUpdates;
    private final String defaultVoteCommandVal = "{\"text\":\"Vote here!\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":[{\"text\":\"Curseforge\",\"color\":\"aqua\"}]},\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://minecraft.curseforge.com/projects/293830\"}}";


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
        loadVotingConfig("voting", configuration);

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    private void loadGeneralConfig(String category, Configuration configuration) {

        String varComment =
                "The port for Votifier to listen on, make sure your server provider allows the port!\n";

        this.port = configuration.getInt("Listener Port", category, 8192, 0, 65535, varComment);

        varComment =
                "The Server's Host address if different from the address set in the server.properties file.\n" +
                "Leave EMPTY for default value:\n";

        this.host = configuration.getString("Host Address", category,"0.0.0.0", varComment);

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

    private void loadVotingConfig(String category, Configuration configuration){
        String varComment =
                "The text that is shown when a player types /vote. Must be formatted in /tellraw nbt format\n" +
                "For better /tellraw visual editing, go to the link: https://minecraft.tools/en/tellraw.php";

        this.voteCommand = configuration.get(category,"Vote Command", defaultVoteCommandVal, varComment).getString();

        varComment = "How many rewards a player can receive while offline. Must be claimed via \"/vote claim\"\n"
                    + "Set to 0 to disable";

        this.maxOfflineRewards = configuration.getInt("Offline Reward Count", category, 5, 0, 100, varComment);
    }
}
