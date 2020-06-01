package me.ohvalsgod.thads.baller.gui.buttons.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.object.AbstractBallerObject;
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
public class BallerObjectEditBuyPriceButton extends Button {

    @Getter private static Map<UUID, AbstractBallerObject> inEdit = new HashMap();

    private AbstractBallerObject item;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(Material.MAP);
        builder.name(Thads.get().getLang().getString("lol.menu.objects.edit.price.buy"));
        builder.lore(Thads.get().getLang().getString("lol.menu.objects.edit.price.edit"));
        return builder.build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (!BallerObjectEditSellPriceButton.getInEdit().containsKey(player.getUniqueId())
                && !inEdit.containsKey(player.getUniqueId())) {
            player.closeInventory();
            inEdit.put(player.getUniqueId(), item);
            player.sendMessage(Thads.get().getLang().getString("lol.economy.price.edit.buy"));

            final UUID uuid = player.getUniqueId();
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (inEdit.containsKey(player.getUniqueId())) {
                        inEdit.remove(player.getUniqueId());

                        if (Bukkit.getPlayer(uuid) != null) {
                            player.sendMessage(Thads.get().getLang().getString("lol.error.edit-expired"));
                        }
                    }
                }
            }.runTaskLater(Thads.get(), 20 * 30);
        } else {
            player.sendMessage(Thads.get().getLang().getString("lol.error.edit-already-running"));
        }
    }

}
