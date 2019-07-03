package me.ohvalsgod.thads.baller;

import lombok.Getter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.armor.AbstractBallerArmor;
import me.ohvalsgod.thads.baller.gui.weapons.LOLWeaponsMenu;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.config.ConfigCursor;
import me.ohvalsgod.thads.util.ArmorBuilder;
import me.ohvalsgod.thads.util.ClassUtil;
import me.ohvalsgod.thads.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BallerManager {

    private final Thads thads;

    @Getter private List<AbstractBallerItem> ballerItems;
    @Getter private List<AbstractBallerArmor> ballerArmors;
    @Getter private LOLWeaponsMenu menu;

    public BallerManager(Thads thads) {
        this.thads = thads;

        ballerItems = new ArrayList<>();
        ballerArmors = new ArrayList<>();

        init();

        this.menu = new LOLWeaponsMenu();
    }

    private void init() {
        loadBallerItemsFromPackage(Thads.getInstance(), "me.ohvalsgod.thads.baller.item.items.avengers");
        loadBallerItemsFromPackage(Thads.getInstance(), "me.ohvalsgod.thads.baller.item.items.defaults");
        loadBallerItemsFromPackage(Thads.getInstance(), "me.ohvalsgod.thads.baller.item.items.summer");
        loadBallerItemsFromPackage(Thads.getInstance(), "me.ohvalsgod.thads.baller.item.items.christmas");

        loadBallerArmorFromPackage(Thads.getInstance(), "me.ohvalsgod.thads.baller.armor.armors.defaults");
        loadBallerArmorFromPackage(Thads.getInstance(), "me.ohvalsgod.thads.baller.armor.armors.avengers");

        for (AbstractBallerItem item : ballerItems) {
            loadBallerItem(item);
            if (item.getListener() != null) {
                item.register();
            }
        }
        ballerItems.sort(Comparator.comparingDouble(AbstractBallerItem::getWeight));
    }

    public LOLWeaponsMenu getWeaponsMenu() {
        return menu;
    }

    /*
        BALLER ARMOR START
                            */
    public void loadBallerArmorFromPackage(Plugin plugin, String packageName) {
        int i = 0;
        for (Class<?> clazz : ClassUtil.getClassesInPackage(plugin, packageName)) {
            if (clazz.getSuperclass().equals(me.ohvalsgod.thads.baller.armor.AbstractBallerArmor.class)) {
                try {
                    me.ohvalsgod.thads.baller.armor.AbstractBallerArmor item = (me.ohvalsgod.thads.baller.armor.AbstractBallerArmor) clazz.newInstance();
                    ballerArmors.add(item);
                    i++;
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("[Thads] [INIT] There have been a total of " + i + " baller armor sets loaded from package '" + packageName + "'.");
    }

    public void loadBallerArmor(me.ohvalsgod.thads.baller.armor.AbstractBallerArmor armor) {
        final ConfigCursor cursor = new ConfigCursor(thads.getBallerArmorConfig(), armor.getName() + ".baller-armor");

        //  Regular baller armor
        ArmorBuilder builder = new ArmorBuilder(cursor.getString("chests"));

        builder.name(cursor.getString("name"));
        builder.lore(cursor.getStringList("lore"));

        cursor.setPath(cursor.getPath() + ".enchants");
        for (String string : cursor.getKeys()) {
            builder.enchant(Enchantment.getByName(string), cursor.getInt(string));
        }

        if (cursor.getBoolean("hide-flags")) {
            builder.hideFlags();
        }
    }
    /*
        BALLER ARMOR END
                            */

    /*
        BALLER ITEMS START
                            */

    public void loadBallerItemsFromPackage(Plugin plugin, String packageName) {
        int i = 0;
        for (Class<?> clazz : ClassUtil.getClassesInPackage(plugin, packageName)) {
            if (clazz.getSuperclass().equals(AbstractBallerItem.class)) {
                try {
                    AbstractBallerItem item = (AbstractBallerItem) clazz.newInstance();
                    ballerItems.add(item);
                    i++;
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("[Thads] [INIT] There have been a total of " + i + " baller items loaded from package '" + packageName + "'.");
    }

    public AbstractBallerItem getFromLegendary(ItemStack source) {
        if (source != null) {
            if (source.hasItemMeta()) {
                if (source.getItemMeta().hasLore()) {
                    for (AbstractBallerItem item : ballerItems) {
                        if (item.isLegendaryEnabled()) {
                            if (item.getLegendaryItemStack().getItemMeta().getLore().equals(source.getItemMeta().getLore())) {
                                return item;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public Set<AbstractBallerItem> getLegendaryBallerItems() {
        return ballerItems.stream().filter(AbstractBallerItem::isLegendaryEnabled).collect(Collectors.toSet());
    }

    public AbstractBallerItem getByName(String string) {
        for (AbstractBallerItem ballerItem : ballerItems) {
            if (ballerItem.getName().equalsIgnoreCase(string) || ballerItem.getAliases().contains(string.toLowerCase())) {
                return ballerItem;
            }
        }
        return null;
    }

    public AbstractBallerItem getByLore(List<String> lore) {
        for (AbstractBallerItem ballerItem : ballerItems) {
            ItemStack itemStack = ballerItem.getBallerItemStack();
            ItemStack legItemStack = ballerItem.getLegendaryItemStack();
            if (itemStack.hasItemMeta()) {
                if (itemStack.getItemMeta().hasLore()) {
                    if (itemStack.getItemMeta().getLore().equals(lore)) {
                        return ballerItem;
                    }
                }
            }

            if (legItemStack.hasItemMeta()) {
                if (legItemStack.getItemMeta().hasLore()) {
                    if (legItemStack.getItemMeta().getLore().equals(lore)) {
                        return ballerItem;
                    }
                }
            }
        }
        return null;
    }

    public AbstractBallerItem getByItemStack(ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.hasItemMeta()) {
                if (itemStack.getItemMeta().hasLore()) {
                    return getByLore(itemStack.getItemMeta().getLore());
                }
            }
        }
        return null;
    }

    public void saveBallerItem(AbstractBallerItem item) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(Thads.getInstance().getBallerItemsConfig().getFile());

        config.set(item.getName() + ".enabled", item.isEnabled());
        config.set(item.getName() + ".sell", item.getSellPrice());
        config.set(item.getName() + ".buy", item.getBuyPrice());
        config.set(item.getName() + ".legendary-item.enabled", item.isLegendaryEnabled());

        try {
            config.save(Thads.getInstance().getBallerItemsConfig().getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadBallerItem(AbstractBallerItem item) {
        final ConfigCursor cursor = new ConfigCursor(thads.getBallerItemsConfig(), item.getName() + ".baller-item");

        //  Regular baller item
        ItemBuilder builder = new ItemBuilder(Material.getMaterial(cursor.getString("material")));

        builder.name(cursor.getString("name"));
        builder.lore(cursor.getStringList("lore"));

        cursor.setPath(cursor.getPath() + ".enchants");
        for (String string : cursor.getKeys()) {
            builder.enchantment(Enchantment.getByName(string), cursor.getInt(string));
        }

        if (cursor.getBoolean("hide-flags")) {
            builder.hideFlags();
        }

        item.setBallerItemStack(builder.build());

        //  Legendary baller item
        cursor.setPath(item.getName() + ".legendary-item");

        ItemBuilder legendary = new ItemBuilder(Material.getMaterial(cursor.getString("material")));

        legendary.name(cursor.getString("name"));
        legendary.lore(cursor.getStringList("lore"));

        cursor.setPath(cursor.getPath() + ".enchants");
        for (String string : cursor.getKeys()) {
            legendary.enchantment(Enchantment.getByName(string), cursor.getInt(string));
        }

        if (cursor.getBoolean("hide-flags")) {
            legendary.hideFlags();
        }

        item.setLegendaryItemStack(legendary.build());

        //  Setup values
        cursor.setPath(item.getName());

        item.setEnabled(cursor.getBoolean("enabled"));
        item.setBuyPrice(cursor.getInt("buy"));
        item.setSellPrice(cursor.getInt("sell"));
        item.setLegendaryEnabled(cursor.getBoolean("legendary-item.enabled"));
    }

    public AbstractBallerItem getByPlayer(Player player) {
        return getByItemStack(player.getItemInHand());
    }

    /*
        BALLER ITEMS END
                           */

    public static BallerManager getBallerManager() {
        return Thads.getInstance().getBallerManager();
    }

}
