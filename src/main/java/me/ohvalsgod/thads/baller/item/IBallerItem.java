package me.ohvalsgod.thads.baller.item;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface IBallerItem {

    String getName();

    List<String> getAliases();

    ItemStack getBallerItemStack();

    void setBallerItemStack(ItemStack item);

    ItemStack getLegendaryItemStack();

    void setLegendaryItemStack(ItemStack item);

    Integer getSellPrice();

    void setSellPrice(int i);

    Integer getBuyPrice();

    void setBuyPrice(int i);

    Boolean isEnabled();

    void setEnabled(boolean b);

    Boolean isLegendaryEnabled();

    void setLegendaryEnabled(boolean b);

    Double getWeight();

    void setWeight(double d);

    Listener getListener();

    void unregister();

    void register();

}
