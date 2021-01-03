package io.github.zellfrey.forgevotifier.api;

public interface IRewardStore {

    int getOutStandingRewardCount(String playerName);

    int getPlayerVoteCount(String playerName);

    int getMaxStoredRewards();

    void storePlayerReward(String name, String service, String address, String timestamp);

    void claimRewards(String name);
}
