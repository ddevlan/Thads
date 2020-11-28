package me.ohvalsgod.thads.baller.item.items.summer;

import lombok.Getter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.util.WorldGuardUtil;
import org.apache.commons.lang3.RandomUtils;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class MagicFeather extends AbstractBallerItem {

    /*

     */

    @Getter private static Set<UUID> flying;
    private Map<UUID, Long> left;
    private Map<UUID, Float> wave;
    private Map<UUID, Location> infiniteFlightFix;
    private Map<UUID, Item> legendaryEntities;
    private BukkitRunnable task;

    public MagicFeather() {
        super("magicfeather");
        getAliases().add("mf");
        setWeight(6);

        listener = new MFListener();
    }


    @Override
    public void fixJrebel() {
        super.fixJrebel();
        flying = new HashSet<>();
        left = new HashMap<>();
        wave = new HashMap<>();
        infiniteFlightFix = new HashMap<>();
        legendaryEntities = new HashMap<>();
        if (task != null) task.cancel();
        task = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!(Thads.get().getBallerManager().getItemByStack(player.getItemInHand()) instanceof MagicFeather)) {
                        if (flying.contains(player.getUniqueId())) {
                            if (player.isFlying() || player.getAllowFlight()) {
                                disableFlight(player);
                            }
                        }
                    }
                    if (infiniteFlightFix.containsKey(player.getUniqueId()) && flying.contains(player.getUniqueId())) {
                        if (infiniteFlightFix.get(player.getUniqueId()).distance(player.getLocation()) < 0.33) {
                            disableFlight(player);
                        }
                    }
                }
            }
        };
    }


    @me.ohvalsgod.thads.listener.Listener
    public class MFListener implements Listener {

        @EventHandler
        public void onMove(PlayerMoveEvent event) {
            Player player = event.getPlayer();
            if (Thads.get().getBallerManager().getItemByStack(player.getItemInHand()) instanceof MagicFeather) {
                if (!WorldGuardUtil.isPlayerInPvP(player) || (WorldGuardUtil.isPlayerInPvP(player) && Thads.get().getBallerManager().getFromLegendary(player.getItemInHand()) instanceof MagicFeather && left.containsKey(player.getUniqueId()))) {

                    if (WorldGuardUtil.isPlayerInPvP(player) && left.containsKey(player.getUniqueId())) {
                        infiniteFlightFix.put(player.getUniqueId(), player.getLocation());
                    }

                    if (!flying.contains(player.getUniqueId()) && !left.containsKey(player.getUniqueId())) {
                        flying.add(player.getUniqueId());
                    }
                    player.setAllowFlight(true);
                    player.setFlying(true);

                    if (Thads.get().getBallerManager().getFromLegendary(player.getItemInHand()) instanceof MagicFeather) {
                        if (wave.containsKey(player.getUniqueId())) {
                            if (wave.get(player.getUniqueId()) > 1) {
                                wave.put(player.getUniqueId(), wave.get(player.getUniqueId()) - RandomUtils.nextFloat(0.75f/6, 1.5f/6));
                            } else {
                                wave.put(player.getUniqueId(), wave.get(player.getUniqueId()) + RandomUtils.nextFloat(0.75f / 4, 1.5f / 4));
                            }
                        } else {
                            wave.put(player.getUniqueId(), 1f);
                            player.getWorld().playSound(player.getLocation(), Sound.DOOR_OPEN, 0.75f, 1f);
                        }

                        if ((int)System.currentTimeMillis() % 40  == 0) {
                            player.getWorld().playSound(player.getLocation(), Sound.ORB_PICKUP, 0.75f, wave.get(player.getUniqueId()));
                        }

                        if ((int)System.currentTimeMillis() % 60 == 0) {
                            player.getWorld().playSound(player.getLocation(), Sound.ORB_PICKUP, 0.5f, wave.get(player.getUniqueId())*0.8725f);
                        }

                        if ((int)System.currentTimeMillis() % 70 == 0) {
                            player.getWorld().playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0f, wave.get(player.getUniqueId())*0.75f);
                        }

                        getDoubleHelix(3, player.getLocation()).forEach(location -> player.playEffect(player.getLocation(), Effect.HAPPY_VILLAGER, null));

                        for (float i = 0; i < .45; i = i + 0.1f) {
                            for (float i2 = 0; i2 < .45; i2 = i2 + 0.1f) {
                                for (float i3 = 0; i3 < .45; i3 = i3 + 0.1f) {
                                    player.getWorld().playEffect(player.getLocation(), Effect.TILE_BREAK, new MaterialData(Material.EMERALD_BLOCK, (byte)0));
                                    player.getWorld().playEffect(player.getLocation(), Effect.TILE_BREAK, new MaterialData(Material.WOOL, (byte)5));
//                                    player.spigot().playEffect(player.getLocation(), Effect.TILE_BREAK, Material.EMERALD_BLOCK.getId(), 0, 0, 0, 0, 1, 1, 64);
                                }
                            }
                        }
                    } else {
                        player.getWorld().playSound(player.getLocation(), Sound.STEP_WOOL, 0.85f, 0.85f);
                        for (float i = 0; i < .45; i = i + 0.1f) {
                            for (float i2 = 0; i2 < .45; i2 = i2 + 0.1f) {
                                for (float i3 = 0; i3 < .45; i3 = i3 + 0.1f) {
                                    player.getWorld().playEffect(player.getLocation(), Effect.TILE_BREAK, new MaterialData(Material.WOOL, (byte)0));
                                }
                            }
                        }
                    }
                }

                if (flying.contains(player.getUniqueId())) {
                    if (WorldGuardUtil.isPlayerInPvP(player) && Thads.get().getBallerManager().getFromLegendary(player.getItemInHand()) instanceof MagicFeather) {
                        if (!left.containsKey(player.getUniqueId())) {
                            left.put(player.getUniqueId(), System.currentTimeMillis());
                        }
                    } else {
                        left.remove(player.getUniqueId());
                    }
                }
            }

            if (flying.contains(player.getUniqueId())) {
                if (!(Thads.get().getBallerManager().getItemByStack(player.getItemInHand()) instanceof MagicFeather)) {
                    disableFlight(player);
                    left.remove(player.getUniqueId());
                    infiniteFlightFix.remove(player.getUniqueId());
                    return;
                }

                if ((left.containsKey(player.getUniqueId()) && System.currentTimeMillis() - left.get(player.getUniqueId()) > 3000)) {
                    disableFlight(player);
                    left.remove(player.getUniqueId());
                    infiniteFlightFix.remove(player.getUniqueId());
                } else {
                    if (left.containsKey(player.getUniqueId())) {
                        player.sendMessage("Flight done in: " + ((left.get(player.getUniqueId()) + 3000) - System.currentTimeMillis()) + "");
                    }
                }
                if (WorldGuardUtil.isPlayerInPvP(player)) {
                    if (!left.containsKey(player.getUniqueId())) {
                        disableFlight(player);
                        left.remove(player.getUniqueId());
                        infiniteFlightFix.remove(player.getUniqueId());
                    }
                }

                if ((WorldGuardUtil.isPlayerInPvP(player) && !left.containsKey(player.getUniqueId())) || !(Thads.get().getBallerManager().getItemByStack(player.getItemInHand()) instanceof MagicFeather) || (left.containsKey(player.getUniqueId()) && System.currentTimeMillis() - left.get(player.getUniqueId()) > 3000)) {
                    if (wave.containsKey(player.getUniqueId())) {
                        player.getWorld().playSound(player.getLocation(), Sound.DOOR_CLOSE, 0.75f, 1f);
                        wave.remove(player.getUniqueId());
                    }
                }
            }
        }

        @EventHandler
        public void onPickup(PlayerPickupItemEvent event) {
            legendaryEntities.remove(event.getPlayer().getUniqueId());
        }

        @EventHandler
        public void onToggleFlight(PlayerToggleFlightEvent event) {
            if (flying.contains(event.getPlayer().getUniqueId()) || (Thads.get().getBallerManager().getItemByStack(event.getPlayer().getItemInHand()) instanceof MagicFeather)) {
                event.setCancelled(true);
            }
        }

        @EventHandler
        public void onDrop(PlayerDropItemEvent event) {
            if (Thads.get().getBallerManager().getItemByStack(event.getItemDrop().getItemStack()) != null) {
                if (Thads.get().getBallerManager().getItemByStack(event.getItemDrop().getItemStack()) instanceof MagicFeather) {
                    if (Thads.get().getBallerManager().getFromLegendary(event.getItemDrop().getItemStack()) instanceof MagicFeather) {
                        Item item = event.getItemDrop();
                        legendaryEntities.put(event.getPlayer().getUniqueId(), item);
                        item.setVelocity(event.getPlayer().getEyeLocation().getDirection().multiply(1.3).normalize());
                    }
                    disableFlight(event.getPlayer());
                }
            }
        }
    }

    private void disableFlight(Player player) {
        player.setAllowFlight(false);
        player.setFlying(false);
        infiniteFlightFix.remove(player.getUniqueId());
        flying.remove(player.getUniqueId());
    }

    private List<Location> getCircle(double radius, int amount, Location center) {
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

    private List<Location> getInvertedCircle(double radius, int amount, Location center) {
        List<Location> locations = new ArrayList<>();

        double increment = (4 * Math.PI) / amount;

        for (int i = 0; i < amount; i++) {
            double angle = i * increment;

            double z = center.getX() + (radius * Math.cos(angle));
            double x = center.getZ() + (radius * Math.sin(angle));

            Location location = new Location(center.getWorld(), x, center.getY(), z);
            locations.add(location);
        }

        return locations;
    }

    private List<Location> getDoubleHelix(double radius, Location center) {
        List<Location> locations = new ArrayList<>();

        for (double y = center.getY() - 25; y <= center.getY() + 25; y+=0.05) {
            double x = radius * Math.cos(y);
            double z = radius * Math.sin(y);
            locations.add(new Location(center.getWorld(), x, y, z));
        }

        return locations;
    }

    private void bool(boolean bool, Player player) {
        player.sendMessage((bool ? ChatColor.GREEN:ChatColor.RED) + String.valueOf(bool));
    }

}
