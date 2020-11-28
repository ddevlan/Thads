package me.ohvalsgod.thads.command.eco;

import com.ddylan.library.command.Command;
import me.ohvalsgod.thads.Thads;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.util.CC;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomyCommands {

    @Command(names = "lols", permission = "lolpvp.eco.sell")
    public static void lols(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            AbstractBallerItem item = Thads.get().getBallerManager().getByPlayer(player);

            if (item == null) {
                player.sendMessage(CC.RED + "You must have a baller item in your hand.");
                return;
            }

            EconomyResponse response = Thads.get().getEcon().depositPlayer(player, getPrice(item));
            sender.sendMessage(ChatColor.GOLD + "You have sold your " + CC.AQUA + item.getName() + CC.GOLD + ".");
            sender.sendMessage(CC.AQUA + response.amount + " " + CC.GREEN + "has been deposited to your account.");
        } else {
            sender.sendMessage(CC.RED + "Only players can use this command.");
        }
    }

    @Command(names = "lolp", permission = "lolpvp.eco.price")
    public static void lolp(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            AbstractBallerItem item = Thads.get().getBallerManager().getByPlayer(player);

            if (item == null) {
                player.sendMessage(CC.RED + "You must have a baller item in your hand.");
                return;
            }

            double price = getPrice(item);
            player.sendMessage(ChatColor.YELLOW + "Your '" + item.getName() + CC.YELLOW + "' sells for about " + CC.GREEN + "$" + price +  CC.YELLOW + ".");
        } else {
            sender.sendMessage(CC.RED + "Only players can use this command.");
        }
    }

    private static double getPrice(AbstractBallerItem abstractBallerItem) {
        return abstractBallerItem.getSellPrice();
    }

}
