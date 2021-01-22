package io.github.zellfrey.forgevotifier.api.reward;


public class StoredReward {

    public final String service, address, timestamp;

    public StoredReward(String service, String address, String timestamp) {
        this.service = service;
        this.address = address;
        this.timestamp = timestamp;
    }
}
