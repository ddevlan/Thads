package me.ohvalsgod.thads.baller.item;

import org.bukkit.inventory.ItemStack;

public interface IBallerItem {

    ItemStack getBallerItemStack();

    void setBallerItemStack(ItemStack item);

    ItemStack getLegendaryItemStack();

    void setLegendaryItemStack(ItemStack item);

    Boolean isLegendaryEnabled();

    void setLegendaryEnabled(boolean b);

}
