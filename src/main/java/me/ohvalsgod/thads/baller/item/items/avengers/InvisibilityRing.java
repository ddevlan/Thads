package me.ohvalsgod.thads.baller.item.items.avengers;

import lombok.Getter;
import me.confuser.barapi.BarAPI;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class InvisibilityRing extends AbstractBallerItem {

    private HashMap<String, Integer> warmup;
    @Getter private static Set<String> invis;

    public InvisibilityRing() {
        super("invisibilityring");
        getAliases().add("ir");
        getAliases().add("invisring");
        setWeight(6.5);

        warmup = new HashMap<>();
        invis = new HashSet<>();

        listener = new IRListener();
    }

    public class IRListener implements Listener {
        @EventHandler
        public void onLeave(PlayerQuitEvent e) {
            invis.remove(e.getPlayer().getName());
        }

        @EventHandler
        public void onJoin(PlayerJoinEvent e) {
            Player player = e.getPlayer();
            for (String str : invis) {
                Player p = Bukkit.getPlayer(str);
                if (p != null) {
                    player.hidePlayer(p);
                } else {
                    invis.remove(str);
                }
            }
        }

        @EventHandler
        public void onMove(PlayerMoveEvent e) {
            if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
                Player player = e.getPlayer();
                boolean hasInvisRing = false;
                if (invis.contains(player.getName())) {
                    for (ItemStack i : player.getInventory().getContents()) {
                        if (BallerManager.getBallerManager().getByItemStack(i) instanceof InvisibilityRing) {
                            hasInvisRing = true;
                            break;
                        }
                    }

                    if (!hasInvisRing) {
                        for (Player other : Bukkit.getServer().getOnlinePlayers()) {
                            other.showPlayer(player);
                            invis.remove(player.getName());
                            BarAPI.removeBar(player);
                        }
                        player.sendMessage(LANG_ITEM.getString("no-longer-invisible"));
                    }
                }
            }
        }

        @EventHandler
        public void onPick(PlayerPickupItemEvent e) {
            if (invis.contains(e.getPlayer().getName())) {
                e.setCancelled(true);
            }
        }

        @EventHandler
        public void onHit(EntityDamageByEntityEvent e) {
            if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
                Player damager = (Player) e.getDamager();
                Player damaged = (Player) e.getEntity();

                if (invis.contains(damager.getName())) {
                    e.setCancelled(true);
                }

                if (warmup.containsKey(damaged.getName())) {
                    Bukkit.getServer().getScheduler().cancelTask(warmup.get(damager.getName()));
                    warmup.remove(damaged.getName());
                    damaged.sendMessage(LANG_ITEM.getString("hit-on-vanish"));
                }
            }
        }

        @EventHandler
        public void onInteract(PlayerInteractEvent e) {
            if (isEnabled()) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Player player = e.getPlayer();
                    if (BallerManager.getBallerManager().getByItemStack(player.getItemInHand()) instanceof InvisibilityRing) {
                        e.setCancelled(true);
                        if (invis.contains(player.getName())) {
                            for (Player other : Bukkit.getServer().getOnlinePlayers()) {
                                other.showPlayer(player);
                            }
                            invis.remove(player.getName());
                            BarAPI.removeBar(player);
                            player.sendMessage(LANG_ITEM.getString("no-longer-invisible"));
                        } else if (!warmup.containsKey(player.getName())) {
                            int warmup = 5;
                            player.sendMessage(LANG_ITEM.getString("vanishing-in").replace("%time%", String.valueOf(warmup)));
                            InvisibilityRing.this.warmup.put(player.getName(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Thads.getInstance(), new Runnable() {
                                @Override
                                public void run() {
                                    BarAPI.setMessage(LANG_ITEM.getString("now-invisible.bar"));
                                    player.sendMessage(LANG_ITEM.getString("now-invisible.chat"));
                                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                        p.hidePlayer(player);
                                    }
                                    invis.add(player.getName());
                                    InvisibilityRing.this.warmup.remove(player.getName());

                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            if (invis.contains(player.getName())) {
                                                BarAPI.setMessage(player, LANG_ITEM.getString("now-invisible.bar"));
                                            } else {
                                                cancel();
                                            }
                                        }
                                    }.runTaskTimer(Thads.getInstance(), 0L, 400L);
                                }
                            }, warmup * 20L));
                        }
                    }
                }
            }
        }
    }

}
