package me.ohvalsgod.thads.baller.gui.weapons;

import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PianoKeyMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return ChatColor.AQUA + "Piano Key Song Selector";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();



        return buttons;
    }
}
