package api.reward;

public class StoredReward {

    public final String username, service, address;
    public final String timestamp;

    public StoredReward(String username, String service, String address, String timestamp) {
        this.username = username;
        this.service = service;
        this.address = address;
        this.timestamp = timestamp;
    }
}
