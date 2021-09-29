package hexagonstore.cash;

import hexagonstore.cash.api.CashAPI;
import hexagonstore.cash.commands.CashCommand;
import hexagonstore.cash.manager.AccountManager;
import hexagonstore.cash.repository.Database;
import hexagonstore.cash.repository.providers.MySQL;
import hexagonstore.cash.repository.providers.SQLite;
import hexagonstore.cash.utils.EC_Config;
import org.bukkit.plugin.java.JavaPlugin;

public class CashPlugin extends JavaPlugin {

    private static CashPlugin instance;

    public EC_Config config;
    public AccountManager manager;
    public CashAPI cashAPI;

    public Database database;

    public static CashPlugin getPlugin() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        config = new EC_Config(null, "config.yml", false);

        database = config.getBoolean("MySQL.ativar") ? new MySQL() : new SQLite();
        database.open();

        cashAPI = new CashAPI();

        manager = new AccountManager(this);
        manager.loadAccounts();

        getCommand("cash").setExecutor(new CashCommand());
    }

    @Override
    public void onDisable() {
        manager.saveAccounts();
        database.close();
    }
}
