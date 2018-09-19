package me.ohvalsgod.thads.baller.item;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBallerItem implements IBallerItem {

    public String name;
    public ItemStack ballerItemStack, legendaryItemStack;
    public int sellPrice, buyPrice, legendarySellPrice, legendaryBuyPrice;
    public Listener listener;
    public boolean enabled, legendaryEnabled;
    public List<String> aliases;
    public double weight;

    public AbstractBallerItem(String name) {
        this.name = name;
        aliases = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public ItemStack getBallerItemStack() {
        return ballerItemStack;
    }

    @Override
    public void setBallerItemStack(ItemStack item) {
        ballerItemStack = item;
    }

    @Override
    public ItemStack getLegendaryItemStack() {
        return legendaryItemStack;
    }

    @Override
    public void setLegendaryItemStack(ItemStack item) {
        legendaryItemStack = item;
    }

    @Override
    public Integer getSellPrice() {
        return sellPrice;
    }

    @Override
    public void setSellPrice(int i) {
        sellPrice = i;
    }

    @Override
    public Integer getBuyPrice() {
        return buyPrice;
    }

    @Override
    public void setBuyPrice(int i) {
        buyPrice = i;
    }

    @Override
    public Boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean b) {
        enabled = b;
    }

    @Override
    public Boolean isLegendaryEnabled() {
        return legendaryEnabled;
    }

    @Override
    public void setLegendaryEnabled(boolean b) {
        legendaryEnabled = b;
    }

    @Override
    public Double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double d) {
        weight = d;
    }

    @Override
    public Listener getListener() {
        return listener;
    }

}
