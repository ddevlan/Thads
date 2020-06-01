package me.ohvalsgod.thads.baller.item.items.defaults;

import lombok.Getter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.bossbar.BossBarAPI;

import java.util.HashMap;

public class MagicSperm extends AbstractBallerItem {

    @Getter private static HashMap<String, Integer> invis;

    public MagicSperm() {
        super("magicsperm");
        setWeight(8);
        getAliases().add("ms");
        invis = new HashMap<>();
        listener = new MSListener();
    }



    public class MSListener implements Listener {
        @EventHandler
        public void onHit(EntityDamageByEntityEvent event) {
            if (isEnabled()) {
                if (event.getEntity() instanceof Player) {
                    Player target = (Player) event.getEntity();
                    if (event.getDamager() instanceof Player) {
                        Player player = (Player) event.getDamager();
                        if (BallerManager.getBallerManager().getItemByStack(player.getItemInHand()) instanceof MagicSperm) {
                            int i = 2;
                            if (!invis.containsKey(player.getName())) {
                                target.hidePlayer(player);
                                BossBarAPI.setMessage(player, CC.AQUA + CC.BOLD + "You are invisible.", i);
                                invis.put(player.getName(), i + 1);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (invis.containsKey(player.getName())) {
                                            invis.put(player.getName(), invis.get(player.getName()) - 1);
                                            if (invis.get(player.getName()) <= 0) {
                                                target.showPlayer(player);
                                                cancel();
                                            }
                                        }
                                    }
                                }.runTaskTimer(Thads.get(), 0L, 20L);
                            }
                        }
                    }
                }
            }
        }
    }

}
