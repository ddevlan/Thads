package me.ohvalsgod.thads.data;

import lombok.Getter;
import lombok.Setter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.cooldown.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class PlayerData extends PlayerInfo {

    @Getter private static Map<UUID, PlayerData> cached = new HashMap<>();

    private Cooldown icebladeCooldown, pianoKeyCooldown;
    private boolean debugMode;

    public PlayerData(UUID uuid) {
        super(uuid, null);
        debugMode = false;
        icebladeCooldown = new Cooldown(0);
        pianoKeyCooldown = new Cooldown(0);

        cached.put(uuid, this);
    }

    public static PlayerData getByUuid(UUID uuid) {
        final PlayerData toReturn = cached.get(uuid);
        return toReturn == null ? new PlayerData(uuid) : toReturn;
    }

    public static PlayerData getByName(String name) {
        final Player target = Bukkit.getPlayer(name);
        final PlayerData playerData;

        if (target == null) {
            UUID uuid = Thads.getInstance().getUuidCache().getUuid(name);
            if (uuid != null) {
                playerData = PlayerData.getByUuid(uuid);
            } else {
                return null;
            }
        } else {
            playerData = PlayerData.getByUuid(target.getUniqueId());
        }
        return playerData;
    }

}
