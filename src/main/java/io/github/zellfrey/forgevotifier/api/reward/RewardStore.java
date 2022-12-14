package io.github.zellfrey.forgevotifier.api.reward;

//import com.github.upcraftlp.votifier.reward.store.RewardStoreWorldSavedData;

import io.github.zellfrey.forgevotifier.api.IRewardStore;
import io.github.zellfrey.forgevotifier.server.reward.datastore.RewardDataStore;

import javax.annotation.Nullable;

public class RewardStore {

    /**
     * get the current reward store
     * there has to be a world loaded already, or this will return null!
     */
    public static IRewardStore getStore() {
        return RewardDataStore.get();
    }
}
