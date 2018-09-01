package me.ohvalsgod.thads.jedis;

import com.google.gson.JsonObject;

public interface JedisSubscriptionHandler {

    void handleMessage(JsonObject json);

}
