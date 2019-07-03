package me.ohvalsgod.thads.baller.gui.buttons.armor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.armor.AbstractBallerArmor;
import me.ohvalsgod.thads.baller.gui.buttons.weapons.BallerItemEditBuyPriceButton;
import me.ohvalsgod.thads.baller.gui.buttons.weapons.BallerItemEditSellPriceButton;
import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class BallerArmorEditBuyPriceButton extends Button {

    @Getter private static Map<UUID, AbstractBallerArmor> inEdit = new HashMap();

    private AbstractBallerArmor armor;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(Material.MAP);
        builder.name(Thads.getInstance().getLang().getString("lol.menu.armor.edit.price.buy"));
        builder.lore(Thads.getInstance().getLang().getString("lol.menu.armor.edit.price.edit"));
        return builder.build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (!BallerArmorEditSellPriceButton.getInEdit().containsKey(player.getUniqueId())
                && !BallerItemEditSellPriceButton.getInEdit().containsKey(player.getUniqueId())
                && !BallerItemEditBuyPriceButton.getInEdit().containsKey(player.getUniqueId())
                && !inEdit.containsKey(player.getUniqueId())) {
            player.closeInventory();
            inEdit.put(player.getUniqueId(), armor);
            player.sendMessage(Thads.getInstance().getLang().getString("lol.economy.price.edit.buy"));

            final UUID uuid = player.getUniqueId();
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (inEdit.containsKey(player.getUniqueId())) {
                        inEdit.remove(player.getUniqueId());

                        if (Bukkit.getPlayer(uuid) != null) {
                            player.sendMessage(Thads.getInstance().getLang().getString("lol.error.edit-expired"));
                        }
                    }
                }
            }.runTaskLater(Thads.getInstance(), 20 * 30);
        } else {
            player.sendMessage(Thads.getInstance().getLang().getString("lol.error.edit-already-running"));
        }
    }
}
