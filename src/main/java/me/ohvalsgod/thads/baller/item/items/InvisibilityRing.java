package me.ohvalsgod.thads.baller.item.items;

import lombok.Getter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.item.BallerItem;
import me.ohvalsgod.thads.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.bossbar.BossBarAPI;

import java.util.*;

public class InvisibilityRing implements BallerItem {

    private HashMap<String, Integer> warmup;
    @Getter private static Set<String> invis;

    private ItemStack ballerItemStack, legendaryItemStack;
    private int sellPrice, buyPrice, legendarySellPrice, legendaryBuyPrice;
    private Listener listener;
    private boolean enabled, legendaryEnabled;
    private List<String> aliases;

    public InvisibilityRing() {
        aliases = Arrays.asList("ir", "invisring");

        warmup = new HashMap<>();
        invis = new HashSet<>();

        listener = new IRListener();
    }

    public static boolean isInvis(Player player) {
        return invis.contains(player.getName());
    }

    @Override
    public String getName() {
        return "invisibilityring";
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

    @Override
    public int getWeight() {
        return 11;
    }

    @Override
    public Listener getListener() {
        return listener;
    }

    public class IRListener implements Listener {
        @EventHandler
        public void onLeave(PlayerQuitEvent e) {
            invis.remove(e.getPlayer().getName());
        }

        @EventHandler
        public void onJoin(PlayerJoinEvent e) {
            Player player = e.getPlayer();
            for (String str : invis) {
                Player p = Bukkit.getPlayer(str);
                if (p != null) {
                    player.hidePlayer(p);
                } else {
                    invis.remove(str);
                }
            }
        }

        @EventHandler
        public void onMove(PlayerMoveEvent e) {
            if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
                Player player = e.getPlayer();
                boolean hasInvisRing = false;
                if (invis.contains(player.getName())) {
                    for (ItemStack i : player.getInventory().getContents()) {
                        if (BallerManager.getBallerManager().getByItemStack(i) instanceof InvisibilityRing) {
                            hasInvisRing = true;
                            break;
                        }
                    }

                    if (!hasInvisRing) {
                        for (Player other : Bukkit.getServer().getOnlinePlayers()) {
                            other.showPlayer(player);
                            invis.remove(player.getName());
                            BossBarAPI.removeAllBars(player);
                            player.sendMessage(CC.RED + "You are no longer invisible.");

                        }
                    }
                }
            }
        }

        @EventHandler
        public void onPick(PlayerPickupItemEvent e) {
            if (invis.contains(e.getPlayer().getName())) {
                e.setCancelled(true);
            }
        }

        @EventHandler
        public void onHit(EntityDamageByEntityEvent e) {
            if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
                Player damager = (Player) e.getDamager();
                Player damaged = (Player) e.getEntity();

                if (invis.contains(damager.getName())) {
                    e.setCancelled(true);
                }

                if (warmup.containsKey(damaged.getName())) {
                    Bukkit.getServer().getScheduler().cancelTask(warmup.get(damager.getName()));
                    warmup.remove(damaged.getName());
                    damaged.sendMessage(CC.RED + "You cannot vanish now. You were hit.");
                }
            }
        }

        @EventHandler
        public void onInteract(PlayerInteractEvent e) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Player player = e.getPlayer();
                if (BallerManager.getBallerManager().getByItemStack(player.getItemInHand()) instanceof InvisibilityRing) {
                    e.setCancelled(true);
                    if (invis.contains(player.getName())) {
                        for (Player other : Bukkit.getServer().getOnlinePlayers()) {
                            other.showPlayer(player);
                        }
                        invis.remove(player.getName());
                        BossBarAPI.removeAllBars(player);
                        player.sendMessage(CC.RED + "You are no longer invisible.");
                    } else if (!warmup.containsKey(player.getName())) {
                        int warm = 5;
                        player.sendMessage(CC.GRAY + "Vanishing in: " + CC.AQUA + warm + CC.GRAY + " seconds.");
                        warmup.put(player.getName(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Thads.getInstance(), new Runnable() {
                            @Override
                            public void run() {
//                                BossBarAPI.addBar(player,
//                                        new TextComponent("You are now invisible!"),
//                                        BossBarAPI.Color.RED,
//                                        BossBarAPI.Style.PROGRESS,
//                                        1f,
//                                        BossBarAPI.Property.DARKEN_SKY);
                                BossBarAPI.setMessage(player, ChatColor.RED + "" + ChatColor.BOLD + "You are now invisible!");
                                player.sendMessage(CC.GREEN + "You are now invisible.");
                                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                    p.hidePlayer(player);
                                }
                                invis.add(player.getName());
                                warmup.remove(player.getName());

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (invis.contains(player.getName())) {
                                            BossBarAPI.setMessage(player, ChatColor.RED + "" + ChatColor.BOLD + "You are now invisible!");
                                        } else {
                                            cancel();
                                        }
                                    }
                                }.runTaskTimer(Thads.getInstance(), 0L, 400L);
                            }
                        }, warm * 20L));
                    }
                }
            }
        }
    }

}
