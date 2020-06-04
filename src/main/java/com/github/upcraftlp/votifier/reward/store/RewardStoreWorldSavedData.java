package com.github.upcraftlp.votifier.reward.store;

import com.github.upcraftlp.votifier.ForgeVotifier;
import api.IRewardStore;
import api.VoteReceivedEvent;
import api.reward.StoredReward;
import com.github.upcraftlp.votifier.config.VotifierConfig;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RewardStoreWorldSavedData extends WorldSavedData implements IRewardStore {

    private static final String DATA_NAME = ForgeVotifier.MODID + "_reward_data";
    private final Map<String, List<StoredReward>> STORED_REWARDS = Maps.newHashMap();

    public RewardStoreWorldSavedData() {
        this(DATA_NAME);
    }

    public RewardStoreWorldSavedData(String name) {
        super(name);
    }

    public static RewardStoreWorldSavedData get() {
        return get(MinecraftServer.getServer().getEntityWorld());
    }

    public static RewardStoreWorldSavedData get(World world) {
        MapStorage storage = world.mapStorage;
        RewardStoreWorldSavedData instance = (RewardStoreWorldSavedData) storage.loadData(RewardStoreWorldSavedData.class, DATA_NAME);
        if(instance == null) {
            instance = new RewardStoreWorldSavedData();
            storage.setData(DATA_NAME, instance);
        }
        return instance;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList(DATA_NAME, Constants.NBT.TAG_COMPOUND);
        STORED_REWARDS.clear();
        for(int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound compound = list.getCompoundTagAt(i);
            String playerName = compound.getString("player");
            NBTTagList rewardList = compound.getTagList("rewards", Constants.NBT.TAG_COMPOUND);
            List<StoredReward> rewards = new ArrayList<>();
            for(int j = 0; j < rewardList.tagCount(); j++) {
                NBTTagCompound rewardTag = rewardList.getCompoundTagAt(i);
                String service = rewardTag.getString("service");
                String address = rewardTag.getString("address");
                String timestamp = rewardTag.getString("timestamp");
                rewards.add(new StoredReward(playerName, service, address, timestamp));
            }
            STORED_REWARDS.put(playerName, rewards);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();
        for(String playerName : STORED_REWARDS.keySet()) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("player", playerName);
            List<StoredReward> rewards = STORED_REWARDS.get(playerName);
            if(rewards == null) continue;
            NBTTagList playerRewardList = new NBTTagList();
            for(StoredReward reward : rewards) {
                NBTTagCompound rewardTag = new NBTTagCompound();
                rewardTag.setString("service", reward.service);
                rewardTag.setString("address", reward.address);
                rewardTag.setString("timestamp", reward.timestamp);
                playerRewardList.appendTag(rewardTag);
            }
            nbt.setTag("rewards", playerRewardList);
            list.appendTag(nbt);
        }
        compound.setTag(DATA_NAME, list);
    }

    @Override
    public int getOutStandingRewardCount(String playerName) {
        return Math.min(getRewardsForPlayer(playerName).size(), getMaxStoredRewards());
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
    public void claimRewards(String name) {
        EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(name);
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