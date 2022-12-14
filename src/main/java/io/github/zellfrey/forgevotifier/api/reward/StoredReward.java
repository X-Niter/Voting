package io.github.zellfrey.forgevotifier.api.reward;


public class StoredReward {

    public final String playerName, service, address, timestamp;

    public StoredReward(String playerName, String service, String address, String timestamp) {
        this.playerName = playerName;
        this.service = service;
        this.address = address;
        this.timestamp = timestamp;
    }
}
