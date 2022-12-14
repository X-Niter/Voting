package io.github.zellfrey.forgevotifier.api;

import com.google.gson.JsonObject;
import io.github.zellfrey.forgevotifier.server.reward.Reward;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nullable;

/**
 * Original author: UpcraftLP
 */
public class RewardCreatedEvent extends Event {

    private final String type;
    private final JsonObject json;
    private Reward result = null;

    /**
     * fired on the {@link MinecraftForge#EVENT_BUS} when an unknown Reward is to be parsed
     *
     * @param json JsonObject
     * @param type String
     */
    public RewardCreatedEvent(String type, JsonObject json) {
        this.type = type;
        this.json = json;
    }

    /**
     * Returns {@link RewardCreatedEvent#json}
     */
    public JsonObject getJson() {
        return json;
    }

    /**
     * Returns {@link RewardCreatedEvent#type}
     */
    public String getRewardType() {
        return type;
    }

    @Nullable
    public Reward getRewardResult() {
        return result;
    }

    public void setRewardResult(Reward result) {
        this.result = result;
    }
}
