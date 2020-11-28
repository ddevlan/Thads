package me.ohvalsgod.thads.mysterychest.gui;

import com.ddylan.library.menu.Button;
import lombok.AllArgsConstructor;
import me.ohvalsgod.thads.mysterychest.MysteryChest;
import me.ohvalsgod.thads.mysterychest.loot.MysteryChestEditMenu;
import me.ohvalsgod.thads.util.CC;
import me.ohvalsgod.thads.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@AllArgsConstructor
public class MysteryChestDisplayButton extends Button {

    private final MysteryChest chest;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.CHEST).name(chest.getDisplayName()).lore(Arrays.asList(CC.YELLOW + "Left-click to receive.", CC.YELLOW + "Right click to edit.")).build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType.isLeftClick()) {
            player.getInventory().addItem(chest.toItemStack(1));
        } else {
            new MysteryChestEditMenu(chest).openMenu(player);
        }
    }
}
