package me.ohvalsgod.thads.baller.object;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.config.ConfigCursor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBallerObject implements IBallerObject {

    protected final ConfigCursor LANG;
    protected BallerManager ballerManager;

    private String name;
    private int sellPrice, buyPrice;
    protected Listener listener;
    private boolean enabled;
    private List<String> aliases;
    private double weight;
    private boolean registered = false;

    public AbstractBallerObject(String name) {
        this.name = name;
        this.sellPrice = 0;
        this.buyPrice = 0;
        this.enabled = true;
        this.aliases = new ArrayList<>();
        this.weight = 0;

        LANG = new ConfigCursor(Thads.get().getLangConfig(), "lol.objects." + name);
        ballerManager = Thads.get().getBallerManager();
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
    public Integer getSellPrice() {
        return sellPrice;
    }

    @Override
    public void setSellPrice(int i) {
        this.sellPrice = i;
    }

    @Override
    public Integer getBuyPrice() {
        return buyPrice;
    }

    @Override
    public void setBuyPrice(int i) {
        this.buyPrice = i;
    }

    @Override
    public Boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean b) {
        this.enabled = b;
    }

    @Override
    public Double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double d) {
        this.weight = d;
    }

    @Override
    public Listener getListener() {
        return listener;
    }

    public void fixJrebel() {
        System.out.println(getClass().getSimpleName() + "#fixJrebel() method ran");
    }

    public abstract void give(Player player);

    @Override
    public void unregister() {
        if (registered) {
            if (listener != null) {
                HandlerList.unregisterAll(listener);
                registered = !registered;
            }
        } else {
            Thads.get().getLogger().warning("Tried to register events that weren't registered.");
        }
    }

    @Override
    public void register() {
        if (getListener() != null) {
            if (!registered) {
                Thads.get().getServer().getPluginManager().registerEvents(listener, Thads.get());
                registered = !registered;
            } else {
                Thads.get().getLogger().warning("Tried to register events that were already registered.");
            }
        }
    }

}
