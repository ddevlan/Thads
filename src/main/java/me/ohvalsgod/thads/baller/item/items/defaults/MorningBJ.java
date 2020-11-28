package me.ohvalsgod.thads.baller.item.items.defaults;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MorningBJ extends AbstractBallerItem {

    public MorningBJ() {
        super("morningbj");
        getAliases().add("mbj");
        getAliases().add("morningblowjob");
        setWeight(2.5);
        listener = new BJListener();
    }

    @me.ohvalsgod.thads.listener.Listener
    public class BJListener implements Listener {
        @EventHandler
        public void onDamage(EntityDamageByEntityEvent event) {
            if (isEnabled()) {
                if (event.getEntity() instanceof Player) {
                    if (event.getDamager() instanceof Player) {
                        Player player = (Player) event.getDamager();
                        if (Thads.get().getBallerManager().getItemByStack(player.getItemInHand()) instanceof MorningBJ) {
                            event.setDamage(event.getDamage() * 1.15);
                        }
                    }
                }
            }
        }
    }

}
