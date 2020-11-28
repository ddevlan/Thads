package me.ohvalsgod.thads.baller.item;

import org.bukkit.inventory.ItemStack;

public interface IBallerItem {

    ItemStack getBallerItemStack();

    ItemStack getLegendaryItemStack();

    Boolean isLegendaryEnabled();

    void setLegendaryEnabled(boolean b);

}
