package me.ohvalsgod.thads.command.commands;

import me.ohvalsgod.thads.command.Command;
import org.bukkit.command.CommandSender;

public class LOLCommands {

    @Command(
            names = {"lol", "lol.help"},
            permissionNode = "lolpvp.help"
    )
    public static void help(CommandSender sender) {

    }

}
