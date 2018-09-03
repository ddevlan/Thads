package me.ohvalsgod.thads.command.commands;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.gui.weapons.LOLWeaponsMenu;
import me.ohvalsgod.thads.command.Command;
import me.ohvalsgod.thads.config.ConfigCursor;
import me.ohvalsgod.thads.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LOLCommands {

    private static ConfigCursor cursor;

    @Command(
            names = {"lol", "lol.help"},
            permissionNode = "lolpvp.help"
    )
    public static void help(CommandSender sender) {
        for (String string : Thads.getInstance().getLangConfig().getConfig().getStringList("lol.help")) {
            sender.sendMessage(CC.translate(string));
        }
    }

    @Command(
            names = {"lolweapons", "lol weapons"},
            permissionNode = "lolpvp.weapons"
    )
    public static void weapons(Player player) {
        long start = System.currentTimeMillis();
        player.sendMessage(CC.GREEN + "Loading baller items...");
        new LOLWeaponsMenu().openMenu(player);
        player.sendMessage(CC.GREEN + "Loaded baller items in " + Long.toString(System.currentTimeMillis() - start) + "ms.");
    }

}
