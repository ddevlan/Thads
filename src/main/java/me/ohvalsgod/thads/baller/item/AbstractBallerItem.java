package me.ohvalsgod.thads.baller.item;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.config.ConfigCursor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class AbstractBallerItem implements IBallerItem {

    protected final ConfigCursor LANG_ITEM;
    protected final ConfigCursor LANG;

    private Map<UUID, Long> cooldowns;

    public String name;
    private ItemStack ballerItemStack, legendaryItemStack;
    private int sellPrice, buyPrice;
    public Listener listener;
    private boolean enabled, legendaryEnabled;
    private List<String> aliases;
    private double weight;
    private boolean registered = false;
    private double cooldown;

    public AbstractBallerItem(String name) {
        this.name = name;
        LANG_ITEM = new ConfigCursor(Thads.getInstance().getLangConfig(), "lol.weapons." + name);
        LANG = new ConfigCursor(Thads.getInstance().getLangConfig(), "");
        aliases = new ArrayList<>();
        cooldowns = new HashMap<>();
    }

    public boolean expired(Player player) {
        return cooldowns.getOrDefault(player.getUniqueId(), 0L) + cooldown*1000 > System.currentTimeMillis();
    }

    public void cool(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public double remaining(Player player) {
        return System.currentTimeMillis() + cooldown*1000 - cooldowns.get(player.getUniqueId());
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
    public Boolean isLegendaryEnabled() {
        return legendaryEnabled;
    }

    @Override
    public void setEnabled(boolean b) {
        enabled = b;
    }

    @Override
    public void setLegendaryEnabled(boolean b) {
        this.legendaryEnabled = b;
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

    @Override
    public void unregister() {
        if (registered) {
            if (listener != null) {
                HandlerList.unregisterAll(listener);
                registered = !registered;
            }
        } else {
            Thads.getInstance().getLogger().warning("Tried to register events that weren't registered.");
        }
    }

    @Override
    public void register() {
        if (!registered) {
            Thads.getInstance().getServer().getPluginManager().registerEvents(listener, Thads.getInstance());
            registered = !registered;
        } else {
            Thads.getInstance().getLogger().warning("Tried to register events that were already registered.");
        }
    }

}
