package me.ohvalsgod.thads.baller.armor;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.object.AbstractBallerObject;
import me.ohvalsgod.thads.config.ConfigCursor;
import me.ohvalsgod.thads.util.ArmorBuilder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractBallerArmor extends AbstractBallerObject implements IBallerArmor {

    public AbstractBallerArmor(String name) {
        super(name);
    }

    @Override
    public void give(Player player) {
        for (ItemStack item : getBallerArmor()) {
            if (player.getInventory().firstEmpty() == -1) {
                player.getWorld().dropItem(player.getEyeLocation(), item);
            } else {
                player.getInventory().addItem(item);
            }
        }
    }

    @Override
    public ItemStack[] getBallerArmor() {
        ConfigCursor cursor = new ConfigCursor(Thads.get().getBallerObjectConfig(), getName() + ".baller-armor");
        ArmorBuilder builder = new ArmorBuilder(cursor.getString("type"));

        builder.name(cursor.getString("name"));
        builder.lore(cursor.getStringList("lore"));

        //TODO: only enchant them properly based off of enchantment.canEnchant(ItemStack)
        if (!cursor.getKeys("enchants").isEmpty()) {
            for (String string : cursor.getKeys("enchants")) {
                Enchantment enchantment = Enchantment.getByName(string);
                builder.enchant(enchantment, cursor.getInt("enchants." + string));
            }
        }

        if (cursor.parentPath().getBoolean("hide-flags")) {
            builder.hideFlags();
        }

        return builder.build();
    }
}
