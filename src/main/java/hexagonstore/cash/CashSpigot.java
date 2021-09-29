package hexagonstore.cash;

import hexagonstore.cash.api.CashAPI;
import hexagonstore.cash.commands.CashCommand;
import hexagonstore.cash.listeners.JoinEvent;
import hexagonstore.cash.listeners.QuitEvent;
import hexagonstore.cash.manager.AccountManager;
import hexagonstore.cash.repository.Database;
import hexagonstore.cash.repository.providers.MySQL;
import hexagonstore.cash.repository.providers.SQLite;
import hexagonstore.cash.utils.EC_Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CashSpigot extends JavaPlugin {

    private static CashSpigot plugin;

    public EC_Config config;
    public AccountManager manager;
    public CashAPI cashAPI;

    public Database database;

    public static CashSpigot getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        config = new EC_Config(null, "config.yml", false);

        database = config.getBoolean("MySQL.ativar") ? new MySQL() : new SQLite();
        database.open();

        cashAPI = new CashAPI();

        manager = new AccountManager(this);
        manager.loadAccounts();

        Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
        Bukkit.getPluginManager().registerEvents(new QuitEvent(), this);
        getCommand("cash").setExecutor(new CashCommand());
    }

    @Override
    public void onDisable() {
        manager.saveAccounts();
        database.close();
    }
}
