package me.ohvalsgod.thads.mysterychest;

import lombok.Getter;
import lombok.Setter;
import me.ohvalsgod.thads.mysterychest.loot.MysteryChestLoot;
import me.ohvalsgod.thads.util.CC;
import me.ohvalsgod.thads.util.ItemBuilder;
import me.ohvalsgod.thads.util.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class MysteryChest {

    @Setter private String name;
    @Setter private ChatColor color;
    @Setter private long creationDate;
    @Setter private UUID author;
    private List<MysteryChestLoot> loot, removed;   //TODO: add logs

    public MysteryChest(String name) {
        this.name = name;
        color = ChatColor.RED;
        loot = new ArrayList<>();
        removed = new ArrayList<>();
        creationDate = System.currentTimeMillis();
    }

    public MysteryChestLoot getRandomLoot() {
        return loot.get(MathUtil.RANDOM.nextInt(loot.size()));
    }

    public String getDisplayName() {
        return color + name;
    }

    public void open(Player player) {
        player.getItemInHand().setAmount(player.getItemInHand().getAmount() == 1 ? 1:player.getItemInHand().getAmount() - 1);
        player.setItemInHand(player.getItemOnCursor().getAmount() == 1 ? null:player.getItemInHand());
        player.updateInventory();
        for (ItemStack item : loot.get(MathUtil.RANDOM.nextInt(loot.size())).getContents()) {
            if (player.getInventory().firstEmpty() == -1) {
                player.getWorld().dropItem(player.getEyeLocation(), item);
            } else {
                player.getInventory().addItem(item);
            }
        }
        Bukkit.broadcastMessage(player.getName() + "opened pvp chest");
    }

    public void removeLoot(MysteryChestLoot loot) {
        this.loot.remove(loot);
        this.removed.add(loot);
    }

    public ItemStack toItemStack(int amount) {
        ItemBuilder builder = new ItemBuilder(Material.CHEST);
        builder.amount(amount);
        builder.name(color + name);
        builder.lore(CC.GRAY + "Right-click to unwrap!");

        return builder.build();
    }

}
