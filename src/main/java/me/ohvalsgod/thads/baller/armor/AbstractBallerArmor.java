package me.ohvalsgod.thads.baller.armor;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBallerArmor implements IBallerArmor {

    public String name;
    public ItemStack[] ballerItemStack;
    public int sellPrice, buyPrice;
    public Listener listener;
    public boolean enabled;
    public List<String> aliases;
    public double weight;

    public AbstractBallerArmor(String name) {
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
    public ItemStack[] getArmor() {
        return ballerItemStack;
    }

    @Override
    public void setArmor(ItemStack[] items) {
        ballerItemStack = items;
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
    public Double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double d) {
        weight = d;
    }

    @Override
    public Listener getListeners() {
        return listener;
    }

}
