package me.ohvalsgod.thads.baller.item;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface BallerItem {

    String getName();

    ItemStack getBallerItemStack();

    void setBallerItemStack(ItemStack itemStack);

    ItemStack getLegendaryItemStack();

    boolean getLegendaryItemEnabled();

    void setLegendaryItemStack(ItemStack itemStack);

    void setLegendaryEnabled(boolean b);

    int getSellPrice();

    void setSellPrice(int i);

    int getBuyPrice();

    void setBuyPrice(int i);

    int getLegendarySellPrice();

    void setLegendarySellPrice(int i);

    int getLegendaryBuyPrice();

    void setLegendaryBuyPrice(int i);

    Listener getListener();

    boolean isEnabled();

    void setEnabled(boolean b);

    List<String> getAliases();

}
