package me.ohvalsgod.thads.baller.item.items.defaults;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.util.WorldGuardUtil;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class AEBow extends AbstractBallerItem {

    private ArrayList<Entity> arrows;

    public AEBow() {
        super("aebow");
        getAliases().add("allenchantsbow");
        setWeight(4.75);
        listener = new AEBOWListener();
        arrows = new ArrayList<>();
    }

    public class AEBOWListener implements Listener {
        @EventHandler
        public void onDamage(EntityDamageByEntityEvent event) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if (event.getDamager() instanceof Arrow) {
                    Arrow arrow = (Arrow) event.getDamager();
                    if (WorldGuardUtil.isPlayerInPvP(player)) {
                        if (arrows.contains(arrow)) {
                            player.getWorld().strikeLightning(player.getLocation());
                            player.getWorld().playEffect(player.getLocation(), Effect.POTION_BREAK, 8260);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10 * 20, 0));
                        }
                    }
                }
            }
        }

        @EventHandler
        public void onHit(ProjectileHitEvent event) {
            if (arrows.contains(event.getEntity())) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Thads.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        arrows.remove(event.getEntity());
                    }
                }, 3L);
            }
        }
    }

    @EventHandler
    public void onBowShot(EntityShootBowEvent event) {
        if (isEnabled()) {
            if (event.getProjectile() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getProjectile();
                ItemStack bow = event.getBow();

                if (BallerManager.getBallerManager().getByItemStack(bow) instanceof AEBow) {
                    arrows.add(arrow);
                }
            }
        }
    }

}
