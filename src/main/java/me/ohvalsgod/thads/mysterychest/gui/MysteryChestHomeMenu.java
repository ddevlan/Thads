package me.ohvalsgod.thads.mysterychest.gui;

import com.ddylan.library.menu.Button;
import com.ddylan.library.menu.Menu;
import lombok.val;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.mysterychest.MysteryChest;
import me.ohvalsgod.thads.util.CC;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MysteryChestHomeMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return CC.AQUA + "All Mystery Chests";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        val buttons = new HashMap<Integer, com.ddylan.library.menu.Button>();

        for (MysteryChest chest : Thads.get().getMysteryChestManager().getMysteryChests()) {
            buttons.put(buttons.size(), new MysteryChestDisplayButton(chest));
        }

        return buttons;
    }



}
