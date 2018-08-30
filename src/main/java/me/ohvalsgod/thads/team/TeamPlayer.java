package me.ohvalsgod.thads.team;

import lombok.Getter;
import lombok.Setter;
import me.ohvalsgod.thads.data.PlayerInfo;

import java.util.UUID;

@Getter
public class TeamPlayer extends PlayerInfo {

    @Setter
    private boolean alive;

    public TeamPlayer(UUID uuid, String name) {
        super(uuid, name);
    }

}
