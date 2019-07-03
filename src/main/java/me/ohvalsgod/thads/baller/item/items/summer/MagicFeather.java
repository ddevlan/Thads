package me.ohvalsgod.thads.baller.item.items.summer;

import lombok.Getter;
import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.util.WoolUtil;
import me.ohvalsgod.thads.util.WorldGuardUtil;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MagicFeather extends AbstractBallerItem {

    @Getter private static ArrayList<UUID> flying;
    private Map<UUID, Integer> color;

    public MagicFeather() {
        super("magicfeather");
        getAliases().add("mf");
        setWeight(6);
        flying = new ArrayList<>();
        color = new HashMap<>();
        listener = new MFListener();
    }

    public class MFListener implements Listener {
        @EventHandler
        public void onMove(PlayerMoveEvent event) {
            if (isEnabled()) {
                Player player = event.getPlayer();
                if (BallerManager.getBallerManager().getByItemStack(player.getItemInHand()) instanceof MagicFeather) {
                    if (!WorldGuardUtil.isPlayerInPvP(player)) {
                        if (!flying.contains(player.getUniqueId())) {
                            flying.add(player.getUniqueId());
                        }
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        if (BallerManager.getBallerManager().getFromLegendary(player.getItemInHand()) instanceof MagicFeather) {
                            if (!color.containsKey(player.getUniqueId())) {
                                color.put(player.getUniqueId(), 0);
                            }

                            player.getWorld().playSound(player.getLocation(), Sound.STEP_WOOL, 0.85f, 0.85f);

                            for (float i = 0; i < .45; i = i + 0.1f) {
                                for (float i2 = 0; i2 < .45; i2 = i2 + 0.1f) {
                                    for (float i3 = 0; i3 < .45; i3 = i3 + 0.1f) {
                                        player.getWorld().spigot().playEffect(player.getLocation(), Effect.TILE_BREAK, Material.WOOL.getId(), WoolUtil.convertChatColorToWoolDate(WoolUtil.rainbowColors.get(color.get(player.getUniqueId()))), i, i2, i3, 1, 1, 30);
                                    }
                                }
                            }

                            if (color.get(player.getUniqueId()) + 1 < WoolUtil.rainbowColors.size()) {
                                color.put(player.getUniqueId(), color.get(player.getUniqueId()) + 1);
                            } else {
                                color.put(player.getUniqueId(), 0);
                            }
                        } else {
                            player.getWorld().playSound(player.getLocation(), Sound.STEP_WOOL, 0.85f, 0.85f);
                            for (float i = 0; i < .45; i = i + 0.1f) {
                                for (float i2 = 0; i2 < .45; i2 = i2 + 0.1f) {
                                    for (float i3 = 0; i3 < .45; i3 = i3 + 0.1f) {
                                        player.getWorld().spigot().playEffect(player.getLocation(), Effect.TILE_BREAK, Material.WOOL.getId(), 0, i, i2, i3, 1, 1, 30);
                                    }
                                }
                            }
                        }
                    }
                }

                if (flying.contains(player.getUniqueId())) {
                    if (WorldGuardUtil.isPlayerInPvP(player) || !(BallerManager.getBallerManager().getByItemStack(player.getItemInHand()) instanceof MagicFeather)) {
                        player.setFlying(false);
                        player.setAllowFlight(false);
                        flying.remove(player.getUniqueId());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {
        if (flying.contains(event.getPlayer().getUniqueId()) || (BallerManager.getBallerManager().getByItemStack(event.getPlayer().getItemInHand()) instanceof MagicFeather)) {
            event.setCancelled(true);
        }
    }

}
