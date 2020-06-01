package me.ohvalsgod.thads.baller.gui.buttons.object;

import lombok.AllArgsConstructor;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.armor.AbstractBallerArmor;
import me.ohvalsgod.thads.baller.gui.object.LOLObjectEditMenu;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.baller.object.AbstractBallerObject;
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
public class BallerObjectButton extends Button {

    private final AbstractBallerObject item;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (item instanceof AbstractBallerArmor) {
            AbstractBallerArmor armor = (AbstractBallerArmor) item;
            ItemBuilder builder = new ItemBuilder(armor.getBallerArmor()[1].getType());
            builder.enchantment(Enchantment.DURABILITY, 1);
            builder.hideFlags();
            builder.name(armor.getBallerArmor()[1].getItemMeta().getDisplayName());

            for (String string : Thads.get().getLang().getStringList("lol.menu.armor.item.lore")) {
                String lore = string
                        .replace("%separator%", CC.SCOREBOARD_SEPERATOR_EXTRA)
                        .replace("%enabled%", WordUtils.capitalizeFully(Boolean.toString(armor.isEnabled())))
                        .replace("%buy_price%", NumberUtil.formatNumber(armor.getBuyPrice()))
                        .replace("%sell_price%", NumberUtil.formatNumber(armor.getSellPrice()))
                        .replace("%weight%", armor.getWeight().toString());

                builder.lore(CC.translate(lore));
            }
        } else if (item instanceof AbstractBallerItem) {
            AbstractBallerItem ballerItem = (AbstractBallerItem) item;
            ItemBuilder builder = new ItemBuilder(ballerItem.getBallerItemStack().getType());
            builder.enchantment(Enchantment.DURABILITY, 1);
            builder.hideFlags();
            builder.name(ballerItem.getBallerItemStack().getItemMeta().getDisplayName());

            for (String string : Thads.get().getLang().getStringList("lol.menu.objects.item.lore")) {
                String lore = string
                        .replace("%separator%", CC.SCOREBOARD_SEPERATOR_EXTRA)
                        .replace("%enabled%", WordUtils.capitalizeFully(Boolean.toString(ballerItem.isEnabled())))
                        .replace("%legendary_enabled%", WordUtils.capitalizeFully(Boolean.toString(ballerItem.isLegendaryEnabled())))
                        .replace("%buy_price%", NumberUtil.formatNumber(ballerItem.getBuyPrice()))
                        .replace("%sell_price%", NumberUtil.formatNumber(ballerItem.getSellPrice()))
                        .replace("%weight%", ballerItem.getWeight().toString());

                builder.lore(CC.translate(lore));
            }
        }
        return null;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (clickType.isRightClick()) {
            if (item instanceof AbstractBallerItem) {
                if (((AbstractBallerItem) item).isLegendaryEnabled() && item.isEnabled()) {
                    if (player.getInventory().firstEmpty() > -1) {
                        player.getInventory().addItem(((AbstractBallerItem) item).getLegendaryItemStack());
                    }
                }
            } else {
                player.sendMessage(CC.translate(Thads.get().getLang().getString("lol.error.legendary-disabled")));
            }
        } else if (clickType.isLeftClick()) {
            if (item.isEnabled()) {
                if (player.getInventory().firstEmpty() > -1) {
                    if (item instanceof AbstractBallerItem) {
                        player.getInventory().addItem(((AbstractBallerItem) item).getBallerItemStack());
                    } else if (item instanceof AbstractBallerArmor) {
                        if (player.getInventory().firstEmpty() > 2) {
                            for (ItemStack itemStack : ((AbstractBallerArmor) item).getBallerArmor()) {
                                player.getInventory().addItem(itemStack);
                            }
                        }
                    }
                }
            } else {
                player.sendMessage(CC.translate(Thads.get().getLang().getString("lol.error.baller-item-disabled")));
            }
        } else if (clickType.isCreativeAction()) {
            new LOLObjectEditMenu(item).openMenu(player);
        }
    }

}
