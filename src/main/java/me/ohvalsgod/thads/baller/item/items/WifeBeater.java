package me.ohvalsgod.thads.baller.item.items;

import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.item.BallerItem;
import me.ohvalsgod.thads.util.WorldGuardUtil;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;

public class WifeBeater implements BallerItem {

    private ItemStack ballerItemStack, legendaryItemStack;
    private int sellPrice, buyPrice, legendarySellPrice, legendaryBuyPrice;
    private boolean enabled, legendaryEnabled;
    private Listener listener;
    private List<String> aliases;

    public WifeBeater() {
        aliases = Collections.singletonList("wb");
        BallerManager.getBallerManager().loadBallerItem(this);

        listener = new WBListener();
    }

    @Override
    public String getName() {
        return "wifebeater";
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

    public class WBListener implements Listener {

        @EventHandler
        public void onDamage(PlayerInteractAtEntityEvent e) {
            Player player = e.getPlayer();
            if (e.getRightClicked() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) e.getRightClicked();
                if (WorldGuardUtil.isPlayerInPvP(player) && !InvisibilityRing.isInvis(player)) {
                    if (BallerManager.getBallerManager().getByItemStack(player.getItemInHand()) instanceof WifeBeater) {
                        entity.setVelocity(new Vector(0, 1.3, 0));
                        entity.getWorld().playSound(entity.getLocation(), Sound.ENDERDRAGON_WINGS, 1F, 1F);
                    }
                }
            }

        }

    }

}
