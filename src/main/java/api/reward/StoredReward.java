package api.reward;

public class StoredReward {

    public final String username, service, address;
    public final long timestamp;

    public StoredReward(String username, String service, String address, long timestamp) {
        this.username = username;
        this.service = service;
        this.address = address;
        this.timestamp = timestamp;
    }
}
