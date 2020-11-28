package me.ohvalsgod.thads;

import com.ddylan.library.LibraryPlugin;
import com.ddylan.library.chat.ChatProvider;
import com.ddylan.library.command.CommandHandler;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import lombok.Getter;
import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.baller.item.AbstractBallerItem;
import me.ohvalsgod.thads.baller.object.AbstractBallerObject;
import me.ohvalsgod.thads.command.eco.EconomyCommands;
import me.ohvalsgod.thads.config.ConfigCursor;
import me.ohvalsgod.thads.config.FileConfig;
import me.ohvalsgod.thads.data.PlayerDataHandler;
import me.ohvalsgod.thads.mysterychest.MysteryChest;
import me.ohvalsgod.thads.mysterychest.MysteryChestManager;
import me.ohvalsgod.thads.dev.shopify.ShopifyHandler;
import me.ohvalsgod.thads.listener.ListenerHandler;
import me.ohvalsgod.thads.mysterychest.command.MysteryChestCommands;
import me.ohvalsgod.thads.util.CC;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.logging.Logger;

@Getter
public class Thads extends JavaPlugin {

    //TODO:         fresh install i.e. new deployment of plugin, requires setup for options settings etc on first launch
    //   /*
    //      reformat of the plugin coming very soon (as of 6/6/2020 11:31 AM), swapping over mainly to Dependency
    //      injection for every module of the plugin, they will effectively act as sub-plugins that will all inter-
    //      -act with each other. this will reduce clutter and increase scalability, and will allow external modules
    //      built on the same library to be easily implemented
    //                                                                                                      */

    public static Random RANDOM = new Random();

    private static Thads instance;
    private static int changesMade = 0;

    //  Dependencies
    private CommandHandler commandHandler;
    private WorldGuardPlugin worldGuard = null;
    private LibraryPlugin libraryPlugin;

    //  Files
    private FileConfig mainConfig, settingsConifg, ballerObjectConfig, langConfig, perksConfig;
    private ConfigCursor lang;

    //  Data handlers
    private BallerManager ballerManager;
    private MysteryChestManager mysteryChestManager;
    private PlayerDataHandler playerDataHandler;

    //  Shop.test
    private ShopifyHandler shopifyHandler;

    //  Eco Setup
    private final Logger log = Logger.getLogger("Minecraft");
    private Economy econ = null;
    private Permission perms = null;
    private Chat chat = null;

    @Override
    public void onEnable() {
        instance = this;

        this.libraryPlugin = (LibraryPlugin) getServer().getPluginManager().getPlugin("Library");

        if (libraryPlugin == null) {
            log.severe(String.format("[%s] - Disabled due to no Library dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        setupPermissions();
        setupChat();

        this.libraryPlugin.getChatHandler().registerProvider(new ChatProvider("LOLPVP", '!', "lolpvpprovider", 2) {
            @Override
            public String format(Player player, String s) {
                return player.getDisplayName() + CC.GOLD + "> " + CC.GRAY + s;
            }
        });

        this.commandHandler = libraryPlugin.getCommandHandler();

        commandHandler.registerClass(MysteryChestCommands.class);
        commandHandler.registerClass(GeneralCommands.class);
        commandHandler.registerClass(EconomyCommands.class);

        initFiles();
        initDependencies();
        initDataHandlers();

        ListenerHandler.loadListenersFromPackage(instance, "me.ohvalsgod.thads.listener.listeners");
        ListenerHandler.loadListenersFromPackage(instance, "me.ohvalsgod.thads.menu");
        startAutoSaves();
        fixJrebel();
    }

    @Override
    public void onDisable() {
        this.commandHandler.unregisterClass(GeneralCommands.class);
        this.commandHandler.unregisterClass(EconomyCommands.class);
        this.commandHandler.unregisterClass(MysteryChestCommands.class);

        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        if (getServer().getPluginManager().getPlugin("PermissionsEx") == null) {
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        perms = rsp.getProvider();
        return perms != null;
    }

    private void startAutoSaves() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (changesMade > 0) {
                    int i = 0;
                    for (AbstractBallerItem item : Thads.get().getBallerManager().getBallerItems()) {
                        Thads.get().getBallerManager().saveBallerItem(item);
                        i++;
                    }

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.hasPermission("lolpvp.autosave.notify.balleritems")) {
                            player.sendMessage(CC.GRAY + "[Auto-Save] " + CC.AQUA + "There have been " + i + " baller item(s) saved.");
                            player.sendMessage(CC.GRAY + "[Auto-Save] " + CC.AQUA + "There have been " + changesMade + " changes made.");
                        }
                    }
                    changesMade = 0;
                } else {
                    getServer().getConsoleSender().sendMessage(CC.GRAY + "[Auto-Save] Nothing was needed to be saved.");
                }
            }
        }.runTaskTimer(instance, 20 * 60 * 5, 20 * 60 * 5);

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
                        player.sendMessage(CC.GRAY + "[Auto-Save] " + CC.AQUA + "There have been " + i + " mystery chests saved.");
                    }
                }
            }
        }.runTaskTimer(instance, 20 * 60 * 2, 20 * 60 * 5);
    }

    //TODO: Code your own buycraft :D
    private void shop() {
        this.shopifyHandler = new ShopifyHandler(instance, "", "");
    }

    private void initDependencies() {
        if (!setupWorldGuard()) {
            getLogger().severe("[WARNING] This plugin heavily depends on WorldGuard! Please install it and then restart the server!");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    /*

     */

    private boolean setupWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if (!(plugin instanceof WorldGuardPlugin)) {
            return false;
        }
        worldGuard = (WorldGuardPlugin) plugin;
        return true;
    }

    public void fixJrebel() {
        System.out.println(" ");
        System.out.println("•-------------------------------------------------------•");
        System.out.println("|                   Start of JRebel                     |");
        System.out.println("|                  [Debug] Messages                     |");
        System.out.println("•-------------------------------------------------------•");
        System.out.println(" ");
        getBallerManager().getBallerObjects().forEach(AbstractBallerObject::fixJrebel);
        System.out.println(" ");
        System.out.println("•-------------------------------------------------------•");
        System.out.println("|                    End of JRebel                      |");
        System.out.println("|                  [Debug] Messages                     |");
        System.out.println("•-------------------------------------------------------•");
        System.out.println(" ");
    }

    public void initFiles() {
        mainConfig = new FileConfig(instance, "config.yml");
        settingsConifg = new FileConfig(instance, "settings.yml");
        ballerObjectConfig = new FileConfig(instance, "baller-objects.yml");
        langConfig = new FileConfig(instance, "lang.yml");

        lang = new ConfigCursor(langConfig, "");
    }

    private void initDataHandlers() {
        mysteryChestManager = new MysteryChestManager(instance);
        ballerManager = new BallerManager(instance, ballerObjectConfig);
        playerDataHandler = new PlayerDataHandler(instance);
    }

    public boolean isPlayerInPVP(Player player){
        final RegionManager regionManager = getWorldGuard().getRegionManager(player.getLocation().getWorld());
        final ApplicableRegionSet set = regionManager.getApplicableRegions(player.getLocation());
        final LocalPlayer localPlayer = getWorldGuard().wrapPlayer(player);
        return set.testState(localPlayer, DefaultFlag.PVP);
    }

    /*
        Weird function. should find change for another class later.
     */
    public static void change() {
        changesMade = changesMade + 1;
    }

    public static Thads get() {
        return instance;
    }
}