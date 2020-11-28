package me.ohvalsgod.thads.baller.item.items.defaults;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.baller.item.items.avengers.InvisibilityRing;
import me.ohvalsgod.thads.util.WorldGuardUtil;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;

public class WifeBeater extends AbstractBallerItem {

    public WifeBeater() {
        super("wifebeater");
        getAliases().add("wb");
        setWeight(4.5);
        listener = new WBListener();
    }

    @me.ohvalsgod.thads.listener.Listener
    public class WBListener implements Listener {
        @EventHandler
        public void onDamage(PlayerInteractEntityEvent e) {
            if (isEnabled()) {
                Player player = e.getPlayer();
                if (e.getRightClicked() instanceof LivingEntity) {
                    LivingEntity entity = (LivingEntity) e.getRightClicked();
                    if (WorldGuardUtil.isPlayerInPvP(player) && !InvisibilityRing.getInvis().contains(player.getName())) {
                        if (Thads.get().getBallerManager().getItemByStack(player.getItemInHand()) instanceof WifeBeater) {
                            entity.setVelocity(new Vector(0, 1.3, 0));
                            entity.getWorld().playSound(entity.getLocation(), Sound.ENDERDRAGON_WINGS, 1F, 1F);
                            entity.getWorld().playSound(entity.getLocation(), Sound.ENDERDRAGON_WINGS, 1f, 1f);
                        }
                    }
                }
            }
        }
    }

}
