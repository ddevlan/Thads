package me.ohvalsgod.thads.baller.item;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.object.AbstractBallerObject;
import me.ohvalsgod.thads.config.ConfigCursor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class AbstractBallerItem extends AbstractBallerObject implements IBallerItem {

    protected final ConfigCursor LANG_ITEM;
    protected final ConfigCursor LANG;

    private Map<UUID, Long> cooldowns;

    private ItemStack ballerItemStack, legendaryItemStack;
    public Listener listener;
    private boolean legendaryEnabled;
    private List<String> aliases;
    private boolean registered = false;
    private double cooldown;

    public AbstractBallerItem(String name) {
        super(name);

        //FIX AND MAKE CONFIG TYPE UNIVERSAL

        LANG_ITEM = new ConfigCursor(Thads.get().getLangConfig(), "lol.weapons." + name);
        LANG = new ConfigCursor(Thads.get().getLangConfig(), "");
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
    public Boolean isLegendaryEnabled() {
        return legendaryEnabled;
    }

    @Override
    public void setLegendaryEnabled(boolean b) {
        this.legendaryEnabled = b;
    }

}
