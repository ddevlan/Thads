package me.ohvalsgod.thads;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import lombok.Getter;
import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.config.ConfigCursor;
import me.ohvalsgod.thads.config.FileConfig;
import me.ohvalsgod.thads.jedis.JedisSettings;
import me.ohvalsgod.thads.jedis.ThadsJedis;
import me.ohvalsgod.thads.uuid.UUIDCache;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Thads  extends JavaPlugin {

    @Getter private static Thads instance;

    //  Dependencies
    private WorldGuardPlugin worldGuard = null;
    private static Economy econ = null;
    private static Chat chat = null;

    //  Files
    private FileConfig mainConfig, settingsConifg, ballerItemsConfig, langConfig;

    //  Database
    private ThadsJedis thadsJedis;

    //  Data handlers
    private ServerSettings serverSettings;
    private BallerManager ballerManager;
    private UUIDCache uuidCache;

    @Override
    public void onEnable() {
        instance = this;

        initDependencies();
        initFiles();
        initDatabases();
        initDataHandlers();
    }

    private void initDatabases() {
        final ConfigCursor cursor = new ConfigCursor(mainConfig, "database");

        final JedisSettings settings = new JedisSettings(
                cursor.getString("redis.host"),
                cursor.getInt("redis.port"),
                cursor.getString("redis.password")
        );
        thadsJedis = new ThadsJedis(settings);
    }

    private void initDependencies() {
        if (!setupWorldGuard()) {
            getLogger().severe("[WARNING] This plugin heavily depends on WorldGuard! Please install it and then restart the server!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!setupEconomy()) {
            getLogger().severe("[WARNING] This plugin heavily depends on Vault! Please install it and then restart the server!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupChat();
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

    private boolean setupWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return false;
        }
        worldGuard = (WorldGuardPlugin) plugin;
        return true;
    }

    private void initFiles() {
        mainConfig = new FileConfig(instance, "config.yml");
        settingsConifg = new FileConfig(instance, "settings.yml");
        ballerItemsConfig = new FileConfig(instance, "balleritems.yml");
    }

    private void initDataHandlers() {
        serverSettings = new ServerSettings(this);
        ballerManager = new BallerManager(this);
        uuidCache = new UUIDCache(this);
    }

}
