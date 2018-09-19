package me.ohvalsgod.thads.baller.item.items.defaults;

import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.baller.item.items.avengers.InvisibilityRing;
import me.ohvalsgod.thads.util.WorldGuardUtil;
import org.bukkit.Effect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Ejacul8 extends AbstractBallerItem {

    public Ejacul8() {
        super("ejacul8");
        getAliases().add("ejac");
        setWeight(5);
        listener = new EJACListener();
    }

    public class EJACListener implements Listener {
        @EventHandler
        public void onDamage(EntityDamageByEntityEvent e) {
            if (e.getDamager() instanceof Player)  {
                Player player = (Player) e.getDamager();
                if (e.getEntity() instanceof LivingEntity) {
                    LivingEntity damaged = (LivingEntity) e.getEntity();

                    if (BallerManager.getBallerManager().getByItemStack(player.getItemInHand()) instanceof Ejacul8) {
                        if (damaged instanceof Player) {
                            if (WorldGuardUtil.isPlayerInPvP(player) && !InvisibilityRing.getInvis().contains(player.getName())) {
                                damaged.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 8 * 20, 1));
                                damaged.getWorld().playEffect(damaged.getLocation(), Effect.POTION_BREAK, 4);
                            }
                        } else {
                            damaged.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 8 * 20, 1));
                            damaged.getWorld().playEffect(damaged.getLocation(), Effect.POTION_BREAK, 4);
                        }
                    }
                }
            }
        }
    }

}
