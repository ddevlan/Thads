package me.ohvalsgod.thads.baller.gui.buttons.weapons;

import lombok.AllArgsConstructor;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.baller.gui.weapons.LOLWeaponsEditMenu;
import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.util.CC;
import me.ohvalsgod.thads.util.ItemBuilder;
import me.ohvalsgod.thads.util.NumberUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class BallerItemDisplayButton extends Button {

    private AbstractBallerItem item;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(item.getBallerItemStack().getType());
        builder.enchantment(Enchantment.DURABILITY, 1);
        builder.hideFlags();
        builder.name(item.getBallerItemStack().getItemMeta().getDisplayName());

        for (String string : Thads.getInstance().getLang().getStringList("lol.menu.weapons.item.lore")) {
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

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (clickType.isRightClick()) {
            if (item.isLegendaryEnabled() && item.isEnabled()) {
                if (player.getInventory().firstEmpty() > -1) {
                    player.getInventory().addItem(item.getLegendaryItemStack());
                }
            } else {
                player.sendMessage(CC.translate(Thads.getInstance().getLang().getString("lol.error.legendary-disabled")));
            }
        } else if (clickType.isLeftClick()) {
            if (item.isEnabled()) {
                if (player.getInventory().firstEmpty() > -1) {
                    player.getInventory().addItem(item.getBallerItemStack());
                }
            } else {
                player.sendMessage(CC.translate(Thads.getInstance().getLang().getString("lol.error.baller-item-disabled")));
            }
        } else if (clickType.isCreativeAction()) {
            new LOLWeaponsEditMenu(item).openMenu(player);
        }
    }
}
