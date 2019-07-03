package me.ohvalsgod.thads;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import lombok.Getter;
import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.command.CommandHandler;
import me.ohvalsgod.thads.config.ConfigCursor;
import me.ohvalsgod.thads.config.FileConfig;
import me.ohvalsgod.thads.data.PlayerDataHandler;
import me.ohvalsgod.thads.listener.ListenerHandler;
import me.ohvalsgod.thads.mysterychest.MysteryChest;
import me.ohvalsgod.thads.mysterychest.MysteryChestManager;
import me.ohvalsgod.thads.reflection.BukkitReflection;
import me.ohvalsgod.thads.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

@Getter
public class Thads extends JavaPlugin {

    public static Random RANDOM = new Random();

    @Getter private static Thads instance;

    //  Dependencies
    private WorldGuardPlugin worldGuard = null;

    //  Files
    private FileConfig mainConfig, settingsConifg, ballerItemsConfig, ballerArmorConfig, langConfig;
    private ConfigCursor lang;

    //  Data handlers
    private BallerManager ballerManager;
    private MysteryChestManager mysteryChestManager;
    private PlayerDataHandler playerDataHandler;

    @Override
    public void onEnable() {
        instance = this;

        CommandHandler.init();
        CommandHandler.loadCommandsFromPackage(instance, "me.ohvalsgod.thads.command.commands");
        ListenerHandler.loadListenersFromPackage(instance, "me.ohvalsgod.thads.listener.listeners");
        ListenerHandler.loadListenersFromPackage(instance, "me.ohvalsgod.thads.menu");

        initDependencies();
        initFiles();
        initDataHandlers();

        startAutoSaves();
    }

    private void startAutoSaves() {
        new BukkitRunnable() {
            @Override
            public void run() {
                int i = 0;
                for (AbstractBallerItem item : ballerManager.getBallerItems()) {
                    ballerManager.saveBallerItem(item);
                    i++;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("lolpvp.autosave.notify.balleritems")) {
                        player.sendMessage(CC.GRAY + "[Auto-Save] " + CC.AQUA + "There have been " + i + " baller item(s) saved.");
                    }
                }
            }
        }.runTaskTimer(instance, 20 * 60 * 2, 20 * 60 * 5);

        new BukkitRunnable() {
            @Override
            public void run() {
                int i = 0;
                for (MysteryChest chest : mysteryChestManager.getMysteryChests()) {
                    mysteryChestManager.saveMysteryChest(chest);
                    i++;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("lolpvp.autosave.notify.pvpchests")) {
                        player.sendMessage(CC.GRAY + "[Auto-Save] " + CC.AQUA + "There have been " + i + " mystery chest(s) saved.");
                    }
                }
            }
        }.runTaskTimer(instance, 20 * 60 * 2, 20 * 60 * 5);
    }

    private void initDependencies() {
        if (!setupWorldGuard()) {
            getLogger().severe("[WARNING] This plugin heavily depends on WorldGuard! Please install it and then restart the server!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    private boolean setupWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if (!(plugin instanceof WorldGuardPlugin)) {
            return false;
        }
        worldGuard = (WorldGuardPlugin) plugin;
        return true;
    }

    private void initFiles() {
        mainConfig = new FileConfig(instance, "config.yml");
        settingsConifg = new FileConfig(instance, "settings.yml");
        ballerItemsConfig = new FileConfig(instance, "baller-items.yml");
        ballerArmorConfig = new FileConfig(instance, "baller-armor.yml");
        langConfig = new FileConfig(instance, "lang.yml");

        lang = new ConfigCursor(langConfig, "");
    }

    private void initDataHandlers() {
        mysteryChestManager = new MysteryChestManager(instance);
        ballerManager = new BallerManager(instance);
        playerDataHandler = new PlayerDataHandler(instance);
        new BukkitReflection();
    }

}
