package me.ohvalsgod.thads.baller.item.items;

import lombok.Getter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.item.BallerItem;
import me.ohvalsgod.thads.data.PlayerData;
import me.ohvalsgod.thads.util.CC;
import me.ohvalsgod.thads.util.ParticleEffect;
import me.ohvalsgod.thads.util.WorldGuardUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.bossbar.BossBarAPI;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Iceblade implements BallerItem {

    @Getter private static Set<String> frozen;
    @Getter private static Set<String> runnable;


    private ItemStack ballerItemStack, legendaryItemStack;
    private int sellPrice, buyPrice, legendarySellPrice, legendaryBuyPrice;
    private boolean enabled, legendaryEnabled;
    private Listener listener;
    private List<String> aliases;

    public Iceblade() {
        aliases = Collections.singletonList("ib");
        BallerManager.getBallerManager().loadBallerItem(this);

        listener = new IBListener();
        frozen = new HashSet<>();
        runnable = new HashSet<>();
    }

    @Override
    public String getName() {
        return "iceblade";
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

    public class IBListener implements Listener {

        @EventHandler
        public void onVelocityChange(PlayerVelocityEvent e) {
            if (frozen.contains(e.getPlayer().getName())) {
                e.setCancelled(true);
            }
        }

        @EventHandler
        public void onMove(PlayerMoveEvent e) {
            Player player = e.getPlayer();
            if (BossBarAPI.hasBar(player) && BossBarAPI.getMessage(player).equalsIgnoreCase("Ender Dragon")) {
                BossBarAPI.removeBar(player);
            }
            if (frozen.contains(player.getName()) && e.getFrom().getY() < e.getTo().getY()) {
                e.setTo(new Location(player.getWorld(), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ(), e.getFrom().getYaw(), e.getFrom().getPitch()));
            }
        }

        @EventHandler
        public void onDamage(EntityDamageEvent e) {
            if (e.getEntity() instanceof Player) {
                Player player = (Player) e.getEntity();
                if (frozen.contains(player.getName())) {
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL) || e.getCause().equals(EntityDamageEvent.DamageCause.CONTACT)) {
                        e.setDamage(0);
                        e.setCancelled(true);
                    }
                }
            }
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent e) {
            e.getPlayer().setWalkSpeed(0.2f);
        }

        @EventHandler
        public void onSneak(PlayerToggleSneakEvent e) {
            final Player player = e.getPlayer();
            final PlayerData data = PlayerData.getByUuid(player.getUniqueId());
            if (BallerManager.getBallerManager().getByItemStack(player.getItemInHand()) instanceof Iceblade) {
                if (!runnable.contains(player.getName())) {
                    new BukkitRunnable() {
                        Integer lol = Integer.valueOf(0);

                        @Override
                        public void run() {
                            if (player.isSneaking()) {
                                if (this.lol <= 10) {
                                    if (data.getIcebladeCooldown().hasExpired() || runnable.contains(player.getName())) {
                                        runnable.add(player.getName());
                                        if (BossBarAPI.hasBar(player)) BossBarAPI.removeAllBars(player);
                                        BossBarAPI.addBar(player,
                                                new TextComponent("Freeze Radius: " + Float.toString(lol.floatValue())),
                                                BossBarAPI.Color.BLUE,
                                                BossBarAPI.Style.NOTCHED_10,
                                                lol.floatValue(),
                                                BossBarAPI.Property.DARKEN_SKY);
                                    } else {
                                        runnable.remove(player.getName());
                                        Long cd = data.getIcebladeCooldown().getRemaining();
                                        int cdf = cd.intValue() / 1000;
                                        player.sendMessage(CC.AQUA + getName() + CC.RED + " is on cooldown for " + Integer.toString(cdf));
                                        cancel();
                                    }
                                } else {
                                    activate(player, this.lol);
                                    cancel();
                                }
                            } else {
                                if (this.lol > 0) {
                                    activate(player, lol);
                                    cancel();
                                }
                            }
                        }
                    }.runTaskTimer(Thads.getInstance(), 0L, 20L);
                }
            }
        }

        public void activate(final Player player, int radius) {
            boolean b = false;
            runnable.remove(player.getName());
            BossBarAPI.removeAllBars(player);
            int bb = 0;
            StringBuilder playersAffected = new StringBuilder();
            for (Entity p : player.getNearbyEntities(radius, radius, radius)) {
                if ((p instanceof Player)) {
                    final Player oo = (Player)p;
                    bb++;
                    if(WorldGuardUtil.isPlayerInPvP(player) && !InvisibilityRing.isInvis(player)) {
                        if (!frozen.contains(oo.getName())) {
                            b = true;
                            frozen.add(oo.getName());
                            player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 10.0F, 10.0F);
                            ParticleEffect.CLOUD.display(player.getLocation(), 1.0F, 1.0F, 1.0F, 1.0F, 30);
                            int ii = 3;
                            oo.setWalkSpeed(1.0E-004F);
                            BossBarAPI.setMessage(oo, CC.AQUA + "" + CC.BOLD + "You have been frozen by " + player.getName(), ii);
                            oo.sendMessage(CC.GRAY + "You have been frozen by " + CC.AQUA + player.getName());
                            playersAffected.append(CC.AQUA + oo.getName() + CC.GRAY + ", ");
                            ParticleEffect.CLOUD.display(oo.getLocation(), 1.0F, 1.0F, 1.0F, 1.0F, 40);
                            ParticleEffect.CLOUD.display(oo.getLocation(), 1.0F, 1.0F, 1.0F, 1.0F, 40);
                            oo.getWorld().playEffect(oo.getLocation(), Effect.STEP_SOUND, 79);
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Thads.getInstance(), () -> {
                                frozen.remove(oo.getName());
                                BossBarAPI.removeBar(oo);
                                oo.setWalkSpeed(0.2F);
                            }, ii * 20L);
                        }
                    } else {
                        player.sendMessage(CC.RED + "You cannot use this weapon here.");
                        runnable.remove(player.getName());
                        break;
                    }
                }
            }
            if(playersAffected.length() > 0) {
                player.sendMessage(CC.GRAY + "You have frozen: " + playersAffected.toString());
            }
            if ((bb == 0) && (!b)) {
                player.sendMessage(CC.RED + "You did not affect anyone.");
            }
        }

    }

}
