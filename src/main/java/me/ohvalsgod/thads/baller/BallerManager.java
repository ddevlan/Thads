package me.ohvalsgod.thads.baller;

import lombok.Getter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.armor.AbstractBallerArmor;
import me.ohvalsgod.thads.baller.armor.IBallerArmor;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.baller.item.IBallerItem;
import me.ohvalsgod.thads.baller.object.AbstractBallerObject;
import me.ohvalsgod.thads.baller.object.IBallerObject;
import me.ohvalsgod.thads.config.ConfigCursor;
import me.ohvalsgod.thads.util.ArmorBuilder;
import me.ohvalsgod.thads.util.ClassUtil;
import me.ohvalsgod.thads.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BallerManager {

    private final Thads thads;

    @Getter private Set<IBallerObject> ballerObjects;

    public BallerManager(Thads thads) {
        this.thads = thads;

        ballerObjects = new HashSet<>();
        System.out.println("[Thads] [INIT] There have been a total of " + generateBallerObjects() + " baller objects loaded.");
        ballerObjects.forEach(this::loadBallerObject);
        ballerObjects = ballerObjects.stream().sorted(Comparator.comparingDouble(IBallerObject::getWeight)).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /*
        Data handling for Baller Objects
     */

    private int generateBallerObjects() {
        String[] pkg = {"me.ohvalsgod.thads.baller.item.items.", "me.ohvalsgod.thads.baller.armor.armors."};
        String[][] ext = {{"defaults", "summer", "christmas", "avengers"}, {"defaults"}};

        int loaded = 0;
        for (int i = 0; i < 2; i++) {
            for (String string : ext[i]) {
                loaded = loaded + loadBallerObjectsFromPackage(pkg[i] + string);
            }
        }

        return loaded;
    }

    public void loadBallerObject(IBallerObject item) {
        ConfigCursor cursor = new ConfigCursor(thads.getBallerObjectConfig(), item.getName());

        cursor.setPath(cursor.getPath() + ".baller-" + (item instanceof IBallerItem ? "item":"armor"));

        item.setEnabled(cursor.getBoolean("enabled"));
        item.setBuyPrice(cursor.getInt("buy"));
        item.setSellPrice(cursor.getInt("sell"));

        /**
         * BallerObjects are loaded from file and into an ItemStack on generation.
         * Maybe possibly in the future this could be changed and it could happen on command for in-game item updates.
         */
        if (item instanceof IBallerItem) {
            IBallerItem ballerItem = (IBallerItem) item;

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

            ballerItem.setBallerItemStack(builder.build());

            cursor.setPath(item.getName() + ".legendary-item");

            builder = new ItemBuilder(Material.getMaterial(cursor.getString("material")));

            builder.name(cursor.getString("name"));
            builder.lore(cursor.getStringList("lore"));

            cursor.setPath(cursor.getPath() + ".enchants");
            for (String string : cursor.getKeys()) {
                builder.enchantment(Enchantment.getByName(string), cursor.getInt(string));
            }

            if (cursor.getBoolean("hide-flags")) {
                builder.hideFlags();
            }

            ballerItem.setLegendaryItemStack(builder.build());

            cursor.setPath(item.getName());

            ballerItem.setLegendaryEnabled(cursor.getBoolean("legendary-item.enabled"));
        } else if (item instanceof IBallerArmor) {
            IBallerArmor ballerArmor = (IBallerArmor) item;

            ArmorBuilder builder = new ArmorBuilder(cursor.getString("type"));

            builder.name(cursor.getString("name"));
            builder.lore(cursor.getStringList("lore"));

            cursor.set(cursor.getPath() + ".enchants");
            for (String string : cursor.getKeys()) {
                builder.enchant(Enchantment.getByName(string), cursor.getInt(string));
            }

            if (cursor.getBoolean("hide-flags")) {
                builder.hideFlags();
            }

            ballerArmor.setArmor(builder.build());
        }

        if (item.isEnabled()) {
            if (item.getListener() != null) {
                item.register();
            }
        }
    }

    private int loadBallerObjectsFromPackage(String pkg) {
        int i = 0;

        for (Class<?> clazz : ClassUtil.getClassesInPackage(Thads.get(), pkg)) {
            System.out.println(clazz.getName() + " - Super: " + clazz.getSuperclass().getName() + " - ");
//            if (clazz.getSuperclass().getSuperclass().equals(AbstractBallerObject.class)) {
//                try {
//                    AbstractBallerObject obj = (AbstractBallerObject) clazz.newInstance();
//                    ballerObjects.add(obj);
//                    i++;
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        }

        return i;
    }

    public AbstractBallerObject getBallerObjectByName(String name) {
        for (IBallerObject object : ballerObjects) {
            if (object.getName().equalsIgnoreCase(name)) {
                return (AbstractBallerObject) object;
            }
        }
        return null;
    }
    //

    /*
        BallerItem Methods
     */

    public Set<AbstractBallerItem> getBallerItems() {
        Set<AbstractBallerItem> ballerItems = new HashSet<>();
        ballerObjects.forEach(iBallerObject -> {
            if (iBallerObject instanceof AbstractBallerItem) {
                getBallerItems().add((AbstractBallerItem) iBallerObject);
            }
        });
        return ballerItems;
    }

    public Set<AbstractBallerItem> getLegendaryBallerItems() {
        return getBallerItems().stream().filter(AbstractBallerItem::isLegendaryEnabled).collect(Collectors.toSet());
    }

    public AbstractBallerItem getItemByName(String string) {
        for (AbstractBallerItem ballerItem : getBallerItems()) {
            if (ballerItem.getName().equalsIgnoreCase(string) || ballerItem.getAliases().contains(string.toLowerCase())) {
                return ballerItem;
            }
        }
        return null;
    }

    public AbstractBallerItem getItemByLore(List<String> lore) {
        for (AbstractBallerItem ballerItem : getBallerItems()) {
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

    public AbstractBallerItem getItemByStack(ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.hasItemMeta()) {
                if (itemStack.getItemMeta().hasLore()) {
                    return getItemByLore(itemStack.getItemMeta().getLore());
                }
            }
        }
        return null;
    }

    public AbstractBallerItem getFromLegendary(ItemStack source) {
        if (source != null) {
            if (source.hasItemMeta()) {
                if (source.getItemMeta().hasLore()) {
                    for (AbstractBallerItem item : getBallerItems()) {
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

    public void saveBallerItem(AbstractBallerItem item) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(Thads.get().getBallerObjectConfig().getFile());

        config.set(item.getName() + ".enabled", item.isEnabled());
        config.set(item.getName() + ".sell", item.getSellPrice());
        config.set(item.getName() + ".buy", item.getBuyPrice());
        config.set(item.getName() + ".legendary-item.enabled", item.isLegendaryEnabled());

        try {
            config.save(Thads.get().getBallerObjectConfig().getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AbstractBallerItem getByPlayer(Player player) {
        return getItemByStack(player.getItemInHand());
    }

    /*
        BallerArmor Methods
     */
    public Set<AbstractBallerArmor> getBallerArmor() {
        Set<AbstractBallerArmor> ballerArmor = new HashSet<>();
        ballerObjects.forEach(iBallerObject -> {
            if (iBallerObject instanceof AbstractBallerArmor) {
                ballerArmor.add((AbstractBallerArmor) iBallerObject);
            }
        });
        return ballerArmor;
    }

    public AbstractBallerArmor getArmorByName(String string) {
        for (AbstractBallerArmor ballerArmor : getBallerArmor()) {
            if (ballerArmor.getName().equalsIgnoreCase(string) || ballerArmor.getAliases().contains(string.toLowerCase())) {
                return ballerArmor;
            }
        }
        return null;
    }

    public void saveBallerArmor(AbstractBallerArmor item) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(Thads.get().getBallerObjectConfig().getFile());

        config.set(item.getName() + ".enabled", item.isEnabled());
        config.set(item.getName() + ".sell", item.getSellPrice());
        config.set(item.getName() + ".buy", item.getBuyPrice());

        try {
            config.save(Thads.get().getBallerObjectConfig().getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        The reason this method is written like this is to have different lores on multiple different armor pieces
        to provide future proof of armor sets that have different types.
     */
    public AbstractBallerArmor getArmorByLore(List<List<String>> lore) {
        for (AbstractBallerArmor armor : getBallerArmor()) {
            boolean match = false;

            int i = 0;
            for (ItemStack itemStack : armor.getBallerArmor()) {
                if (itemStack != null) {
                    if (itemStack.hasItemMeta()) {
                        if (itemStack.getItemMeta().hasLore()) {
                            if (itemStack.getItemMeta().getLore().equals(lore.get(i))) {
                                match = true;
                            } else {
                                match = false;
                            }
                        }
                    }
                }
                i++;
            }

            if (match) {
                return armor;
            }
        }
        return null;
    }

    public AbstractBallerArmor getArmorByItemStack(ItemStack source) {
        if (source != null) {
            if (source.hasItemMeta()) {
                if (source.getItemMeta().hasLore()) {
                    for (AbstractBallerArmor armor : getBallerArmor()) {
                        boolean match = false;

                        int i = 0;
                        for (ItemStack itemStack : armor.getBallerArmor()) {
                            if (itemStack != null) {
                                if (itemStack.hasItemMeta()) {
                                    if (itemStack.getItemMeta().hasLore()) {
                                        if (itemStack.getItemMeta().getLore().equals(source.getItemMeta().getLore())) {
                                            match = true;
                                            break;
                                        } else {
                                            match = false;
                                        }
                                    }
                                }
                            }
                            i++;
                        }

                        if (match) {
                            return armor;
                        }
                    }
                }
            }
        }
        return null;
    }

    public AbstractBallerArmor getArmorByPlayer(Player player) {
        List<List<String>> strings = new ArrayList<>();
        for (ItemStack itemStack : player.getInventory().getArmorContents()) {
            if (itemStack != null) {
                if (itemStack.hasItemMeta()) {
                    if (itemStack.getItemMeta().hasLore()) {
                        strings.add(itemStack.getItemMeta().getLore());
                    }
                }
            }
        }

        return getArmorByLore(strings);
    }

    /*
        Instance method
     */
    public static BallerManager getBallerManager() {
        return Thads.get().getBallerManager();
    }

}
