package me.ohvalsgod.thads.command.commands;

import me.ohvalsgod.thads.command.Command;
import me.ohvalsgod.thads.mysterychest.gui.MysteryChestHomeMenu;
import org.bukkit.entity.Player;

public class MysteryChestCommands {

    @Command(
            names = {"pvpchest", "mysterychest"},
            permissionNode = "lolpvp.pvpchest"
    )
    public static void pvpchest(Player player) {
        new MysteryChestHomeMenu().openMenu(player);
    }

}
