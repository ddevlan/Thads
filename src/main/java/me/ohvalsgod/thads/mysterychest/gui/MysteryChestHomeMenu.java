package me.ohvalsgod.thads.mysterychest.gui;

import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.menu.Menu;
import me.ohvalsgod.thads.util.CC;
import org.bukkit.entity.Player;

import java.util.Map;

public class MysteryChestHomeMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return CC.AQUA + "All Mystery Chests";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return null;
    }



}
