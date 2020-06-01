package me.ohvalsgod.thads.baller.gui.buttons;

import lombok.AllArgsConstructor;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
public class LOLItemsInfoButton extends Button {

    private int loaded;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(Material.PAPER);

        builder.name(Thads.get().getLang().getString("lol.menu.global.info.name"));
        List<String> lore = Thads.get().getLang().getStringList("lol.menu.global.info.lore");
        lore.replaceAll(s -> s.replace("%amount%", String.valueOf(loaded)));
        builder.lore(lore);

        return builder.build();
    }

}
