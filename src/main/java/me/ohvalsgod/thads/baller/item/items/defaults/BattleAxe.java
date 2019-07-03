package me.ohvalsgod.thads.baller.item.items.defaults;

import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class BattleAxe extends AbstractBallerItem {

    public BattleAxe() {
        super("battleaxe");
        getAliases().add("baxe");
        setWeight(4);
        listener = new BAXEListener();
    }

    public class BAXEListener implements Listener {

        @EventHandler
        public void onDamage(EntityDamageByEntityEvent e) {
            if (isEnabled()) {
                if (e.getEntity() instanceof Player) {
                    if (e.getDamager() instanceof Player) {
                        Player damager = (Player) e.getDamager();

                        if (BallerManager.getBallerManager().getByItemStack(damager.getItemInHand()) instanceof BattleAxe) {
                            e.setDamage(EntityDamageEvent.DamageModifier.ARMOR, e.getDamage() * 1.2);
                        }
                    }
                }
            }
        }
    }

}
