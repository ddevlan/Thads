package me.ohvalsgod.thads.baller;

import lombok.Getter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.item.BallerItem;
import me.ohvalsgod.thads.config.ConfigCursor;
import me.ohvalsgod.thads.util.ClassUtil;
import me.ohvalsgod.thads.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class BallerManager {

    private Set<BallerItem> ballerItems;
    private ConfigCursor ballerCursor;

    public BallerManager(Thads thads) {
        ballerItems = new HashSet<>();
        ballerCursor = new ConfigCursor(thads.getBallerItemsConfig(), "");
        initBallerItems();
    }

    /*
        BALLER ITEMS START
     */

    private void initBallerItems() {
        //TODO: make a method to load all baller items from package
        loadBallerItemsFromPackage(Thads.getInstance(), "me.ohvalsgod.thads.baller.item.items");
 /*
        ballerItems.add(new Noobsblade());
        ballerItems.add(new Excalibur());
        ballerItems.add(new MorningWood());
        ballerItems.add(new SparringAxe());
        ballerItems.add(new DateRapist());
        ballerItems.add(new BattleAxe());
        ballerItems.add(new WifeBeater());
        ballerItems.add(new Ejacul8());
        ballerItems.add(new Iceblade());
        ballerItems.add(new InvisibilityRing());
*/
        for (BallerItem item : ballerItems) {
            if (item.getListener() != null && item.isEnabled()) {
                Thads.getInstance().getServer().getPluginManager().registerEvents(item.getListener(), Thads.getInstance());
            }
        }
    }

    public void loadBallerItemsFromPackage(Plugin plugin, String packageName) {
        for (Class<?> clazz : ClassUtil.getClassesInPackage(plugin, packageName)) {
            for (Class<?> clazz$ : clazz.getInterfaces()) {
                if (clazz$.equals(BallerItem.class)) {
                    try {
                        ballerItems.add((BallerItem) clazz.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Set<BallerItem> getLegendaryWeapons() {
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
        if (itemStack.hasItemMeta()) {
            if (itemStack.getItemMeta().hasLore()) {
                return getByLore(itemStack.getItemMeta().getLore());
            }
        }
        return null;
    }

    public void loadBallerItem(BallerItem item) {
        //  Regular baller item
        ballerCursor.setPath(item.getName() + ".baller-item");

        ItemBuilder builder = new ItemBuilder(Material.valueOf(ballerCursor.getString("material")));

        builder.name(ballerCursor.getString("name"));
        builder.lore(ballerCursor.getStringList("lore"));

        ballerCursor.setPath(ballerCursor.getPath() + ".enchants");
        for (String string : ballerCursor.getKeys()) {
            builder.enchantment(Enchantment.getByName(string), ballerCursor.getInt(string));
        }

        item.setBallerItemStack(builder.build());

        //  Legendary baller item
        ballerCursor.setPath(item.getName() + ".legendary-item");

        ItemBuilder legendary = new ItemBuilder(Material.valueOf(ballerCursor.getString("material")));

        legendary.name(ballerCursor.getString("name"));
        legendary.lore(ballerCursor.getStringList("lore"));

        ballerCursor.setPath(ballerCursor.getPath() + ".enchants");
        for (String string : ballerCursor.getKeys()) {
            legendary.enchantment(Enchantment.getByName(string), ballerCursor.getInt(string));
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
