package me.ohvalsgod.thads.baller.gui.buttons.weapons;

import lombok.AllArgsConstructor;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.util.CC;
import me.ohvalsgod.thads.util.ItemBuilder;
import me.ohvalsgod.thads.util.NumberUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class BallerItemInformationButton extends Button {

    private AbstractBallerItem item;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(Material.PAPER);

        builder.name(Thads.getInstance().getLang().getString("lol.menu.weapons.edit.information.name").replace("%item%", item.getBallerItemStack().getItemMeta().getDisplayName()));
        for (String string : Thads.getInstance().getLang().getStringList("lol.menu.weapons.edit.information.lore")) {
            String lore = string
                    .replace("%separator%", CC.SCOREBOARD_SEPERATOR_EXTRA)
                    .replace("%enabled%", WordUtils.capitalizeFully(Boolean.toString(item.isEnabled())))
                    .replace("%legendary_enabled%", WordUtils.capitalizeFully(Boolean.toString(item.isLegendaryEnabled())))
                    .replace("%buy_price%", NumberUtil.formatNumber(item.getBuyPrice()))
                    .replace("%sell_price%", NumberUtil.formatNumber(item.getSellPrice()))
                    .replace("%weight%", item.getWeight().toString());

            builder.lore(CC.translate(lore));
        }

        return builder.build();
    }



}
