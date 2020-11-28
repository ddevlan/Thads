package me.ohvalsgod.thads.dev.visibility;


import org.bukkit.entity.Player;

public interface OverrideHandler {
    OverrideAction getAction(Player paramPlayer1, Player paramPlayer2);
}
