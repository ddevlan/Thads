package me.ohvalsgod.thads.baller.item.items;

import me.ohvalsgod.thads.baller.item.BallerItem;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class DateRapist implements BallerItem {

    private ItemStack ballerItemStack, legendaryItemStack;
    private int sellPrice, buyPrice, legendarySellPrice, legendaryBuyPrice;
    private boolean enabled, legendaryEnabled;
    private List<String> aliases;

    public DateRapist() {
        aliases = Arrays.asList("dr", "daterape");

    }

    @Override
    public String getName() {
        return "daterapist";
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

    @Override
    public int getWeight() {
        return 6;
    }

    @Override
    public Listener getListener() {
        return null;
    }

}
