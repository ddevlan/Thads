package me.ohvalsgod.thads.baller.gui.buttons.weapons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.gui.buttons.armor.BallerArmorEditBuyPriceButton;
import me.ohvalsgod.thads.baller.gui.buttons.armor.BallerArmorEditSellPriceButton;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
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
public class BallerItemEditSellPriceButton extends Button {

    @Getter private static Map<UUID, AbstractBallerItem> inEdit = new HashMap();

    private AbstractBallerItem item;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(Material.MAP);
        builder.name(Thads.getInstance().getLang().getString("lol.menu.weapons.edit.price.sell"));
        builder.lore(Thads.getInstance().getLang().getString("lol.menu.weapons.edit.price.edit"));
        return builder.build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (!BallerItemEditBuyPriceButton.getInEdit().containsKey(player.getUniqueId())
                && !BallerArmorEditSellPriceButton.getInEdit().containsKey(player.getUniqueId())
                && !BallerArmorEditBuyPriceButton.getInEdit().containsKey(player.getUniqueId())
                && !inEdit.containsKey(player.getUniqueId())) {
            player.closeInventory();
            inEdit.put(player.getUniqueId(), item);
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
