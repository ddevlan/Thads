package me.ohvalsgod.thads.baller.object;

import org.bukkit.event.Listener;

import java.util.List;

interface IBallerObject {

    String getName();

    List<String> getAliases();

    Integer getSellPrice();

    void setSellPrice(int i);

    Integer getBuyPrice();

    void setBuyPrice(int i);

    Boolean isEnabled();

    void setEnabled(boolean b);

    Double getWeight();

    void setWeight(double d);

    Listener getListener();

    void register();

    void unregister();

}
