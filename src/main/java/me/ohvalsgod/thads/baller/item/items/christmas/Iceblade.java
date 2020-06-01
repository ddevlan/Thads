package me.ohvalsgod.thads.baller.item.items.christmas;

import lombok.Getter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.baller.item.items.avengers.InvisibilityRing;
import me.ohvalsgod.thads.data.PlayerData;
import me.ohvalsgod.thads.util.CC;
import me.ohvalsgod.thads.util.ParticleEffect;
import me.ohvalsgod.thads.util.WorldGuardUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.bossbar.BossBarAPI;

import java.util.HashSet;
import java.util.Set;

public class Iceblade extends AbstractBallerItem {

    @Getter private static Set<String> frozen;
    @Getter private static Set<String> runnable;

    public Iceblade() {
        super("iceblade");
        getAliases().add("ib");
        setWeight(5.5);
        frozen = new HashSet<>();
        runnable = new HashSet<>();
        listener = new IBListener();
    }

    public class IBListener implements Listener {
        @EventHandler
        public void onVelocityChange(PlayerVelocityEvent e) {
            if (frozen.contains(e.getPlayer().getName())) {
                e.setCancelled(true);
            }
        }

        @EventHandler
        public void onMove(PlayerMoveEvent e) {
            Player player = e.getPlayer();
            if (BossBarAPI.hasBar(player) && !BossBarAPI.getMessage(player).equalsIgnoreCase("Ender Dragon")) {
                BossBarAPI.removeBar(player);
            }

            if (frozen.contains(player.getName()) && e.getFrom().getY() < e.getTo().getY()) {
                e.setTo(new Location(player.getWorld(), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ(), e.getFrom().getYaw(), e.getFrom().getPitch()));
            }
        }

        @EventHandler
        public void onDamage(EntityDamageEvent e) {
            if (e.getEntity() instanceof Player) {
                Player player = (Player) e.getEntity();
                if (frozen.contains(player.getName())) {
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL) || e.getCause().equals(EntityDamageEvent.DamageCause.CONTACT)) {
                        e.setDamage(0);
                        e.setCancelled(true);
                    }
                }
            }
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent e) {
            e.getPlayer().setWalkSpeed(0.2f);
        }

        @EventHandler
        public void onSneak(PlayerToggleSneakEvent e) {
            if (isEnabled()) {
                final Player player = e.getPlayer();
                final PlayerData data = Thads.get().getPlayerDataHandler().getPlayerData(player.getUniqueId());
                if (BallerManager.getBallerManager().getItemByStack(player.getItemInHand()) instanceof Iceblade) {
                    if (!runnable.contains(player.getName())) {
                        new BukkitRunnable() {
                            Double radius = 0d;

                            @Override
                            public void run() {
                                if (player.isSneaking()) {
                                    if (this.radius <= 10) {
                                        if (expired(player) || runnable.contains(player.getName())) {
                                            runnable.add(player.getName());
                                            BossBarAPI.setMessage(player, ChatColor.AQUA + "" + ChatColor.BOLD + "Freeze Radius: " + Float.toString(radius.floatValue()));
                                            radius = radius + 1;
                                        } else {
                                            runnable.remove(player.getName());
                                            double cdf = remaining(player);
                                            player.sendMessage(LANG.getString("lol.error.cooldown").replace("{ITEM}", WordUtils.capitalizeFully(getName())).replace("{AMOUNT}", String.valueOf(cdf/1000)));
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
                    }
                }
            }
        }

        public void activate(final Player player, Double radius) {
            BossBarAPI.removeBar(player);
            cool(player);
            boolean b = false;
            runnable.remove(player.getName());
            int bb = 0;
            StringBuilder playersAffected = new StringBuilder();
            for (Entity p : player.getNearbyEntities(radius, radius, radius)) {
                if ((p instanceof Player)) {
                    final Player oo = (Player)p;
                    bb++;
                    if(WorldGuardUtil.isPlayerInPvP(player) && !InvisibilityRing.getInvis().contains(player.getName())) {
                        if (!frozen.contains(oo.getName())) {
                            b = true;
                            frozen.add(oo.getName());
                            player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 10.0F, 10.0F);
                            ParticleEffect.CLOUD.display(player.getLocation(), 1.0F, 1.0F, 1.0F, 1.0F, 30);
                            int ii = 3;
                            oo.setWalkSpeed(1.0E-004F);
                            BossBarAPI.setMessage(oo, CC.AQUA + "" + CC.BOLD + "You have been frozen by " + player.getName(), ii);
                            oo.sendMessage(CC.GRAY + "You have been frozen by " + CC.AQUA + player.getName());
                            playersAffected.append(CC.AQUA + oo.getName() + CC.GRAY + ", ");
                            ParticleEffect.CLOUD.display(oo.getLocation(), 1.0F, 1.0F, 1.0F, 1.0F, 40);
                            ParticleEffect.CLOUD.display(oo.getLocation(), 1.0F, 1.0F, 1.0F, 1.0F, 40);
                            oo.getWorld().playEffect(oo.getLocation(), Effect.STEP_SOUND, 79);
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Thads.get(), () -> {
                                frozen.remove(oo.getName());
                                BossBarAPI.removeBar(oo);
                                oo.setWalkSpeed(0.2F);
                            }, ii * 20L);
                        }
                    } else {
                        player.sendMessage(CC.RED + "You cannot use this weapon here.");
                        runnable.remove(player.getName());
                        break;
                    }
                }
            }
            if(playersAffected.length() > 0) {
                player.sendMessage(CC.GRAY + "You have frozen: " + playersAffected.toString());
            } else {
                player.sendMessage(CC.RED + "You did not affect anyone.");
            }
        }
    }
}
