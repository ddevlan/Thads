package me.ohvalsgod.thads.command.param.defaults;

import me.ohvalsgod.thads.command.param.ParameterType;
import me.ohvalsgod.thads.kits.Kit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class KitParameterType implements ParameterType<Kit> {

    @Override
    public Kit transform(CommandSender sender, String source) {
        for (Kit kit : Kit.getKits()) {
            if (kit.getName().equalsIgnoreCase(source)) {
                if (kit.hasPermission(sender)) {
                    return kit;
                } else {
                    sender.sendMessage(ChatColor.RED + "No permission.");
                }
            }
        }
        sender.sendMessage(ChatColor.RED + "This kit does not exist.");
        return null;
    }

    @Override
    public List<String> tabComplete(Player sender, Set<String> flags, String source) {
        return Kit.getKits().stream().sorted(Comparator.comparingDouble(Kit::getWeight)).map(Kit::getName).collect(Collectors.toList());
    }
}
