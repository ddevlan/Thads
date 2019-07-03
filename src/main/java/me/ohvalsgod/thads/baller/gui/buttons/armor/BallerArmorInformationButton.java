package me.ohvalsgod.thads.baller.gui.buttons.armor;

import lombok.AllArgsConstructor;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.armor.AbstractBallerArmor;
import me.ohvalsgod.thads.menu.Button;
import me.ohvalsgod.thads.util.CC;
import me.ohvalsgod.thads.util.ItemBuilder;
import me.ohvalsgod.thads.util.NumberUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class BallerArmorInformationButton extends Button {

    private AbstractBallerArmor armor;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(Material.PAPER);

        builder.name(Thads.getInstance().getLang().getString("lol.menu.armor.edit.information.name").replace("%armor%", armor.getArmor()[0].getItemMeta().getDisplayName()));
        for (String string : Thads.getInstance().getLang().getStringList("lol.menu.armor.edit.information.lore")) {
            String lore = string
                    .replace("%separator%", CC.SCOREBOARD_SEPERATOR_EXTRA)
                    .replace("%enabled%", WordUtils.capitalizeFully(Boolean.toString(armor.isEnabled())))
                    .replace("%buy_price%", NumberUtil.formatNumber(armor.getBuyPrice()))
                    .replace("%sell_price%", NumberUtil.formatNumber(armor.getSellPrice()))
                    .replace("%weight%", armor.getWeight().toString());

            builder.lore(CC.translate(lore));
        }

        return builder.build();
    }



}
