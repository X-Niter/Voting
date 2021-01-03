package io.github.zellfrey.forgevotifier.api;

import io.github.zellfrey.forgevotifier.api.reward.Reward;
import com.google.gson.JsonObject;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

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
     * @param json
     * @param type
     */
    public RewardCreatedEvent(String type, JsonObject json) {
        this.type = type;
        this.json = json;
    }

    public JsonObject getJson() {
        return json;
    }

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
