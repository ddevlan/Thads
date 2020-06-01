package me.ohvalsgod.thads.baller.gui.buttons.weapons;

import lombok.AllArgsConstructor;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.util.CC;
import me.ohvalsgod.thads.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class LegendaryItemToggleButton extends Button {

    private AbstractBallerItem item;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(Material.WOOL);
        builder.name(item.getLegendaryItemStack().getItemMeta().getDisplayName() + (item.isLegendaryEnabled() ? " Enabled": " Disabled"));
        builder.lore(CC.GRAY + (item.isLegendaryEnabled() ? "Click to disable.":"Click to enable."));
        builder.durability(item.isLegendaryEnabled() ? 5:14);
        return builder.build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        item.setLegendaryEnabled(!item.isLegendaryEnabled());
        player.sendMessage(Thads.get().getLang().getString("lol.weapons.item-toggled")
                .replace("%item%", item.getLegendaryItemStack().getItemMeta().getDisplayName())
                .replace("%toggled%", item.isLegendaryEnabled() ? CC.GREEN + "enabled":CC.RED + "disabled"));
    }

    @Override
    public boolean shouldUpdate(Player player, ClickType clickType) {
        return true;
    }
}
