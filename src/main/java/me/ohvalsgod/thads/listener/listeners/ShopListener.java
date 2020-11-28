package me.ohvalsgod.thads.listener.listeners;

import com.shopify.model.ShopifyOrder;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.dev.shopify.event.ShopifyOrderAttemptProcessEvent;
import me.ohvalsgod.thads.dev.shopify.event.ShopifyOrderQueueEvent;
import me.ohvalsgod.thads.dev.shopify.event.ShopifyOrderQueueTickEvent;
import me.ohvalsgod.thads.dev.shopify.event.ShopifyOrderReceivedEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Level;

public class ShopListener implements Listener {

    @EventHandler
    public void onOrderReceive(ShopifyOrderReceivedEvent event) {
        ShopifyOrder order = event.getShopifyOrder();

        if (!event.inQueue()) {
            event.queue();
        } else {
            Thads.get().getLogger().log(Level.WARNING, "This should not happen, but an order that was already in queue has been received as a duplicate." + order.getName() + "_" + order.getUserId() + "_" + order.getCreatedAt().toString());
        }
    }

    @EventHandler
    public void onOrderQueue(ShopifyOrderQueueEvent event) {
        ShopifyOrder order = event.getShopifyOrder();

        //todo: if for some reason in the future an order in queue needs to be removed, this is where it will be
    }

    @EventHandler
    public void onOrderQueueTick(ShopifyOrderQueueTickEvent event) {
        ShopifyOrder order = event.getShopifyOrder();

        Thads.get().getLogger().log(Level.FINE, order.getId() + " tick!");
    }

    @EventHandler
    public void onOrderAttemptProcess(ShopifyOrderAttemptProcessEvent event) {
        ShopifyOrder order = event.getShopifyOrder();

        //TODO: ALSO HAVE TO LINK SHOPIFY ACCOUNTS TO MC ACCOUNTS AND CHECK HERE. THIS IS WHERE THE ORDER WILL

        System.out.println(order.getCartToken() + " processed!");
        Bukkit.getServer().broadcastMessage(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastname() + " purchased " + order.getCartToken());

        //TODO: add code in the future to link order to ingame actions

    }

}
