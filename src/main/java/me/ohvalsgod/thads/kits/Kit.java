package me.ohvalsgod.thads.kits;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.ohvalsgod.thads.Thads;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class Kit {

    //          /kit create Test

    private String name;
    private ItemStack icon;
    private ItemStack[] items;
    private long cooldown;
    private double weight;
    private Map<UUID, Long> cooldowns;

    @Getter
    private static Set<Kit> kits;

    public boolean onCooldown(UUID uuid) {
        if (cooldowns.containsKey(uuid)) {
            return System.currentTimeMillis() - cooldowns.get(uuid) > cooldown;
        }
        return false;
    }

    public long getCooldownRemaining(UUID uuid) {
        if (onCooldown(uuid)) {
            return (System.currentTimeMillis() - cooldowns.get(uuid)) - cooldown;
        }
        return -1337L;
    }

    private void cooldown(UUID uuid) {
        if (!onCooldown(uuid)) {
            cooldowns.put(uuid, System.currentTimeMillis() + cooldown);
        }
    }

    private void warmup(UUID uuid) {
        if (onCooldown(uuid)) {
            cooldowns.remove(uuid);
        }
    }

    public void give(Player player) {
        if (hasPermission(player)) {
            if (!onCooldown(player.getUniqueId())) {
                for (ItemStack item : items) {
                    if (player.getInventory().firstEmpty() > -1) {
                        player.getInventory().addItem(item);
                    } else {
                        player.getWorld().dropItem(player.getEyeLocation(), item);
                    }
                }
                player.sendMessage(Thads.get().getLang().getString("kits.applied"));
                cooldown(player.getUniqueId());
            } else {
                player.sendMessage(Thads.get().getLang().getString("kits.cooldown")
                        .replace("%kit%", name)
                        .replace("%cooldown%", String.valueOf(getCooldownRemaining(player.getUniqueId()))));
            }
        } else {
            player.sendMessage(ChatColor.RED + "No permission.");
        }
    }

    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("lolpvp.kits." + name);
    }

    public static String toDisplayList() {
        StringBuilder sb = new StringBuilder();

        int i = 0;
        for (Kit kit : getSorted()) {
            sb.append(Thads.get().getLang().getString("kits.list.display-color")).append(kit.getName());
            if (i < getKits().size()) {
                sb.append(Thads.get().getLang().getString("kits.list.seperator"));
            }
        }

        return Thads.get().getLang().getString("kits.list.format").replace("%kits%", sb.toString());
    }

    public static List<Kit> getSorted() {
        return Kit.getKits().stream().sorted(Comparator.comparingDouble(Kit::getWeight)).collect(Collectors.toList());
    }
}
