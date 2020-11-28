package me.ohvalsgod.thads.mysterychest.loot;

import lombok.Getter;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.util.InventorySerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class MysteryChestLoot {

    private UUID author;
    private long creationDate;
    private List<ItemStack> contents;

    public MysteryChestLoot(UUID author, long creationDate, List<ItemStack> contents) {
        this.author = author;
        this.contents = contents;
        this.creationDate = creationDate;
    }

    public String toString() {
        String s = "";
        s = s + "@author;" + author.toString();
        s = s + ":@creationDate;" + creationDate;
        s = s + ":@contents;" + InventorySerializer.itemStackListToBase64(contents);
        return s;
    }

    public static MysteryChestLoot fromString(String source) {
       String[] att = source.split(":");

       UUID creator = UUID.randomUUID();
       long creationDate = System.currentTimeMillis();
       List<ItemStack> contents = new ArrayList<>();

        for (String attribute : att) {
            String[] split = attribute.split(";");

            if (split[0].equalsIgnoreCase("@author")) {
                creator = UUID.fromString(split[1]);
            }

            if (split[0].equalsIgnoreCase("@contents")) {
                contents = InventorySerializer.itemStackListFromBase64(split[1]);
            }

            if (split[0].equalsIgnoreCase("@creationDate")) {
                creationDate = Long.parseLong(split[1]);
            }
        }
        return new MysteryChestLoot(creator, creationDate, contents);
    }

    public void give(Player player) {
        int i = 0;
        for (ItemStack itemStack : contents) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.getInventory().firstEmpty() == -1) {
                        player.getWorld().dropItem(player.getEyeLocation(), itemStack);
                    } else {
                        player.getInventory().addItem(itemStack);
                    }
                }
            }.runTaskLater(Thads.get(), i * 2); // Delayed function to do cool lottery thingy
            i++;
        }
    }

}
