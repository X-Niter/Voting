package io.github.zellfrey.forgevotifier.server.reward.datastore;

import com.google.common.collect.Maps;
import io.github.zellfrey.forgevotifier.ForgeVotifier;
import io.github.zellfrey.forgevotifier.api.IRewardStore;
import io.github.zellfrey.forgevotifier.api.VoteReceivedEvent;
import io.github.zellfrey.forgevotifier.api.reward.StoredReward;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RewardDataStore extends WorldSavedData implements IRewardStore {

    private static final String DATA_NAME = ForgeVotifier.MODID + "_reward_data";
    private final Map<String, List<StoredReward>> STORED_REWARDS = Maps.newHashMap();

    public RewardDataStore() {
        this(DATA_NAME);
    }

    public RewardDataStore(String name) {
        super(name);
    }

    public static RewardDataStore get() {
        //Minecraft.getInstance().player.getCommandSenderWorld();
        return get(Minecraft.getInstance().world.getServer().getWorld(World.OVERWORLD));
    }

    public static RewardDataStore get(World world) {
        if (!(world instanceof ServerWorld))
        {
            throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
        }

        ServerWorld overworld = world.getServer().getWorld(World.OVERWORLD);

        DimensionSavedDataManager storage = overworld.getSavedData();

        return storage.getOrCreate(RewardDataStore::new, DATA_NAME);
    }

    @Override
    public void read(CompoundNBT nbt) {
        ListNBT list = nbt.getList(DATA_NAME, Constants.NBT.TAG_COMPOUND);
        STORED_REWARDS.clear();
        for(int i = 0; i < list.size(); i++) {
            CompoundNBT compound = list.getCompound(i);
            String playerName = compound.getString("player");
            ListNBT rewardList = compound.getList("rewards", Constants.NBT.TAG_COMPOUND);
            List<StoredReward> rewards = new ArrayList<>();
            for(int j = 0; j < rewardList.size(); j++) {
                CompoundNBT rewardTag = rewardList.getCompound(i);
                String service = rewardTag.getString("service");
                String address = rewardTag.getString("address");
                String timestamp = rewardTag.getString("timestamp");
                rewards.add(new StoredReward(playerName, service, address, timestamp));
            }
            STORED_REWARDS.put(playerName, rewards);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT list = new ListNBT();
        for(String playerName : STORED_REWARDS.keySet()) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("player", playerName);
            List<StoredReward> rewards = STORED_REWARDS.get(playerName);
            if(rewards == null) continue;
            ListNBT playerRewardList = new ListNBT();
            for(StoredReward reward : rewards) {
                CompoundNBT rewardTag = new CompoundNBT();
                rewardTag.putString("service", reward.service);
                rewardTag.putString("address", reward.address);
                rewardTag.putString("timestamp", reward.timestamp);
                playerRewardList.add(rewardTag);
            }
            nbt.put("rewards", playerRewardList);
            list.add(nbt);
        }
        compound.put(DATA_NAME, list);
        return compound;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getOutStandingRewardCount(String playerName) {
        return Math.min(getRewardsForPlayer(playerName).size(), getMaxStoredRewards());
    }

    @Override
    public int getPlayerVoteCount(String playerName) {
        return 0;
    }

    @Override
    public int getMaxStoredRewards() {
        return VotifierConfig.maxOfflineRewards;
    }

    @Override
    public void storePlayerReward(String name, String service, String address, String timestamp) {
        if(getMaxStoredRewards() == 0) return; //do not store anything
        ForgeVotifier.getLogger().debug("cannot find player {}, assuming they're offline and storing reward.", name);
        List<StoredReward> rewards = getRewardsForPlayer(name);
        while(rewards.size() > getMaxStoredRewards()) {
            rewards.remove(0);
        }
        rewards.add(new StoredReward(name, service, address, timestamp));
        this.markDirty();
    }

    private List<StoredReward> getRewardsForPlayer(String name) {
        name = name.toLowerCase(Locale.ROOT);
        if(!STORED_REWARDS.containsKey(name)) STORED_REWARDS.put(name, new ArrayList<>());
        return STORED_REWARDS.get(name);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void claimRewards(String name) {
        ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUsername(name);
        List<StoredReward> rewards = getRewardsForPlayer(name);
        if(player != null) {
            for(int i = 0; i < Math.min(getMaxStoredRewards(), rewards.size()); i++) {
                StoredReward reward = rewards.get(i);
                MinecraftForge.EVENT_BUS.post(new VoteReceivedEvent(player, reward.service, reward.address, reward.timestamp));
            }
        }
        ForgeVotifier.getLogger().debug("player {} claimed their {} outstanding rewards", name, rewards.size());
        rewards.clear();
        this.markDirty();
    }
}