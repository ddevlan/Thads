package me.ohvalsgod.thads.mysterychest.command;

import com.ddylan.library.command.Command;
import me.ohvalsgod.thads.mysterychest.gui.MysteryChestHomeMenu;
import org.bukkit.entity.Player;

public class MysteryChestCommands {

    @Command(
            names = {"pvpchest", "mysterychest"},
            permission = "lolpvp.pvpchest"
    )
    public static void pvpchest(Player player) {
        new MysteryChestHomeMenu().openMenu(player);
    }

}
