package me.ohvalsgod.thads.baller.item.items;

import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.item.BallerItem;
import me.ohvalsgod.thads.util.WorldGuardUtil;
import org.bukkit.Effect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

public class Ejacul8 implements BallerItem {

    private ItemStack ballerItemStack, legendaryItemStack;
    private int sellPrice, buyPrice, legendarySellPrice, legendaryBuyPrice;
    private boolean enabled, legendaryEnabled;
    private Listener listener;
    private List<String> aliases;

    public Ejacul8() {
        BallerManager.getBallerManager().loadBallerItem(this);
        aliases = Collections.singletonList("ejac");

        listener = new EJACListener();
    }

    @Override
    public String getName() {
        return "ejacul8";
    }

    @Override
    public ItemStack getBallerItemStack() {
        return ballerItemStack;
    }

    @Override
    public void setBallerItemStack(ItemStack itemStack) {
        this.ballerItemStack = itemStack;
    }

    @Override
    public ItemStack getLegendaryItemStack() {
        return legendaryItemStack;
    }

    @Override
    public boolean isLegendaryItemEnabled() {
        return legendaryEnabled;
    }

    @Override
    public void setLegendaryItemStack(ItemStack itemStack) {
        this.legendaryItemStack = itemStack;
    }

    @Override
    public void setLegendaryEnabled(boolean b) {
        this.legendaryEnabled = b;
    }

    @Override
    public int getSellPrice() {
        return sellPrice;
    }

    @Override
    public void setSellPrice(int i) {
        this.sellPrice = i;
    }

    @Override
    public int getBuyPrice() {
        return buyPrice;
    }

    @Override
    public void setBuyPrice(int i) {
        this.buyPrice = i;
    }

    @Override
    public int getLegendarySellPrice() {
        return legendarySellPrice;
    }

    @Override
    public void setLegendarySellPrice(int i) {
        this.legendarySellPrice = i;
    }

    @Override
    public int getLegendaryBuyPrice() {
        return legendaryBuyPrice;
    }

    @Override
    public void setLegendaryBuyPrice(int i) {
        this.legendaryBuyPrice = i;
    }

    @Override
    public Listener getListener() {
        return listener;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean b) {
        this.enabled = b;
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    public class EJACListener implements Listener {

        @EventHandler
        public void onDamage(EntityDamageByEntityEvent e) {
            if (e.getEntity() instanceof LivingEntity) {
                if (e.getDamager() instanceof Player) {
                    Player damager = (Player) e.getDamager();
                    LivingEntity damaged = (LivingEntity) e.getEntity();

                    if (BallerManager.getBallerManager().getByItemStack(damager.getItemInHand()) instanceof Ejacul8) {
                        if (damaged instanceof Player) {
                            if (WorldGuardUtil.isPlayerInPvP(damager) && !InvisibilityRing.isInvis(damager)) {
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
