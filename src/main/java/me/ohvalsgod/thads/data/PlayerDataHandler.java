package me.ohvalsgod.thads.data;

import lombok.Getter;
import me.ohvalsgod.thads.Thads;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerDataHandler {

    @Getter private Set<PlayerData> playerDataSet;

    public PlayerDataHandler(Thads thads) {
        playerDataSet = new HashSet<>();
    }

    public PlayerData getPlayerData(UUID uuid) {
        for (PlayerData data : playerDataSet) {
            if (data.getUuid().equals(uuid)) {
                return data;
            }
        }
        return null;
    }

}
