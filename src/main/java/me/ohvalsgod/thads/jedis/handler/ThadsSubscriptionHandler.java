package me.ohvalsgod.thads.jedis.handler;

import com.google.gson.JsonObject;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.jedis.JedisSubscriptionHandler;

public class ThadsSubscriptionHandler implements JedisSubscriptionHandler {

    @Override
    public void handleMessage(JsonObject object) {
        ThadsPayload payload;

        try {
            payload = ThadsPayload.valueOf(object.get("payload").getAsString());
        } catch (IllegalArgumentException e) {
            Thads.getInstance().getLogger().warning("Received a payload-type that could not be parsed");
            return;
        }

        JsonObject data = object.get("data").getAsJsonObject();

        switch (payload) {

        }
    }

}
