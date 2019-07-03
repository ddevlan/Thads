package me.ohvalsgod.thads.baller.gui.armor;

import lombok.AllArgsConstructor;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.armor.AbstractBallerArmor;
import me.ohvalsgod.thads.baller.gui.buttons.armor.BallerArmorEditBuyPriceButton;
import me.ohvalsgod.thads.baller.gui.buttons.armor.BallerArmorEditSellPriceButton;
import me.ohvalsgod.thads.baller.gui.buttons.armor.BallerArmorInformationButton;
import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.menu.Menu;
import me.ohvalsgod.thads.menu.buttons.BackButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class LOLArmorsEditMenu extends Menu {

    private AbstractBallerArmor armor;

    @Override
    public String getTitle(Player player) {
        return Thads.getInstance().getLang().getString("lol.menu.weapons.edit.title").replace("%armor%", trimDisplayName(armor.getArmor()[0].getItemMeta().getDisplayName()));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i = 0; i < 45; i++) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte)15, ""));
        }

        buttons.put(14, new BallerArmorEditBuyPriceButton(armor));
        buttons.put(16, new BallerArmorEditSellPriceButton(armor));
        buttons.put(31, new BallerArmorInformationButton(armor));
        buttons.put(37, new BackButton(new LOLArmorsMenu()));

        return buttons;
    }

    public String trimDisplayName(String string) {

        if (string.length() < 21) {
            return string;
        }

        return string.substring(0, 19) + "...";
    }

}
