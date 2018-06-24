package com.github.upcraftlp.votifier.api.reward;

import com.github.upcraftlp.votifier.api.IRewardStore;
import com.github.upcraftlp.votifier.reward.store.RewardStoreWorldSavedData;

public class RewardStore {

    public static IRewardStore getStore() {
        return RewardStoreWorldSavedData.get();
    }

}
