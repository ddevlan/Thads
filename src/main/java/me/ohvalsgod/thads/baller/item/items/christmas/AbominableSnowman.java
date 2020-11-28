package me.ohvalsgod.thads.baller.item.items.christmas;

import com.ddylan.library.LibraryPlugin;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.baller.item.items.avengers.InvisibilityRing;
import me.ohvalsgod.thads.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class AbominableSnowman extends AbstractBallerItem {

    public Map<UUID, Long> cooldownMap;
    public HashSet<UUID> charging;
    public HashSet<UUID> frozen;

    public AbominableSnowman() {
        super("abominablesnowman");
        getAliases().add("abs");
        setWeight(6.5);
        listener = new AbominableSnowmanListener();
        this.charging = new HashSet<>();
        this.frozen = new HashSet<>();
        setCooldown(10);
    }

    @com.ddylan.library.listener.Listener
    public class AbominableSnowmanListener implements Listener {

        @EventHandler
        public void onAbs(PlayerToggleSneakEvent event) {
            Player player = event.getPlayer();

            if (Thads.get().getBallerManager().getItemByStack(player.getItemInHand()) instanceof AbominableSnowman && !charging.contains(player.getUniqueId())) {
                new BukkitRunnable() {
                    Integer chargeTime = 0;

                    @Override
                    public void run() {
                        if (player.isSneaking()) {
                            if (chargeTime <= 10) {
                                if (!charging.contains(player.getUniqueId()) || System.currentTimeMillis() - cooldownMap.getOrDefault(player.getUniqueId(), -1L) > 5000L) {
                                    cooldownMap.put(player.getUniqueId(), System.currentTimeMillis() + 8000);
                                    charging.add(player.getUniqueId());
                                    float f = chargeTime.floatValue() * 10.0F;
                                    Thads.get().getLibraryPlugin().getBossBarHandler().setBossBar(player, ChatColor.AQUA + "" + ChatColor.BOLD + "Blind Radius " + chargeTime, f);
                                    this.chargeTime = chargeTime + 1;
                                } else {
                                    charging.remove(player.getUniqueId());
                                    long remainingCooldown = System.currentTimeMillis() - cooldownMap.get(player.getUniqueId());
                                    int remainingCoioldownDisplay = (int) remainingCooldown / 1000;
                                    player.sendMessage(CC.AQUA + "Abominable Snowman " + CC.RED + " is on cooldown for " + remainingCoioldownDisplay);
                                    cancel();
                                }
                            } else {
                                activate(player, chargeTime);
                                cancel();
                            }
                        } else {
                            if (this.chargeTime > 0) {
                                activate(player, chargeTime);
                            }
                            cancel();
                        }
                    }
                }.runTaskTimer(Thads.get(), 0L, 20L);
            }
        }

        HashMap<String, BukkitTask> blinded = new HashMap<>();

        public void activate(final Player player, int radius) {
            boolean b = false;
            int bb = 0;
            charging.remove(player.getUniqueId());
            Thads.get().getLibraryPlugin().getBossBarHandler().removeBossBar(player);
            StringBuilder playersAffected = new StringBuilder();
            List<Entity> entities = player.getNearbyEntities(radius, radius, radius);
            ArrayList<Player> players = new ArrayList<>();
            for (Entity p : entities)
            {
                if ((p instanceof Player))
                {
                    bb++;
                    final Player oo = (Player)p;
                    players.add(oo);
                    if(Thads.get().isPlayerInPVP(player) && !InvisibilityRing.getInvis().contains(player.getName())) {
                        BukkitRunnable runnable = null;
                        if (!frozen.contains(oo.getUniqueId())) {
                            runnable = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for (Player p : players) {
                                        for (int i = 0; i < 15; i++) {
                                            p.getWorld().playEffect(p.getLocation().add(0.0, 1.0, 0.0), Effect.STEP_SOUND, 80);
                                            p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 80);
                                        }
                                    }
                                }
                            };
                        }

                        blinded.put(oo.getName(), runnable.runTask(Thads.get()));
                        b = true;
                        frozen.add(oo.getUniqueId());
                        oo.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, radius * 20, 0));
                        int ii = 3;
                        LibraryPlugin.getInstance().getBossBarHandler().setBossBar(oo, ChatColor.AQUA + "" + ChatColor.BOLD + "You have been blinded by " + player.getName(), ii);

                        if (oo != player) {
                            playersAffected.append(ChatColor.AQUA).append(oo.getName());
                            playersAffected.append(ChatColor.GRAY + ", ");
                        }

                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Thads.get(), () -> {
                            AbominableSnowman.this.frozen.remove(oo.getUniqueId());
                            blinded.get(oo.getName()).cancel();
                            blinded.remove(oo.getName());
                            LibraryPlugin.getInstance().getBossBarHandler().removeBossBar(oo);
                        }, ii * 20L);
                    }
                    }
                }
            if(playersAffected.length() > 0) {
                player.sendMessage(ChatColor.GRAY + "You have blinded: " + playersAffected.toString());
            }
            if ((bb == 0) && (!b)) {
                player.sendMessage(ChatColor.RED + "You did not affect anyone.");
            }
            cooldownMap.put(player.getUniqueId(), System.currentTimeMillis() + 8000);
        }
    }
}
