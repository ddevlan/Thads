package me.ohvalsgod.thads.baller.gui.object;

import lombok.AllArgsConstructor;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.armor.AbstractBallerArmor;
import me.ohvalsgod.thads.baller.gui.buttons.object.BallerObjectEditBuyPriceButton;
import me.ohvalsgod.thads.baller.gui.buttons.object.BallerObjectEditSellPriceButton;
import me.ohvalsgod.thads.baller.gui.buttons.object.BallerObjectInfoButton;
import me.ohvalsgod.thads.baller.gui.buttons.object.BallerObjectToggleButton;
import me.ohvalsgod.thads.baller.gui.buttons.weapons.*;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.baller.object.AbstractBallerObject;
import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.menu.Menu;
import me.ohvalsgod.thads.menu.buttons.BackButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class LOLObjectEditMenu extends Menu {

    private AbstractBallerObject item;

    @Override
    public String getTitle(Player player) {
        if (item instanceof AbstractBallerItem) {
            return Thads.get().getLang().getString("lol.menu.objects.edit.title").replace("%item%", trimDisplayName(((AbstractBallerItem) item).getBallerItemStack().getItemMeta().getDisplayName()));
        } else if (item instanceof AbstractBallerArmor) {
            return Thads.get().getLang().getString("lol.menu.armor.edit.title").replace("%item%", trimDisplayName(((AbstractBallerArmor) item).getBallerArmor()[1].getItemMeta().getDisplayName()));
        }
        return "error";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i = 0; i < 45; i++) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte)15, ""));
        }

        buttons.put(10, new BallerObjectToggleButton(item));
        if (item instanceof AbstractBallerItem && ((AbstractBallerItem) item).getLegendaryItemStack().getType() != Material.BEDROCK) {
            buttons.put(12, new LegendaryItemToggleButton((AbstractBallerItem) item));
        }
        buttons.put(14, new BallerObjectEditBuyPriceButton(item));
        buttons.put(16, new BallerObjectEditSellPriceButton(item));
        buttons.put(31, new BallerObjectInfoButton(item));
        buttons.put(37, new BackButton(new LOLObjectsMenu()));

        return buttons;
    }

    public String trimDisplayName(String string) {

        if (string.length() < 21) {
            return string;
        }

        return string.substring(0, 19) + "...";
    }

}
