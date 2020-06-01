package me.ohvalsgod.thads.baller.item.items.summer;

import lombok.Getter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.baller.item.items.avengers.InvisibilityRing;
import me.ohvalsgod.thads.util.CC;
import me.ohvalsgod.thads.util.LocationUtil;
import me.ohvalsgod.thads.util.WorldGuardUtil;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldEvent;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.inventivetalent.bossbar.BossBarAPI;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PianoKey extends AbstractBallerItem {

    //TODO: have to add song selector

    @Getter
    private static Set<String> runnable;

    public PianoKey() {
        super("pianokey");
        getAliases().add("pianosword");
        getAliases().add("pkey");
        setWeight(5.5);
        runnable = new HashSet<>();
        listener = new PKListener();
    }



    public class PKListener implements Listener {

        private void playRecord(Player player, Location location, Integer record) {
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldEvent(1005, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), record, false));
        }

        private void stopRecord(Player player, Location location) {
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldEvent(1005, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), 0, false));
        }

        private void activate(Player player, Double radius) {
            World world = player.getWorld();
            //TODO: cooldown
            BossBarAPI.removeBar(player);
            if (WorldGuardUtil.isPlayerInPvP(player)) {
                if (!InvisibilityRing.getInvis().contains(player.getName())) {
                    for (Location location : LocationUtil.getCircle(player.getLocation(), radius, radius.intValue() * 3)) {
                        world.playEffect(location, Effect.NOTE, 50);
                    }
                    List<Entity> victims = player.getNearbyEntities(radius, radius, radius);
                    for (Entity entity : victims) {
                        if (entity instanceof Damageable) {
                            Damageable d = (Damageable) entity;
                            if (WorldGuardUtil.isEntityInPVP(entity)) {
                                new BukkitRunnable() {
                                    int pitch = 0;
                                    @Override
                                    public void run() {
                                        if (pitch < 15) {
                                            d.getWorld().playSound(d.getLocation(), Sound.NOTE_PIANO, 1f, 0.2f * (pitch + radius.floatValue()));
                                            for (int x = 0; x < 2; x++) {
                                                d.getWorld().playEffect(d.getLocation().add(new Vector(Thads.RANDOM.nextInt(2), 1.5, Thads.RANDOM.nextInt(2))), Effect.NOTE, 2);
                                            }
                                        } else {
                                            cancel();
                                        }
                                        pitch++;
                                    }
                                }.runTaskTimer(Thads.get(), 0L, 2L);

                                d.setHealth(d.getHealth() > 4 ? d.getHealth() - 4:0);
                                d.playEffect(EntityEffect.HURT);
                            }
                        }
                    }

                    if (victims.isEmpty()) {
                        player.sendMessage(CC.RED + "You did not affect anyone.");
                    }
                } else {
                    player.sendMessage(LANG.getString("lol.error.non-invis-ring"));
                }
            } else {
                player.sendMessage(LANG.getString("lol.error.non-pvp-area"));
            }
        }

        @EventHandler
        public void onSneak(PlayerToggleSneakEvent event) {
            Player player = event.getPlayer();
            if (BallerManager.getBallerManager().getItemByStack(player.getItemInHand()) instanceof PianoKey) {
                if (expired(player)) {
                    new BukkitRunnable() {
                        Double radius = 0d;

                        @Override
                        public void run() {
                            if (player.isSneaking()) {
                                if (this.radius <= 10) {
                                    if (expired(player) || runnable.contains(player.getName())) {
                                        runnable.add(player.getName());
                                        BossBarAPI.setMessage(player, ChatColor.AQUA + "" + ChatColor.BOLD + "Music Radius: " + Float.toString(radius.floatValue()));
                                        radius = radius + 1;
                                    } else {
                                        runnable.remove(player.getName());
                                        double cd = remaining(player)/1000;
                                        player.sendMessage(LANG.getString("lol.error.cooldown").replace("{ITEM}", "Piano Key").replace("{AMOUNT}", String.valueOf(cd)));
                                        cancel();
                                    }
                                } else {
                                    activate(player, radius);
                                    cancel();
                                }
                            } else {
                                if (this.radius > 0) {
                                    activate(player, radius);
                                    cancel();
                                }
                            }
                        }
                    }.runTaskTimer(Thads.get(), 0L, 20L);
                } else {
                    player.sendMessage(LANG.getString("lol.error.cooldown").replace("{ITEM}", WordUtils.capitalizeFully(getName())).replace("{AMOUNT}", String.valueOf(remaining(player)/1000)));
                }
            }
        }

        @EventHandler
        public void onDamage(EntityDamageByEntityEvent event) {
            if (event.getDamager() instanceof Player) {
                Player player = (Player) event.getDamager();

                if (BallerManager.getBallerManager().getItemByStack(player.getItemInHand()) instanceof PianoKey) {
                    player.getWorld().playEffect(player.getLocation().add(new Vector(0,1,0)), Effect.NOTE, 2);
                    player.getWorld().playEffect(player.getLocation().add(new Vector(0,1,1)), Effect.NOTE, 2);
                    player.getWorld().playEffect(player.getLocation().add(new Vector(1,1,0)), Effect.NOTE, 2);
                }
            }
        }

        @EventHandler
        public void onDeath(PlayerDeathEvent event) {
            Player player = event.getEntity();
            if (player.getKiller() != null) {
                if (player.getKiller() instanceof Player) {
                    Player killer = player.getKiller();
                    if (BallerManager.getBallerManager().getItemByStack(killer.getItemInHand()) instanceof PianoKey) {
                        String id = player.getItemInHand().getItemMeta().getLore().get(3).split(":")[1].substring(1);
                        for (Player other : Bukkit.getServer().getOnlinePlayers()) {
                            final Location location = other.getLocation().clone();
                            playRecord(player, location, Integer.parseInt(CC.strip(id)));
                            Thads.get().getServer().getScheduler().runTaskLater(Thads.get(), new Runnable() {
                                @Override
                                public void run() {
                                    stopRecord(other, location);
                                }
                            }, 20 * 15);
                        }
                    }
                }
            }
        }
    }

}
