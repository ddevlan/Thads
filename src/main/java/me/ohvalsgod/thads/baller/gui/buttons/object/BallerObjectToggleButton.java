package me.ohvalsgod.thads.baller.gui.buttons.object;

import lombok.AllArgsConstructor;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.armor.AbstractBallerArmor;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.baller.object.AbstractBallerObject;
import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.util.CC;
import me.ohvalsgod.thads.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class BallerObjectToggleButton extends Button {

    private AbstractBallerObject item;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(Material.WOOL);
        builder.name((item instanceof AbstractBallerItem ? ((AbstractBallerItem) item).getBallerItemStack().getItemMeta().getDisplayName():((AbstractBallerArmor)item).getBallerArmor()[1].getItemMeta().getDisplayName()) + (item.isEnabled() ? " Enabled": " Disabled"));
        builder.lore(CC.GRAY + (item.isEnabled() ? "Click to disable.":"Click to enable."));
        builder.durability(item.isEnabled() ? 5:14);
        return builder.build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        item.setEnabled(!item.isEnabled());
        Thads.change();
        player.sendMessage(Thads.get().getLang().getString("lol.objects.item-toggled")
                .replace("%item%", (item instanceof AbstractBallerItem ? ((AbstractBallerItem) item).getBallerItemStack().getItemMeta().getDisplayName():((AbstractBallerArmor)item).getBallerArmor()[1].getItemMeta().getDisplayName()))
                .replace("%toggled%", item.isEnabled() ? CC.GREEN + "enabled":CC.RED + "disabled"));
    }


    @Override
    public boolean shouldUpdate(Player player, ClickType clickType) {
        return true;
    }
}
