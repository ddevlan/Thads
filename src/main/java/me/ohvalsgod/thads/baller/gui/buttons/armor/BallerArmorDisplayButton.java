package me.ohvalsgod.thads.baller.gui.buttons.armor;

import lombok.AllArgsConstructor;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.armor.AbstractBallerArmor;
import me.ohvalsgod.thads.baller.gui.armor.LOLArmorsEditMenu;
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
public class BallerArmorDisplayButton extends Button {

    private AbstractBallerArmor armor;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(armor.getArmor()[1].getType());
        builder.enchantment(Enchantment.DURABILITY, 1);
        builder.hideFlags();
        builder.name(armor.getArmor()[1].getItemMeta().getDisplayName());

        for (String string : Thads.getInstance().getLang().getStringList("lol.menu.weapons.armor.lore")) {
            String lore = string
                    .replace("%separator%", CC.SCOREBOARD_SEPERATOR_EXTRA)
                    .replace("%enabled%", WordUtils.capitalizeFully(Boolean.toString(armor.isEnabled())))
                    .replace("%buy_price%", NumberUtil.formatNumber(armor.getBuyPrice()))
                    .replace("%sell_price%", NumberUtil.formatNumber(armor.getSellPrice()))
                    .replace("%weight%", armor.getWeight().toString());

            builder.lore(CC.translate(lore));
        }

        return builder.build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (clickType.isCreativeAction()) {
            new LOLArmorsEditMenu(armor).openMenu(player);
        } else {
            if (armor.isEnabled()) {
                for (ItemStack is : armor.getArmor()) {
                    if (player.getInventory().firstEmpty() > -1) {
                        player.getInventory().addItem(is);
                    }
                }
            } else {
                player.sendMessage(CC.translate(Thads.getInstance().getLang().getString("lol.error.baller-armor-disabled")));
            }
        }
    }

}
