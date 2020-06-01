package me.ohvalsgod.thads.kits.gui;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.kits.Kit;
import me.ohvalsgod.thads.kits.gui.buttons.KitDisplayButton;
import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.menu.pagination.PaginatedMenu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class KitSelectorMenu extends PaginatedMenu {

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        return buttons;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return Thads.get().getLang().getString("kits.menu.title");
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int i = 0;
        for (Kit kit : Kit.getSorted()) {
            buttons.put(i, new KitDisplayButton(kit));
            i++;
        }

        return buttons;
    }


}
