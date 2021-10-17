package hexagonstore.cash;

import hexagonstore.cash.api.CashAPI;
import hexagonstore.cash.commands.CashCommand;
import hexagonstore.cash.listeners.ClickEvent;
import hexagonstore.cash.listeners.JoinEvent;
import hexagonstore.cash.manager.AccountManager;
import hexagonstore.cash.manager.ShopManager;
import hexagonstore.cash.repository.Database;
import hexagonstore.cash.repository.providers.MySQL;
import hexagonstore.cash.repository.providers.SQLite;
import hexagonstore.cash.utils.EC_Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import hexagonstore.cash.papi.CashExpansion;
public class CashSpigot extends JavaPlugin {

    private static CashSpigot plugin;

    public EC_Config config;
    public EC_Config shopConfig;

    public AccountManager accountManager;
    public ShopManager shopManager;
    public CashAPI cashAPI;

    public Database database;

    public static CashSpigot getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        config = new EC_Config(null, "config.yml", false);
        shopConfig = new EC_Config(null, "shop.yml", false);

        database = config.getBoolean("MySQL.ativar") ? new MySQL() : new SQLite();
        database.open();

        cashAPI = new CashAPI();

        accountManager = new AccountManager(this);
        accountManager.loadAccounts();

        shopManager = new ShopManager(this);
        shopManager.loadProducts();

        Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
        Bukkit.getPluginManager().registerEvents(new ClickEvent(this), this);
        getCommand("cash").setExecutor(new CashCommand());
        new CashExpansion().register();
    }

    @Override
    public void onDisable() {
        accountManager.saveAccounts();
        database.close();
    }
}
