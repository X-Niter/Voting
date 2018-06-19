package com.github.upcraftlp.votifier.api.reward;

import com.github.upcraftlp.votifier.ForgeVotifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;

public class RewardItem extends Reward {

    private final ItemStack itemStack;
    private final String nbtRaw;

    public RewardItem(Item item, int count, @Nullable String nbtString) {
        itemStack = new ItemStack(item, count);
        nbtRaw = nbtString;
    }

    @Override
    public String getType() {
        return "item";
    }

    @Override
    public void activate(MinecraftServer server, EntityPlayer player, long timestamp, String service, String address) {
        ItemStack ret = itemStack.copy();
        if(ret.hasDisplayName()) ret.setStackDisplayName(replace(ret.getDisplayName(), player, service));
        if(nbtRaw != null) {
            try {
                NBTTagCompound nbt = JsonToNBT.getTagFromJson(replace(nbtRaw, player, service));
                ret.setTagCompound(nbt);
            } catch(NBTException e) {
                ForgeVotifier.getLogger().error("unable to parse NBT string: {}", nbtRaw);
            }
        }
        player.inventory.addItemStackToInventory(ret);
    }
}
