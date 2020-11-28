package me.ohvalsgod.thads.baller.item.items.summer;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.util.StreamUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class MagicJohnson extends AbstractBallerItem {

    private Map<UUID, Item> ballers;

    public MagicJohnson() {
        super("magicjohnson");
        ballers = new HashMap<>();
        listener = new MJListener();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, Item> entry : ballers.entrySet()) {
                    if (entry.getValue().getLocation().getBlock().getDrops().contains(new ItemStack(Material.STRING))) {
                        ballers.remove(entry.getKey());
                        Player player = Bukkit.getPlayer(entry.getKey());
                        if (player != null) {
                            if (player.getInventory().firstEmpty() > -1) {
                                player.getInventory().addItem(entry.getValue().getItemStack());
                            } else {
                                player.getWorld().dropItem(player.getEyeLocation(), entry.getValue().getItemStack());
                            }
                            entry.getValue().remove();

                            int amount = 15;
                            final int[] curr = {0};

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (curr[0] < amount) {
                                        for (double i = 0; i < 12; i = i + 1) {
                                            Location center = entry.getValue().getLocation().clone();
                                            center.setY(center.getY() + i);
                                            List<Location> circle = getCircle(i, 90*(i*0.5), center);

                                            for (Location location : circle) {
                                                location.getWorld().spigot().playEffect(location, Effect.FLAME, 0, 0, 0, 0, 0, 0, 1, 128);
                                            }

                                            center = entry.getValue().getLocation().clone();
                                            center.setY(center.getY() - i);
                                            circle = getCircle(i, 90*(i*0.5), center);
                                            for (Location location : circle) {
                                                location.getWorld().spigot().playEffect(location, Effect.FLAME, 0, 0, 0, 0, 0, 0, 1, 128);
                                            }
                                        }
                                    }
                                    curr[0]++;
                                }
                            }.runTaskTimer(Thads.get(), 0, 4);
                        }
                    }

                    if (Bukkit.getPlayer(entry.getKey()) != null) {
                        List<Location> circle = getCircle(0.33, 45, entry.getValue().getLocation());

                        for (Location location : circle) {
                            location.getWorld().spigot().playEffect(location, Effect.FLAME, 0, 0, 0, 0, 0, 0, 1, 128);
                        }
                    }
                }
            }
        }.runTaskTimer(Thads.get(), 0, 0);
    }

    private List<Location> getCircle(double radius, double amount, Location center) {
        List<Location> locations = new ArrayList<>();

        double increment = (4 * Math.PI) / amount;

        for (int i = 0; i < amount; i++) {
            double angle = i * increment;

            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));

            Location location = new Location(center.getWorld(), x, center.getY(), z);

            Vector offset = center.getDirection().clone().multiply(radius * Math.sin(angle));
            offset.setY(Math.cos(angle) * radius);
            location.add(offset.normalize());

            locations.add(location);
        }

        return locations;
    }


    @me.ohvalsgod.thads.listener.Listener
    public class MJListener implements Listener {

        @EventHandler
        public void onDrop(PlayerDropItemEvent event) {
            Item item = event.getItemDrop();
            ItemStack itemStack = event.getItemDrop().getItemStack();

            if (Thads.get().getBallerManager().getItemByStack(itemStack) != null) {
                if (Thads.get().getBallerManager().getItemByStack(itemStack) instanceof MagicJohnson) {
                    ballers.put(event.getPlayer().getUniqueId(), item);
                    item.setVelocity(event.getPlayer().getLocation().getDirection().multiply(1.33));
                    FixedMetadataValue value = new FixedMetadataValue(Thads.get(), true);
                    item.setMetadata("mj_merge", value);
                }
            }
        }

//        @EventHandler
//        public void onMerge(ItemMergeEvent event) {
//            if (Thads.get().getBallerManager().getItemByStack(event.getEntity().getItemStack()) != null) {
//                if (Thads.get().getBallerManager().getItemByStack(event.getEntity().getItemStack()) instanceof MagicJohnson) {
//                    if (event.getEntity().hasMetadata("mj_merge")) {
//                        MetadataValue value = event.getEntity().getMetadata("mj_merge").get(0);
//
//                        if (value.asBoolean()) {
//                            event.setCancelled(true);
//                        }
//                    }
//                }
//            }
//        }

        @EventHandler
        public void onPickup(PlayerPickupItemEvent event) {
            if (ballers.containsValue(event.getItem())) {
                ballers.remove(StreamUtils.getKey(ballers, event.getItem()));
            }
        }

    }

    private List<Location> getDoubleHelix(double radius, Location center) {
        List<Location> locations = new ArrayList<>();

        for (double y = center.getY() - 5; y <= center.getY() + 25; y+=0.10) {
            double x = radius * Math.cos(y);
            double z = radius * Math.sin(y);
            locations.add(new Location(center.getWorld(), center.getX() - x, center.getY() + y, center.getZ() - z));
        }

        return locations;
    }

}
