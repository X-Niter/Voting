package com.github.upcraftlp.votifier.api.reward;

public class StoredReward {

    public final String username, service, address, timestamp;

    public StoredReward(String username, String service, String address, String timestamp) {
        this.username = username;
        this.service = service;
        this.address = address;
        this.timestamp = timestamp;
    }
}
