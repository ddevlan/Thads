package me.ohvalsgod.thads.baller.gui.object;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.armor.AbstractBallerArmor;
import me.ohvalsgod.thads.baller.gui.buttons.LOLItemsInfoButton;
import me.ohvalsgod.thads.baller.gui.buttons.object.BallerObjectButton;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.baller.object.AbstractBallerObject;
import me.ohvalsgod.thads.baller.object.IBallerObject;
import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.menu.pagination.FilterablePaginatedMenu;
import me.ohvalsgod.thads.menu.pagination.PageFilter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class LOLObjectsMenu extends FilterablePaginatedMenu<LOLFilterType> {

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(4, new LOLItemsInfoButton(getFilteredButtons(player).size()));

        return buttons;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return Thads.get().getLang().getString("lol.menu.objects.title");
    }

    @Override
    public Map<Integer, Button> getFilteredButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Set<IBallerObject> ballerObjects = Thads.get().getBallerManager().getBallerObjects();
        List<IBallerObject> sorted = new ArrayList<>();

        switch(LOLFilterType.getByDisplay(getFilters().get(getScrollIndex()).getName().replace("Type: ", ""))) {
            case WEIGHT: {
                sorted.addAll(ballerObjects);
            }
            case ABC: {
                List<String> names = ballerObjects.stream().map(IBallerObject::getName).sorted().collect(Collectors.toList());
                for (String string : names) {
                    sorted.add(Thads.get().getBallerManager().getBallerObjectByName(string));
                }
            }
            case XYZ: {
                List<String> names = ballerObjects.stream().map(IBallerObject::getName).sorted().collect(Collectors.toList());
                Collections.reverse(names);
                for (String string : names) {
                    sorted.add(Thads.get().getBallerManager().getBallerObjectByName(string));
                }
            }
            case VALUE_UP: {
                sorted.addAll(ballerObjects.stream().sorted(Comparator.comparingDouble(IBallerObject::getBuyPrice)).collect(Collectors.toList()));
            }
            case VALUE_DOWN: {
                sorted.addAll(ballerObjects.stream().sorted(Comparator.comparingDouble(IBallerObject::getBuyPrice).reversed()).collect(Collectors.toList()));
            }
            case ALL_ARMOR: {
                List<IBallerObject> toAdd = new ArrayList<>();
                for (IBallerObject ballerObject : ballerObjects) {
                    if (ballerObject instanceof AbstractBallerArmor) {
                        toAdd.add(ballerObject);
                    }
                }
                sorted.addAll(toAdd);
            }
            case ALL_ITEMS: {
                List<IBallerObject> toAdd = new ArrayList<>();
                for (IBallerObject ballerObject : ballerObjects) {
                    if (ballerObject instanceof AbstractBallerItem) {
                        toAdd.add(ballerObject);
                    }
                }
                sorted.addAll(toAdd);
            }
        }

        sorted.forEach(ballerObject -> buttons.put(buttons.size(), new BallerObjectButton((AbstractBallerObject) ballerObject)));

        return buttons;
    }

    @Override
    public List<PageFilter<LOLFilterType>> generateFilters() {
        List<PageFilter<LOLFilterType>> filters = new ArrayList<>();

        for (LOLFilterType type : LOLFilterType.values()) {
            filters.add(new PageFilter<>(("Type: " + type.getDisplay()), lolFilterType -> {
                if (lolFilterType == type) {
                    return true;
                } else {
                    return false;
                }
            }));
        }

        return filters;
    }
}
