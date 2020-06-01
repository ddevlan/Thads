package me.ohvalsgod.thads.command.commands;

import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.command.Command;
import me.ohvalsgod.thads.command.param.Parameter;
import me.ohvalsgod.thads.kits.Kit;
import me.ohvalsgod.thads.kits.gui.KitSelectorMenu;
import me.ohvalsgod.thads.util.TimeUtil;
import org.bukkit.entity.Player;

import java.util.Calendar;

public class KitCommands {

    /*
        Player commands
     */

    @Command(names = "kit", permissionNode = "lolpvp.kits")
    public static void help(Player player) {
        for (String string : Thads.get().getLang().getStringList("kits.help")) {
            player.sendMessage(string);
        }
    }

    @Command(names = "kit", permissionNode = "lolpvp.kits")
    public static void kit(Player player, @Parameter(name = "kit") Kit kit) {
        if (kit.onCooldown(player.getUniqueId())) {
            Calendar cal = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            cal2.setTimeInMillis(System.currentTimeMillis() + kit.getCooldown());

            player.sendMessage(Thads.get().getLang().getString("kits.cooldown")
                                .replace("%kit%", kit.getName())
                                .replace("%time%", TimeUtil.formatDateDiff(cal, cal2)));
        } else {
            kit.give(player);
        }
    }

    @Command(names = "kit.list", permissionNode = "lolpvp.kits.list")
    public static void kitList(Player player) {
        long owned = Kit.getKits().stream().filter(kit -> kit.hasPermission(player)).count();
        player.sendMessage(Thads.get().getLang().getString("kits.list.title")
                                .replace("%owned%", String.valueOf(owned))
                                .replace("%total%", String.valueOf(Kit.getKits().size())));
        player.sendMessage(Kit.toDisplayList());
    }

    @Command(names = "kit.gui", permissionNode = "lolpvp.kits.gui")
    public static void kitGui(Player player) {
        new KitSelectorMenu().openMenu(player);
    }

    /*
        Admin commands
     */

    @Command(names = "kit.create", permissionNode = "lolpvp.kits.create")
    public static void kitCreate(Player player) {
        //TODO: gui
    }

    @Command(names = "kit.delete", permissionNode = "lolpvp.kits.delete")
    public static void kitDelete(Player player, @Parameter(name = "kit") Kit kit) {
        Kit.getKits().remove(kit);
        player.sendMessage(Thads.get().getLang().getString("lolpvp.kits.removed").replace("%kit%", kit.getName()));
        //TODO: confirmation maybe?
    }

}
