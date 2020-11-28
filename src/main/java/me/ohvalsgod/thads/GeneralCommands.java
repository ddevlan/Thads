package me.ohvalsgod.thads;

import com.ddylan.library.command.Command;
import com.ddylan.library.command.Param;
import me.ohvalsgod.thads.baller.gui.object.LOLObjectsMenu;
import me.ohvalsgod.thads.menu.menus.ConfirmMenu;
import me.ohvalsgod.thads.util.CC;
import me.ohvalsgod.thads.util.callback.TypeCallback;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class GeneralCommands {

    @Command(names = {"lol", "lol help"}, permission = "lolpvp.help")
    public static void lolhelp(CommandSender sender) {
        for (String string : Thads.get().getLang().getStringList("lol.help")) {
            sender.sendMessage(CC.translate(string));
        }
    }

    @Command(names = {"lolreload", "lol reload"}, permission = "lolpvp.admin.reload")
    public static void lolReload(Player player) {
        new ConfirmMenu(CC.translate("&6Are you sure?"), (TypeCallback<Boolean>) data -> {
            if (data) {
                Thads.get().initFiles();
                player.sendMessage("You have reloaded the config.");
            }
        }, true).openMenu(player);
    }

    @Command(names = {"lol weapons", "lolweapons"}, permission = "lolpvp.weapons.gui")
    public static void lolweapons(Player player) {
        new LOLObjectsMenu().openMenu(player);
    }

    @Command(names = {"ironman", "im"}, permission = "lolpvp.kits.ironman")
    public static void ironman(Player player) {
        player.addPotionEffects(Arrays.asList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60*20*3, 2, true), new PotionEffect(PotionEffectType.SPEED, 60*20*3, 2, true)));
        player.sendMessage(CC.translate("&7You have taken the &bIRONMAN &7kit!"));
    }

    @Command(names = {"regen", "re"}, permission = "lolpvp.kits.ironman")
    public static void regen(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60*20*3, 1));
        player.sendMessage(CC.translate("&7You have taken the &bREGEN &7kit!"));
    }

    @Command(names = "gear", permission = "op")
    public static void gear(Player player, @Param(name = "armor") String armor, @Param(name = "weapon") String weapon) {
        player.getInventory().clear();
        regen(player);
        ironman(player);
        player.getInventory().setArmorContents(Thads.get().getBallerManager().getArmorByName(armor).getBallerArmor());
        player.getInventory().addItem(Thads.get().getBallerManager().getItemByName(weapon).getBallerItemStack());
        ItemStack itemStack = new ItemStack(Material.POTION);
        itemStack.setDurability((short) 16421);
        for (int i = 0; i < 9*4; i++) {
            if (player.getInventory().getItem(i) == null) {
                player.getInventory().addItem(itemStack);
            }
        }
    }

}
