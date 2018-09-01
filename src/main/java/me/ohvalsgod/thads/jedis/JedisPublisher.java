package me.ohvalsgod.thads.jedis;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import me.ohvalsgod.thads.Thads;
import org.apache.commons.lang3.Validate;

@RequiredArgsConstructor
public class JedisPublisher {

	private final JedisSettings jedisSettings;

	public void write(String channel, JsonObject payload) {
		Validate.notNull(Thads.getInstance().getThadsJedis().getPool());

		Thads.getInstance().getThadsJedis().runCommand(redis -> {
			if (jedisSettings.hasPassword()) {
				redis.auth(jedisSettings.getPassword());
			}

			redis.publish(channel, payload.toString());

			return null;
		});
	}

}
