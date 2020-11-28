package me.ohvalsgod.thads.dev.redis;

import redis.clients.jedis.Jedis;

public interface RedisCommand<T> {
    T execute(Jedis paramJedis);
}
