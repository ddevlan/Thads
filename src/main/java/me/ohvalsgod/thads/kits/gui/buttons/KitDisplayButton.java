package me.ohvalsgod.thads.kits.gui.buttons;

import lombok.AllArgsConstructor;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.kits.Kit;
import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.util.TimeUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

@AllArgsConstructor
public class KitDisplayButton extends Button {

    private Kit kit;

        /*

    menu:
    title: "&9Kit Selector"
    icon:
    name: "&a%kit%"
    lore:
            - "&7%cooldown%"
            - "&6Remaining: &e%remaining%"
    not-owned: "&cYou do not own this kit!"

    */

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack icon = kit.getIcon();
        ItemMeta meta = icon.getItemMeta();

        meta.setDisplayName(Thads.get().getLang().getString("kits.menu.icon.name").replace("%kit%", kit.getName()));

        ArrayList<String> lore = new ArrayList<>();

        for (String string : Thads.get().getLang().getStringList("kits.menu.icon.lore")) {
            lore.add(string
                    .replace("%remaining%", String.valueOf(kit.getCooldownRemaining(player.getUniqueId())))
                    .replace("%cooldown%", TimeUtil.formatToMonthDetailedString(kit.getCooldown())));
        }

        if (!kit.hasPermission(player)) {
            lore.add("");
            lore.add(Thads.get().getLang().getString("kits.menu.icon.not-owned"));
        }

        icon.setItemMeta(meta);

        return icon;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        kit.give(player);
    }
}
