package me.ohvalsgod.thads.baller.item.items.defaults;

import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MorningWood extends AbstractBallerItem {

    public MorningWood() {
        super("morningwood");
        getAliases().add("mw");
        setWeight(2);
        listener = new MWListener();
    }

    public class MWListener implements Listener {
        @EventHandler
        public void onDamage(EntityDamageByEntityEvent event) {
            if (event.getEntity() instanceof Player) {
                if (event.getDamager() instanceof Player) {
                    Player player = (Player) event.getDamager();
                    if (BallerManager.getBallerManager().getByItemStack(player.getItemInHand()) instanceof MorningBJ) {
                        event.setDamage(event.getDamage() * 1.1);
                    }
                }
            }
        }
    }

}
