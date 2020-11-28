package me.ohvalsgod.thads.mysterychest;

import lombok.Getter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.mysterychest.loot.MysteryChestLoot;
import org.bukkit.ChatColor;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Getter
public class MysteryChestManager implements Listener {

    private Set<MysteryChest> mysteryChests;
    private File mysteryChestsFile;

    public MysteryChestManager(JavaPlugin plugin) {
        mysteryChests = new HashSet<>();

        mysteryChestsFile = new File(Thads.get().getDataFolder(), "/mysterychests/");

        if (!mysteryChestsFile.exists()) {
            mysteryChestsFile.mkdirs();
        }

        for (File file : mysteryChestsFile.listFiles()) {
            if (file.getName().contains(".yml") && !file.getName().contains("-removed")) {
                mysteryChests.add(loadMysteryChest(file.getName().replace(".yml", "")));
            }
        }
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public MysteryChest getChestByName(String name) {
        for (MysteryChest chest : mysteryChests) {
            if (chest.getName().equalsIgnoreCase(name)) {
                return chest;
            }
        }
        return null;
    }

    public MysteryChest getByItemStack(ItemStack source) {
        if (source != null) {
            if (source.hasItemMeta()) {
                for (MysteryChest chest : mysteryChests) {
                    if (chest.toItemStack(0).getItemMeta().equals(source.getItemMeta())) {
                        return chest;
                    }
                }
            }
        }
        return null;
    }

    public MysteryChest loadMysteryChest(String name) {
        MysteryChest chest = new MysteryChest(name);

        File file = new File(mysteryChestsFile, name + ".yml");

        if (!file.exists()) {
            return chest;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        chest.setAuthor(UUID.fromString(config.getString("author")));
        chest.setColor(ChatColor.valueOf(config.getString("color")));
        chest.setCreationDate(config.getLong("creationDate"));

        List<MysteryChestLoot> loot = new ArrayList<>();

        if (!config.getStringList("lootSets").isEmpty()) {
            for (String string : config.getStringList("lootSets")) {
                loot.add(MysteryChestLoot.fromString(string));
            }
        }

        chest.getLoot().addAll(loot);

        return chest;
    }

    public void saveMysteryChest(MysteryChest chest) {
        File file = new File(mysteryChestsFile, chest.getName() + ".yml");

        if (file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("author", chest.getAuthor().toString());
        config.set("color", chest.getColor().name());
        config.set("creationDate", chest.getCreationDate());

        List<String> items = new ArrayList<>();

        for (MysteryChestLoot loot : chest.getLoot()) {
            items.add(loot.toString());
        }

        config.set("lootSets", items);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeMysteryChest(MysteryChest chest) {
        mysteryChests.remove(chest);

        File file = new File(mysteryChestsFile, chest.getName() + ".yml");

        if (!file.exists()) {
            return;
        }

        file.renameTo(new File(mysteryChestsFile, chest.getName() + "-removed.yml"));
    }

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getPlayer().getItemInHand() != null) {
                if (event.getPlayer().getItemInHand().hasItemMeta()) {
                    if (getByItemStack(event.getPlayer().getItemInHand()) != null) {
                        MysteryChest chest = getByItemStack(event.getPlayer().getItemInHand());
                        chest.open(event.getPlayer());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void place(BlockPlaceEvent event) {
        if (event.getPlayer().getItemInHand() != null) {
            if (event.getPlayer().getItemInHand().hasItemMeta()) {
                if (getByItemStack(event.getPlayer().getItemInHand()) != null) {
                    if (!event.isCancelled()) {
                        if (getByItemStack(event.getPlayer().getItemInHand()) != null) {
                            MysteryChest mysteryChest = getByItemStack(event.getPlayer().getItemInHand());
                            Chest chest = (Chest) event.getBlock();
                            MysteryChestLoot loot = mysteryChest.getRandomLoot();
                            loot.getContents().forEach(itemStack -> chest.getBlockInventory().addItem(itemStack));
                            //broadcast
                        }
                    }
                }
            }
        }
    }

}
