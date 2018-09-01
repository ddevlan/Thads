package me.ohvalsgod.thads.jedis;

import com.google.gson.JsonObject;
import lombok.Getter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.jedis.handler.ThadsPayload;
import me.ohvalsgod.thads.jedis.handler.ThadsSubscriptionHandler;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Getter
public class ThadsJedis {

    private JedisSettings settings;
    private JedisPool pool;
    private JedisPublisher publisher;
    private JedisSubscriber subscriber;

    public ThadsJedis(JedisSettings settings) {
        this.settings = settings;
        this.pool = new JedisPool(this.settings.getAddress(), this.settings.getPort());

        try (Jedis jedis = this.pool.getResource()) {
            if (this.settings.hasPassword()) {
                jedis.auth(this.settings.getPassword());
            }

            this.publisher = new JedisPublisher(this.settings);
            this.subscriber = new JedisSubscriber("basic", this.settings, new ThadsSubscriptionHandler());
        }
    }

    public boolean isActive() {
        return this.pool != null && !this.pool.isClosed();
    }

    public void write(ThadsPayload payload, JsonObject data) {
        JsonObject object = new JsonObject();

        object.addProperty("payload", payload.name());
        object.add("data", data == null ? new JsonObject() : data);

        this.publisher.write("basic", object);
    }

    public <T> T runCommand(RedisCommand<T> redisCommand) {
        Jedis jedis = this.pool.getResource();
        T result = null;

        try {
            result = redisCommand.execute(jedis);
        } catch (Exception e) {
            e.printStackTrace();

            if (jedis != null) {
                this.pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                this.pool.returnResource(jedis);
            }
        }

        return result;
    }

    public static ThadsJedis getInstance() {
        return Thads.getInstance().getThadsJedis();
    }

}
