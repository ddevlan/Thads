package me.ohvalsgod.thads.baller.item.items;

import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.item.BallerItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class Ejacul8 implements BallerItem {

    private ItemStack ballerItemStack, legendaryItemStack;
    private int sellPrice, buyPrice, legendarySellPrice, legendaryBuyPrice;
    private boolean enabled, legendaryEnabled;
    private Listener listener;
    private List<String> aliases;

    public Ejacul8() {
        BallerManager.getBallerManager().loadBallerItem(this);
        aliases = Collections.singletonList("ejac");
        listener = new EJACListener();
    }

    @Override
    public String getName() {
        return "ejacul8";
    }

    @Override
    public ItemStack getBallerItemStack() {
        return ballerItemStack;
    }

    @Override
    public void setBallerItemStack(ItemStack itemStack) {
        this.ballerItemStack = itemStack;
    }

    @Override
    public ItemStack getLegendaryItemStack() {
        return legendaryItemStack;
    }

    @Override
    public boolean isLegendaryItemEnabled() {
        return legendaryEnabled;
    }

    @Override
    public void setLegendaryItemStack(ItemStack itemStack) {
        this.legendaryItemStack = itemStack;
    }

    @Override
    public void setLegendaryEnabled(boolean b) {
        this.legendaryEnabled = b;
    }

    @Override
    public int getSellPrice() {
        return sellPrice;
    }

    @Override
    public void setSellPrice(int i) {
        this.sellPrice = i;
    }

    @Override
    public int getBuyPrice() {
        return buyPrice;
    }

    @Override
    public void setBuyPrice(int i) {
        this.buyPrice = i;
    }

    @Override
    public int getLegendarySellPrice() {
        return legendarySellPrice;
    }

    @Override
    public void setLegendarySellPrice(int i) {
        this.legendarySellPrice = i;
    }

    @Override
    public int getLegendaryBuyPrice() {
        return legendaryBuyPrice;
    }

    @Override
    public void setLegendaryBuyPrice(int i) {
        this.legendaryBuyPrice = i;
    }

    @Override
    public Listener getListener() {
        return listener;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean b) {
        this.enabled = b;
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    public class EJACListener implements Listener {

        @EventHandler
        public void onDamage(EntityDamageByEntityEvent event) {
            if (event.getEntity() instanceof Player) {
                if (event.getDamager() instanceof Player) {
                    Player damager = (Player) event.getDamager();
                    Player damaged = (Player) event.getEntity();

                    if (BallerManager.getBallerManager().getByItemStack(damager.getItemInHand()) instanceof Ejacul8) {
                        //TODO: worldguard checks, invis ring checks
                        damaged.sendMessage("debug EJACUL8");
                        damager.sendMessage("debug EJACUL8");
                    }
                }
            }
        }

    }


}
