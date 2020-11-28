package me.ohvalsgod.thads.baller.item.items.avengers;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.util.WorldGuardUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class SpidermanBow extends AbstractBallerItem {

    private ArrayList<Entity> arrows;

    public SpidermanBow() {
        super("spidermanbow");
        getAliases().add("smb");
        setWeight(4.75);
        listener = new SMBListener();
        arrows = new ArrayList<>();
    }



    @me.ohvalsgod.thads.listener.Listener
    public class SMBListener implements Listener {

        @EventHandler
        public void onDamage(EntityDamageByEntityEvent event) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if (event.getDamager() instanceof Arrow) {
                    Arrow arrow = (Arrow) event.getDamager();
                    if (WorldGuardUtil.isPlayerInPvP(player)) {
                        if (arrows.contains(arrow)) {
                            Location web = arrow.getLocation();
                            Material lastType = web.getBlock().getType();
                            player.getWorld().getBlockAt(web).setType(Material.WEB);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.getWorld().getBlockAt(web).setType(lastType);
                                }
                            }.runTaskLater(Thads.get(), 20);
                        }
                    }
                }
            }
        }

        @EventHandler
        public void onHit(ProjectileHitEvent event) {
            if (arrows.contains(event.getEntity())) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Thads.get(), () -> arrows.remove(event.getEntity()), 3L);
            }
        }
    }

    @EventHandler
    public void onBowShot(EntityShootBowEvent event) {
        if (isEnabled()) {
            if (event.getProjectile() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getProjectile();
                ItemStack bow = event.getBow();

                if (Thads.get().getBallerManager().getItemByStack(bow) instanceof SpidermanBow) {
                    arrows.add(arrow);
                }
            }
        }
    }

}