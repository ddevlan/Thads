package me.ohvalsgod.thads.baller.gui.armor;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.gui.buttons.weapons.BallerItemDisplayButton;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.menu.pagination.PaginatedMenu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class LOLArmorsMenu extends PaginatedMenu {

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        return buttons;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return Thads.getInstance().getLang().getString("lol.menu.weapons.title");
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (AbstractBallerItem item : BallerManager.getBallerManager().getBallerItems()) {
            buttons.put(buttons.size(), new BallerItemDisplayButton(item));
        }

        return buttons;
    }

}
