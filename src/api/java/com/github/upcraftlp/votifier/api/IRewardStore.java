package com.github.upcraftlp.votifier.api;

public interface IRewardStore {

    int getOutStandingRewardCount(String playerNmae);

    int getMaxStoredRewards();

    void storePlayerReward(String name, String service, String address, long timestamp);

    void claimRewards(String name);

}
