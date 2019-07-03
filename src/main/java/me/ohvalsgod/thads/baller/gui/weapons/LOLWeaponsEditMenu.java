package me.ohvalsgod.thads.baller.gui.weapons;

import lombok.AllArgsConstructor;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.gui.buttons.weapons.*;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.menu.Menu;
import me.ohvalsgod.thads.menu.buttons.BackButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class LOLWeaponsEditMenu extends Menu {

    private AbstractBallerItem item;

    @Override
    public String getTitle(Player player) {
        return Thads.getInstance().getLang().getString("lol.menu.weapons.edit.title").replace("%item%", trimDisplayName(item.getBallerItemStack().getItemMeta().getDisplayName()));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i = 0; i < 45; i++) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte)15, ""));
        }

        buttons.put(10, new BallerItemToggleButton(item));
        buttons.put(12, new LegendaryItemToggleButton(item));
        buttons.put(14, new BallerItemEditBuyPriceButton(item));
        buttons.put(16, new BallerItemEditSellPriceButton(item));
        buttons.put(31, new BallerItemInformationButton(item));
        buttons.put(37, new BackButton(new LOLWeaponsMenu()));

        return buttons;
    }

    public String trimDisplayName(String string) {

        if (string.length() < 21) {
            return string;
        }

        return string.substring(0, 19) + "...";
    }

}
