package me.ohvalsgod.thads.baller.item.items;

import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.item.BallerItem;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class Noobsblade implements BallerItem {

    private ItemStack ballerItemStack, legendaryItemStack;
    private int sellPrice, buyPrice, legendarySellPrice, legendaryBuyPrice;
    private boolean enabled, legendaryEnabled;
    private Listener listener;
    private List<String> aliases;

    public Noobsblade() {
        BallerManager.getBallerManager().loadBallerItem(this);
        aliases = Arrays.asList("noobsword", "noobssword");
    }

    @Override
    public String getName() {
        return "noobsblade";
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

}
