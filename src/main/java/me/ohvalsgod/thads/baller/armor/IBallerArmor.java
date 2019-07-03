package me.ohvalsgod.thads.baller.armor;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface IBallerArmor {

    String getName();

    List<String> getAliases();

    ItemStack[] getArmor();

    void setArmor(ItemStack[] item);

    Integer getSellPrice();

    void setSellPrice(int i);

    Integer getBuyPrice();

    void setBuyPrice(int i);

    Boolean isEnabled();

    void setEnabled(boolean b);

    Double getWeight();

    void setWeight(double d);

    Listener getListeners();

}
