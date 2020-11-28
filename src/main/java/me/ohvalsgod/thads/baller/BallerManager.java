package me.ohvalsgod.thads.baller;

import lombok.Getter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.armor.AbstractBallerArmor;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.baller.object.AbstractBallerObject;
import me.ohvalsgod.thads.config.ConfigCursor;
import me.ohvalsgod.thads.config.FileConfig;
import me.ohvalsgod.thads.util.ClassUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BallerManager {

    private final JavaPlugin plugin;

    private FileConfig objectsConfig;

    @Getter private Set<AbstractBallerObject> ballerObjects;

    public BallerManager(JavaPlugin plugin, FileConfig objectsConfig) {
        this.plugin = plugin;
        this.objectsConfig = objectsConfig;

        ballerObjects = new HashSet<>();
        System.out.println("[Thads] [INIT] There have been a total of " + generateBallerObjects() + " baller objects loaded.");
        ballerObjects.forEach(this::loadBallerObject);
        ballerObjects = ballerObjects.stream().sorted(Comparator.comparingDouble(AbstractBallerObject::getWeight)).collect(Collectors.toCollection(LinkedHashSet::new));
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

    public void loadBallerObject(AbstractBallerObject object) {
        ConfigCursor cursor = new ConfigCursor(objectsConfig, object.getName());

        object.setEnabled(cursor.getBoolean("enabled"));
        object.setBuyPrice(cursor.getInt("buy"));
        object.setSellPrice(cursor.getInt("sell"));

        if (object instanceof AbstractBallerItem) {
            boolean b = cursor.getBoolean("legendary-item.enabled");
            if (b) {
                ((AbstractBallerItem) object).setLegendaryEnabled(b);
            }
        }
    }

    private int loadBallerObjectsFromPackage(String pkg) {
        int i = 0;

        for (Class<?> clazz : ClassUtil.getClassesInPackage(Thads.get(), pkg)) {
            if (clazz.getSuperclass() != null && clazz.getSuperclass().equals(AbstractBallerItem.class) || clazz.getSuperclass().equals(AbstractBallerArmor.class)) {
                try {
                    AbstractBallerObject object = (AbstractBallerObject) clazz.newInstance();
                    loadBallerObject(object);
                    ballerObjects.add(object);

                    if (object.getListener() != null) {
                        if (object.isEnabled()) {
                            object.register();
                        }
                    }

                    i++;
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }

        return i;
    }

    public AbstractBallerObject getBallerObjectByName(String name) {
        for (AbstractBallerObject object : ballerObjects) {
            if (object.getName().equalsIgnoreCase(name)) {
                return object;
            }
        }
        int i = 0;
        return null;
    }

    /*
        BallerItem Methods
     */

    public Set<AbstractBallerItem> getBallerItems() {
        Set<AbstractBallerItem> ballerItems = new HashSet<>();
        ballerObjects.forEach(iBallerObject -> {
            if (iBallerObject instanceof AbstractBallerItem) {
                ballerItems.add((AbstractBallerItem) iBallerObject);
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
                            if (item.getLegendaryItemStack().getItemMeta().getLore().equals(source.getItemMeta().getLore()) && !item.getBallerItemStack().getItemMeta().getLore().equals(source.getItemMeta().getLore())) {
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
        YamlConfiguration config = YamlConfiguration.loadConfiguration(objectsConfig.getFile());

        config.set(item.getName() + ".enabled", item.isEnabled());
        config.set(item.getName() + ".sell", item.getSellPrice());
        config.set(item.getName() + ".buy", item.getBuyPrice());
        config.set(item.getName() + ".legendary-item.enabled", item.isLegendaryEnabled());

        try {
            config.save(objectsConfig.getFile());
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
        YamlConfiguration config = YamlConfiguration.loadConfiguration(objectsConfig.getFile());

        config.set(item.getName() + ".enabled", item.isEnabled());
        config.set(item.getName() + ".sell", item.getSellPrice());
        config.set(item.getName() + ".buy", item.getBuyPrice());

        try {
            config.save(objectsConfig.getFile());
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

}
