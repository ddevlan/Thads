package me.ohvalsgod.thads.baller.gui.buttons.object;

import lombok.AllArgsConstructor;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.armor.AbstractBallerArmor;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.baller.object.AbstractBallerObject;
import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.util.CC;
import me.ohvalsgod.thads.util.ItemBuilder;
import me.ohvalsgod.thads.util.NumberUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class BallerObjectInfoButton extends Button {

    private AbstractBallerObject item;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (item instanceof AbstractBallerItem) {
            AbstractBallerItem ballerItem = (AbstractBallerItem) item;
            ItemBuilder builder = new ItemBuilder(Material.PAPER);

            builder.name(Thads.get().getLang().getString("lol.menu.objects.edit.information.name").replace("%item%", ballerItem.getBallerItemStack().getItemMeta().getDisplayName()));
            for (String string : Thads.get().getLang().getStringList("lol.menu.objects.edit.information.lore")) {
                String lore = string
                        .replace("%separator%", CC.SCOREBOARD_SEPERATOR_EXTRA)
                        .replace("%enabled%", WordUtils.capitalizeFully(Boolean.toString(item.isEnabled())))
                        .replace("%legendary_enabled%", WordUtils.capitalizeFully(Boolean.toString(ballerItem.isLegendaryEnabled())))
                        .replace("%buy_price%", NumberUtil.formatNumber(item.getBuyPrice()))
                        .replace("%sell_price%", NumberUtil.formatNumber(item.getSellPrice()))
                        .replace("%weight%", item.getWeight().toString());

                builder.lore(CC.translate(lore));
            }
        } else {
            AbstractBallerArmor armor = (AbstractBallerArmor) item;
            ItemBuilder builder = new ItemBuilder(Material.PAPER);

            builder.name(Thads.get().getLang().getString("lol.menu.armor.edit.information.name").replace("%armor%", armor.getBallerArmor()[0].getItemMeta().getDisplayName()));
            for (String string : Thads.get().getLang().getStringList("lol.menu.armor.edit.information.lore")) {
                String lore = string
                        .replace("%separator%", CC.SCOREBOARD_SEPERATOR_EXTRA)
                        .replace("%enabled%", WordUtils.capitalizeFully(Boolean.toString(armor.isEnabled())))
                        .replace("%buy_price%", NumberUtil.formatNumber(item.getBuyPrice()))
                        .replace("%sell_price%", NumberUtil.formatNumber(item.getSellPrice()))
                        .replace("%weight%", item.getWeight().toString());

                builder.lore(CC.translate(lore));
            }

            return builder.build();
        }
        return null;
    }

}
