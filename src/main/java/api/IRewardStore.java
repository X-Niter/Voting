package api;

public interface IRewardStore {

    int getOutStandingRewardCount(String playerNmae);

    int getMaxStoredRewards();

    void storePlayerReward(String name, String service, String address, String timestamp);

    void claimRewards(String name);

}
