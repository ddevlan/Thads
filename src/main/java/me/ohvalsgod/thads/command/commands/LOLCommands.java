package me.ohvalsgod.thads.command.commands;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.gui.object.LOLObjectsMenu;
import me.ohvalsgod.thads.command.Command;
import me.ohvalsgod.thads.command.param.Parameter;
import me.ohvalsgod.thads.data.PlayerData;
import me.ohvalsgod.thads.util.CC;
import me.ohvalsgod.thads.util.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LOLCommands {

    @Command(
            names = {"lol", "lol.help"},
            permissionNode = "lolpvp.help"
    )
    public static void help(CommandSender sender) {
        for (String string : Thads.get().getLang().getStringList("lol.help")) {
            sender.sendMessage(CC.translate(string));
        }
    }

    @Command(
            names = {"lolweapons", "lol weapons"},
            permissionNode = "lolpvp.weapons"
    )
    public static void weapons(Player player) {
        long start = System.currentTimeMillis();
        new LOLObjectsMenu().openMenu(player);
        if (System.currentTimeMillis() - start > 10) {
            player.sendMessage(CC.GRAY + "Loaded baller items in " + (System.currentTimeMillis() - start) + "ms.");
        }
    }

    @Command(
            names = {"lol.tag", "lolt"},
            permissionNode = "lolpvp.donator.tag"
    )
    public static void tag(Player player, @Parameter(name = "tag") String tag) {
        PlayerData data = Thads.get().getPlayerDataHandler().getPlayerData(player.getUniqueId());

        if (System.currentTimeMillis() > data.getLastLolTag() + 60 * 1000) {
            if (StringUtils.isAlphanumeric(tag)) {
                if (tag.length() <= 5) {
                    data.setLastLolTag(System.currentTimeMillis());
                    data.setLolTag(tag);
                    player.sendMessage(CC.GREEN + "Your tag has been set to: " + CC.translate(Thads.get().getLang().getString("lol.tag")).replace("%tag%", data.getLolTag()));
                } else {
                    player.sendMessage(CC.RED + "Your tag must be less than 5 characters long.");
                }
            } else {
                player.sendMessage(CC.RED + "Your tag must be alphanumeric.");
            }
        } else {
            player.sendMessage(CC.RED + "You still have " + TimeUtil.formatTime((data.getLastLolTag() + 60 * 1000 - System.currentTimeMillis())) + "s left until you can use /lolt again.");
        }
    }

}
