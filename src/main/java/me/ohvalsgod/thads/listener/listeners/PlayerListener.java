package me.ohvalsgod.thads.listener.listeners;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.armor.AbstractBallerArmor;
import me.ohvalsgod.thads.baller.gui.buttons.object.BallerObjectEditBuyPriceButton;
import me.ohvalsgod.thads.baller.gui.buttons.object.BallerObjectEditSellPriceButton;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.baller.object.AbstractBallerObject;
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

        if (BallerObjectEditSellPriceButton.getInEdit().containsKey(player.getUniqueId())
                || BallerObjectEditBuyPriceButton.getInEdit().containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                if (BallerObjectEditBuyPriceButton.getInEdit().containsKey(player.getUniqueId())) {
                    BallerObjectEditBuyPriceButton.getInEdit().remove(player.getUniqueId());
                    player.sendMessage(Thads.get().getLang().getString("lol.error.edit-cancelled"));
                } else if (BallerObjectEditSellPriceButton.getInEdit().containsKey(player.getUniqueId())) {
                    BallerObjectEditSellPriceButton.getInEdit().remove(player.getUniqueId());
                    player.sendMessage(Thads.get().getLang().getString("lol.error.edit-cancelled"));
                    return;
                }
            } else {
                message = message.replace(" ", "");

                if (!NumberUtil.isInteger(message)) {
                    player.sendMessage(Thads.get().getLang().getString("lol.error.not-a-number"));
                    player.sendMessage(" ");
                    player.sendMessage(CC.GRAY + "Please enter your value again:");
                    return;
                }
                int price = Integer.valueOf(message);

                AbstractBallerObject item;

                if (BallerObjectEditBuyPriceButton.getInEdit().containsKey(player.getUniqueId())) {
                    item = BallerObjectEditBuyPriceButton.getInEdit().get(player.getUniqueId());

                    item.setBuyPrice(price);
                    Thads.change();
                    player.sendMessage(Thads.get().getLang().getString("lol.objects.edit.price.buy")
                            .replace("%item%", (item instanceof AbstractBallerItem ? ((AbstractBallerItem) item).getBallerItemStack().getItemMeta().getDisplayName():((AbstractBallerArmor)item).getBallerArmor()[1].getItemMeta().getDisplayName()))
                            .replace("%amount%", NumberFormat.getCurrencyInstance().format(item.getBuyPrice())));
                    BallerObjectEditBuyPriceButton.getInEdit().remove(player.getUniqueId());
                } else if (BallerObjectEditSellPriceButton.getInEdit().containsKey(player.getUniqueId())) {
                    item = BallerObjectEditSellPriceButton.getInEdit().get(player.getUniqueId());

                    item.setSellPrice(price);
                    Thads.change();
                    player.sendMessage(Thads.get().getLang().getString("lol.objects.edit.price.sell")
                            .replace("%item%", (item instanceof AbstractBallerItem ? ((AbstractBallerItem) item).getBallerItemStack().getItemMeta().getDisplayName():((AbstractBallerArmor)item).getBallerArmor()[1].getItemMeta().getDisplayName()))
                            .replace("%amount%", NumberFormat.getCurrencyInstance().format(item.getSellPrice())));
                    BallerObjectEditSellPriceButton.getInEdit().remove(player.getUniqueId());
                }
            }
        }
    }

}
