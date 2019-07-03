package me.ohvalsgod.thads.listener.listeners;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.gui.buttons.weapons.BallerItemEditBuyPriceButton;
import me.ohvalsgod.thads.baller.gui.buttons.weapons.BallerItemEditSellPriceButton;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.util.CC;
import me.ohvalsgod.thads.util.NumberUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.NumberFormat;

public class PlayerListener implements Listener {
    
    @EventHandler
    public void editHandler(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().replace("$", "").replace(",", "");

        if (BallerItemEditSellPriceButton.getInEdit().containsKey(player.getUniqueId())
                || BallerItemEditBuyPriceButton.getInEdit().containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                if (BallerItemEditBuyPriceButton.getInEdit().containsKey(player.getUniqueId())) {
                    BallerItemEditBuyPriceButton.getInEdit().remove(player.getUniqueId());
                    player.sendMessage(Thads.getInstance().getLang().getString("lol.error.edit-cancelled"));
                } else if (BallerItemEditSellPriceButton.getInEdit().containsKey(player.getUniqueId())) {
                    BallerItemEditSellPriceButton.getInEdit().remove(player.getUniqueId());
                    player.sendMessage(Thads.getInstance().getLang().getString("lol.error.edit-cancelled"));
                    return;
                }
            } else {
                message = message.replace(" ", "");

                if (!NumberUtil.isInteger(message)) {
                    player.sendMessage(Thads.getInstance().getLang().getString("lol.error.not-a-number"));
                    player.sendMessage(" ");
                    player.sendMessage(CC.GRAY + "Please enter your value again:");
                    return;
                }
                int price = Integer.valueOf(message);

                AbstractBallerItem item;

                if (BallerItemEditBuyPriceButton.getInEdit().containsKey(player.getUniqueId())) {
                    item = BallerItemEditBuyPriceButton.getInEdit().get(player.getUniqueId());

                    item.setBuyPrice(price);
                    player.sendMessage(Thads.getInstance().getLang().getString("lol.weapons.edit.price.buy")
                            .replace("%item%", item.getBallerItemStack().getItemMeta().getDisplayName())
                            .replace("%amount%", NumberFormat.getCurrencyInstance().format(item.getBuyPrice())));
                    BallerItemEditBuyPriceButton.getInEdit().remove(player.getUniqueId());
                } else if (BallerItemEditSellPriceButton.getInEdit().containsKey(player.getUniqueId())) {
                    item = BallerItemEditSellPriceButton.getInEdit().get(player.getUniqueId());

                    item.setSellPrice(price);
                    player.sendMessage(Thads.getInstance().getLang().getString("lol.weapons.edit.price.sell")
                            .replace("%item%", item.getBallerItemStack().getItemMeta().getDisplayName())
                            .replace("%amount%", NumberFormat.getCurrencyInstance().format(item.getSellPrice())));
                    BallerItemEditSellPriceButton.getInEdit().remove(player.getUniqueId());
                }
            }
        }
    }

}
