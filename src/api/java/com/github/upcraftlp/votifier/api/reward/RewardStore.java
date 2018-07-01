package com.github.upcraftlp.votifier.api.reward;

import com.github.upcraftlp.votifier.api.IRewardStore;

public class RewardStore {

    @SuppressWarnings("unused")
    private static IRewardStore INSTANCE = null;

    /**
     * get the current reward store
     * there has to be a world loaded already, or this will return null!
     */
    public static IRewardStore getStore() {
        return INSTANCE;
    }

}
