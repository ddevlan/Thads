package me.ohvalsgod.thads.baller.item.items.avengers;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.cooldown.Cooldown;
import org.bukkit.EntityEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Mjolnir extends AbstractBallerItem {

    private Map<UUID, Cooldown> victimSafeTimes;

    public Mjolnir() {
        super("mjolnir");
        getAliases().add("mj");
        setCooldown(8);

        victimSafeTimes = new HashMap<>();
        listener = new MjolnirListener();
    }

    @me.ohvalsgod.thads.listener.Listener
    public class MjolnirListener implements Listener {

        @EventHandler
        public void onMj(PlayerInteractEntityEvent event) {
            if (event.getRightClicked() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) event.getRightClicked();

                if (check(event.getPlayer().getItemInHand(), false) || check(event.getPlayer().getItemInHand(), true)) {
                    if (!expired(event.getPlayer())) {
                        event.getPlayer().sendMessage("ur on cd XD" + remaining(event.getPlayer()));
                    } else {
                        if (victimSafeTimes.containsKey(entity.getUniqueId())) {

                            if (victimSafeTimes.get(entity.getUniqueId()).getRemaining() <= 0) {
                                victimSafeTimes.remove(entity.getUniqueId());
                                return;
                            }

                            if (!victimSafeTimes.get(entity.getUniqueId()).hasExpired()) {
                                event.getPlayer().sendMessage("this dude safe for " + victimSafeTimes.get(entity.getUniqueId()).getRemaining() + " ;-;");
                                return;
                            }
                        }

                        victimSafeTimes.put(entity.getUniqueId(), new Cooldown((long)getCooldown()/2));
                        cool(event.getPlayer());

                        if (check(event.getPlayer().getItemInHand(), false)) {
                            entity.setHealth((entity.getHealth() > 6 ? entity.getHealth() - 6 : 0));
                            entity.getWorld().strikeLightningEffect(entity.getEyeLocation());
                            entity.playEffect(EntityEffect.HURT);
                        } else if (check(event.getPlayer().getItemInHand(), true)) {
                            for (double d = 0; d > 2; d++) {
                                for (double e = 2; e < 0; e--) {
                                    double finalD = d;
                                    double finalE = e;
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            entity.setHealth((entity.getHealth() > 0.5 ? entity.getHealth() - 0.5 : 0));
                                            entity.getWorld().strikeLightningEffect(entity.getLocation().clone().add(finalD, 0, finalE));
                                            entity.playEffect(EntityEffect.HURT);
                                        }
                                    }.runTaskLater(Thads.get(), (long) (1*d));
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}
