package com.github.upcraftlp.votifier.api.reward;

import com.github.upcraftlp.votifier.api.IRewardStore;
import com.github.upcraftlp.votifier.reward.store.RewardStoreWorldSavedData;

import javax.annotation.Nullable;

public class RewardStore {

    /**
     * get the current reward store
     * there has to be a world loaded already, or this will return null!
     */
    @Nullable
    public static IRewardStore getStore() {
        return RewardStoreWorldSavedData.get();
    }
}
