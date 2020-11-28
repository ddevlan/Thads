package me.ohvalsgod.thads.baller.gui.object;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.armor.AbstractBallerArmor;
import me.ohvalsgod.thads.baller.gui.buttons.LOLItemsInfoButton;
import me.ohvalsgod.thads.baller.gui.buttons.object.BallerObjectButton;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.baller.object.AbstractBallerObject;
import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.menu.pagination.FilterablePaginatedMenu;
import me.ohvalsgod.thads.menu.pagination.PageFilter;
import org.bukkit.entity.Player;

import java.util.*;

public class LOLObjectsMenu extends FilterablePaginatedMenu<AbstractBallerObject> {

    @Override
    public String getPrePaginatedTitle(Player player) {
        return Thads.get().getLang().getString("lol.menu.objects.title");
    }

    @Override
    public Map<Integer, Button> getFilteredButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        obj:
        for (AbstractBallerObject object : Thads.get().getBallerManager().getBallerObjects()) {
            for (PageFilter<AbstractBallerObject> filter : getFilters()) {
                if (!filter.test(object)) {
                    continue obj;
                }
            }
            buttons.put(buttons.size(), new BallerObjectButton(object));
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = super.getGlobalButtons(player);

        buttons.put(4, new LOLItemsInfoButton(getFilteredButtons(player).size()));

        return buttons;
    }

    @Override
    public List<PageFilter<AbstractBallerObject>> generateFilters() {
        List<PageFilter<AbstractBallerObject>> filters = new ArrayList<>();

        filters.add(new PageFilter<>("Show: Items", abstractBallerObject -> !(abstractBallerObject instanceof AbstractBallerItem)));
        filters.add(new PageFilter<>("Show: Armor", abstractBallerObject -> !(abstractBallerObject instanceof AbstractBallerArmor)));

        return filters;
    }

}
