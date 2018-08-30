package me.ohvalsgod.thads;

import lombok.Getter;
import me.ohvalsgod.thads.baller.BallerManager;
import me.ohvalsgod.thads.config.FileConfig;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Thads  extends JavaPlugin {

    @Getter private static Thads instance;

    //  Files
    private FileConfig mainConfig, settingsConifg, ballerItemsConfig;

    //  Data handlers
    private ServerSettings serverSettings;
    private BallerManager ballerManager;

    @Override
    public void onEnable() {
        instance = this;

        //TODO: vault check

        initFiles();
        initDataHandlers();
    }

    private void initFiles() {
        mainConfig = new FileConfig(instance, "config.yml");
        settingsConifg = new FileConfig(instance, "settings.yml");
        ballerItemsConfig = new FileConfig(instance, "balleritems.yml");
    }

    private void initDataHandlers() {
        serverSettings = new ServerSettings(this);
        ballerManager = new BallerManager(this);
    }

}
