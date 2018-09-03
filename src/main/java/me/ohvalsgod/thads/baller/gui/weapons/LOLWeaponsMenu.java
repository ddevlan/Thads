package me.ohvalsgod.thads.baller.gui.weapons;

import lombok.AllArgsConstructor;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.item.BallerItem;
import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.menu.pagination.PaginatedMenu;
import me.ohvalsgod.thads.util.CC;
import me.ohvalsgod.thads.util.ItemBuilder;
import org.apache.commons.lang.WordUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class LOLWeaponsMenu extends PaginatedMenu {

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        //TODO: change this to the central menu once its made

        return buttons;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return CC.translate(Thads.getInstance().getLangConfig().getConfig().getString("lol.weapons.gui-title"));
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (BallerItem item : BallerManager.getBallerManager().getBallerItems()) {
            buttons.put(buttons.size(), new BallerItemDisplayButton(item));
        }

        return buttons;
    }

    @AllArgsConstructor
    public static class BallerItemDisplayButton extends Button {

        private BallerItem item;

        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder builder = new ItemBuilder(item.getBallerItemStack().getType());
            builder.enchantment(Enchantment.DURABILITY, 1);
            builder.hideFlags();
            builder.name(CC.AQUA + item.getBallerItemStack().getItemMeta().getDisplayName());
            builder.lore(CC.SCOREBOARD_SEPERATOR_EXTRA);
            builder.lore(CC.YELLOW + "Enabled? " + CC.AQUA + WordUtils.capitalizeFully(Boolean.toString(item.isEnabled())));
            builder.lore(CC.YELLOW + "Legendary Enabled? " + CC.AQUA + WordUtils.capitalizeFully(Boolean.toString(item.isLegendaryItemEnabled())));
            builder.lore(CC.YELLOW + "Shop Price: " + CC.AQUA + item.getBuyPrice());
            builder.lore(CC.YELLOW + "Sell Price: " + CC.AQUA + item.getSellPrice());
            builder.lore(CC.SCOREBOARD_SEPERATOR_EXTRA);
            builder.lore(CC.GRAY + CC.ITALIC + "Left-click for regular, right-click for legendary.");

            return builder.build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if (clickType.isRightClick()) {
                if (item.isLegendaryItemEnabled() && item.isEnabled()) {
                    if (player.getInventory().firstEmpty() > -1) {
                        player.getInventory().addItem(item.getLegendaryItemStack());
                    }
                } else {
                    player.sendMessage(CC.translate(Thads.getInstance().getLangConfig().getConfig().getString("lol.error.legendary-disabled")));
                }
            } else if (clickType.isLeftClick()) {
                if (item.isEnabled()) {
                    if (player.getInventory().firstEmpty() > -1) {
                        player.getInventory().addItem(item.getBallerItemStack());
                    }
                } else {
                    player.sendMessage(CC.translate(Thads.getInstance().getLangConfig().getConfig().getString("lol.error.baller-item-disabled")));
                }
            }
            //TODO: possibly implement edit system by pressing a number on keyboard while hovering over item
        }
    }

}
