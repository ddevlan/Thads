package me.ohvalsgod.thads.baller;

import lombok.Getter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.item.BallerItem;
import me.ohvalsgod.thads.config.ConfigCursor;
import me.ohvalsgod.thads.menu.ButtonListener;
import me.ohvalsgod.thads.util.ClassUtil;
import me.ohvalsgod.thads.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

@Getter
public class BallerManager {

    private List<BallerItem> ballerItems;
    private Thads thads;

    public BallerManager(Thads thads) {
        this.thads = thads;
        ballerItems = new ArrayList<>();

        thads.getServer().getPluginManager().registerEvents(new ButtonListener(), thads);

        initBallerItems();
    }

    /*
        BALLER ITEMS START
     */

    private void initBallerItems() {
        loadBallerItemsFromPackage(Thads.getInstance(), "me.ohvalsgod.thads.baller.item.items");

        for (BallerItem item : ballerItems) {
            loadBallerItem(item);
            if (item.getListener() != null && item.isEnabled()) {
                thads.getServer().getPluginManager().registerEvents(item.getListener(), thads);
                System.out.println("Listener loaded for " + item.getName() + " in class '" + item.getListener().getClass() + "'.");
            }
        }
        System.out.println("! - ! - ! - ! - ! - ! - ! - ! - ! - ! - ! - ! - ! - ! - ! ");
        ballerItems.sort(Comparator.comparingInt(BallerItem::getWeight));
        ballerItems.forEach(item -> System.out.println(item.getName()));
        System.out.println("! - ! - ! - ! - ! - ! - ! - ! - ! - ! - ! - ! - ! - ! - ! ");
    }

    public void loadBallerItemsFromPackage(Plugin plugin, String packageName) {
        int i = 0;
        for (Class<?> clazz : ClassUtil.getClassesInPackage(plugin, packageName)) {
            for (Class<?> clazz$ : clazz.getInterfaces()) {
                if (clazz$.equals(BallerItem.class)) {
                    try {
                        BallerItem item = (BallerItem) clazz.newInstance();
                        ballerItems.add(item);
                        System.out.println(item.getName() + " has been loaded from class " + clazz.getName());
                        i++;
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("There have been a total of " + i + " baller items loaded from package '" + packageName + "'.");
    }

    public Set<BallerItem> getLegendaryBallerItems() {
        Set<BallerItem> legendaries = new HashSet<>();

        for (BallerItem item : ballerItems) {
            if (item.isLegendaryItemEnabled()) {
                legendaries.add(item);
            }
        }

        return legendaries;
    }

    public BallerItem getByName(String string) {
        for (BallerItem ballerItem : ballerItems) {
            if (ballerItem.getName().equalsIgnoreCase(string) || ballerItem.getAliases().contains(string.toLowerCase())) {
                return ballerItem;
            }
        }
        return null;
    }

    public BallerItem getByLore(List<String> lore) {
        for (BallerItem ballerItem : ballerItems) {
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

    public BallerItem getByItemStack(ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.hasItemMeta()) {
                if (itemStack.getItemMeta().hasLore()) {
                    return getByLore(itemStack.getItemMeta().getLore());
                }
            }
        }
        return null;
    }

    public void loadBallerItem(BallerItem item) {
        //  Regular baller item
        final ConfigCursor ballerCursor = new ConfigCursor(thads.getBallerItemsConfig(), item.getName() + ".baller-item");

        ItemBuilder builder = new ItemBuilder(Material.getMaterial(ballerCursor.getString("material")));

        builder.name(ballerCursor.getString("name"));
        builder.lore(ballerCursor.getStringList("lore"));

        ballerCursor.setPath(ballerCursor.getPath() + ".enchants");
        for (String string : ballerCursor.getKeys()) {
            builder.enchantment(Enchantment.getByName(string), ballerCursor.getInt(string));
        }

        if (ballerCursor.getBoolean("hide-flags")) {
            builder.hideFlags();
        }

        item.setBallerItemStack(builder.build());

        //  Legendary baller item
        ballerCursor.setPath(item.getName() + ".legendary-item");

        ItemBuilder legendary = new ItemBuilder(Material.getMaterial(ballerCursor.getString("material")));

        legendary.name(ballerCursor.getString("name"));
        legendary.lore(ballerCursor.getStringList("lore"));

        ballerCursor.setPath(ballerCursor.getPath() + ".enchants");
        for (String string : ballerCursor.getKeys()) {
            legendary.enchantment(Enchantment.getByName(string), ballerCursor.getInt(string));
        }

        if (ballerCursor.getBoolean("hide-flags")) {
            legendary.hideFlags();
        }

        item.setLegendaryItemStack(legendary.build());

        //  Setup values
        ballerCursor.setPath(item.getName());

        item.setEnabled(ballerCursor.getBoolean("enabled"));
        item.setBuyPrice(ballerCursor.getInt("buy"));
        item.setSellPrice(ballerCursor.getInt("sell"));
        item.setLegendaryEnabled(ballerCursor.getBoolean("legendary-item.enabled"));
    }

    public BallerItem getByPlayer(Player player) {
        return getByItemStack(player.getItemInHand());
    }

    /*
        BALLER ITEMS END
     */

    public static BallerManager getBallerManager() {
        return Thads.getInstance().getBallerManager();
    }

}
