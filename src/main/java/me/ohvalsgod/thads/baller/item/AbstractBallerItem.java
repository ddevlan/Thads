package me.ohvalsgod.thads.baller.item;

import lombok.Getter;
import lombok.Setter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.object.AbstractBallerObject;
import me.ohvalsgod.thads.config.ConfigCursor;
import me.ohvalsgod.thads.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class AbstractBallerItem extends AbstractBallerObject implements IBallerItem {

    private Map<UUID, Long> cooldowns;

    private boolean legendaryEnabled = false;
    private List<String> aliases;
    private boolean registered = false;
    @Setter @Getter
    private double cooldown;

    public AbstractBallerItem(String name) {
        super(name);

        aliases = new ArrayList<>();
        cooldowns = new HashMap<>();
    }

    @Override
    public void fixJrebel() {
        super.fixJrebel();

        aliases = new ArrayList<>();
        cooldowns = new HashMap<>();
    }

    public boolean expired(Player player) {
        return cooldowns.getOrDefault(player.getUniqueId(), Long.MAX_VALUE) + cooldown*1000 > System.currentTimeMillis();
    }

    public void cool(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public double remaining(Player player) {
        return cooldowns.get(player.getUniqueId()) + cooldown*1000 - System.currentTimeMillis();
    }
    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public ItemStack getBallerItemStack() {
        ConfigCursor cursor = new ConfigCursor(Thads.get().getBallerObjectConfig(), getName() + ".baller-item");

        ItemBuilder builder = new ItemBuilder(Material.valueOf(cursor.getString("material")));
        builder.name(cursor.getString("name"));
        builder.lore(cursor.getStringList("lore"));
        if (!cursor.getKeys("enchants").isEmpty()) {
            for (String string : cursor.getKeys("enchants")) {
                builder.enchantment(Enchantment.getByName(string), cursor.getInt("enchants." + string));
            }
        }

        if (cursor.parentPath().getBoolean("hide-flags")) {
            builder.hideFlags();
        }

        return builder.build();
    }

    @Override
    public ItemStack getLegendaryItemStack() {
        ConfigCursor cursor = new ConfigCursor(Thads.get().getBallerObjectConfig(), getName() + ".legendary-item");

        ItemBuilder builder = new ItemBuilder(Material.valueOf(cursor.getString("material")));

        if (cursor.getString("material").equalsIgnoreCase("AIR")) {
            builder.type(Material.BEDROCK);
        }

        builder.name(cursor.getString("name"));
        builder.lore(cursor.getStringList("lore"));
        if (!cursor.getKeys(".enchants").isEmpty()) {
            for (String string : cursor.getKeys(".enchants")) {
                builder.enchantment(Enchantment.getByName(string), cursor.getInt("enchants." + string));
            }
        }

        if (cursor.parentPath().getBoolean("hide-flags")) {
            builder.hideFlags();
        }

        return builder.build();
    }

    //TODO
    public boolean check(ItemStack item, boolean legendary) {
        if (item != null) {
            if (item.hasItemMeta()) {
                if (legendary) {
                    if (item.getItemMeta().getLore().equals(getLegendaryItemStack().getItemMeta().getLore()) && item.getItemMeta().getDisplayName().equalsIgnoreCase(getLegendaryItemStack().getItemMeta().getDisplayName())) {
                        return Thads.get().getBallerManager().getItemByName(getName()) == this;
                    }
                } else {
                    if (item.getItemMeta().getLore().equals(getBallerItemStack().getItemMeta().getLore()) && item.getItemMeta().getDisplayName().equalsIgnoreCase(getBallerItemStack().getItemMeta().getDisplayName())) {
                        return Thads.get().getBallerManager().getItemByName(getName()) == this;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Boolean isLegendaryEnabled() {
        return legendaryEnabled;
    }

    @Override
    public void setLegendaryEnabled(boolean b) {
        this.legendaryEnabled = b;
    }

    @Override
    public void give(Player player) {
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), getBallerItemStack());
        } else {
            player.getInventory().addItem(getBallerItemStack());
        }
    }

    public void giveLegendary(Player player) {
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), getLegendaryItemStack());
        } else {
            player.getInventory().addItem(getLegendaryItemStack());
        }
    }


}
