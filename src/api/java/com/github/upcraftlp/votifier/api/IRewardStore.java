package com.github.upcraftlp.votifier.api;

public interface IRewardStore {

    public abstract int getOutStandingRewardCount(String playerNmae);

    public abstract int getMaxStoredRewards();

    public abstract void storePlayerReward(String name, String service, String address, long timestamp);

    public abstract void claimRewards(String name);

}
