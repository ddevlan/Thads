package me.ohvalsgod.thads.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArmorBuilder {

    private List<String> valid = Arrays.asList("DIAMOND", "IRON", "GOLD", "LEATHER", "CHAINMAIL");

    private ItemStack[] is;

    public ArmorBuilder(String type) {
        if (valid.contains(type)) {
            is = new ItemStack[] {
                    new ItemStack(Material.valueOf(type + "_BOOTS")),
                    new ItemStack(Material.valueOf(type + "_LEGGINGS")),
                    new ItemStack(Material.valueOf(type + "_CHESTPLATE")),
                    new ItemStack(Material.valueOf(type + "_HELMET"))
            };
        } else {
            is = new ItemStack[] {
                    new ItemStack(Material.DIAMOND_BOOTS),
                    new ItemStack(Material.DIAMOND_LEGGINGS),
                    new ItemStack(Material.DIAMOND_CHESTPLATE),
                    new ItemStack(Material.DIAMOND_HELMET)
            };
        }
    }

    public ArmorBuilder enchant(Enchantment enchantment, int value) {
        for (ItemStack itemStack : is) {
            if (enchantment.canEnchantItem(itemStack)) {
                itemStack.addUnsafeEnchantment(enchantment, value);
            }
        }
        return this;
    }

    public ArmorBuilder hideFlags() {
//        for (ItemStack item : is) {
//            final ItemMeta meta = item.getItemMeta();
//            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//            item.setItemMeta(meta);
//        }
        return this;
    }

    public ArmorBuilder lore(List<String> lore) {
        for (ItemStack itemStack : is) {
            ItemMeta meta = itemStack.getItemMeta();
            List<String> toAdd = new ArrayList<String>();
            for (String string : lore) {
                toAdd.add(string.replace("&", "§"));
            }
            meta.setLore(toAdd);
            itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ArmorBuilder name(String name) {
        for (ItemStack itemStack : is) {
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(name.replace("&", "§"));
            itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ArmorBuilder nameAndLore(String name, List<String> lore) {
        name(name);
        lore(lore);
        return this;
    }

    public ArmorBuilder setType(String type) {
        return this;
    }

    public ItemStack[] build() {
        return is;
    }
    
}
