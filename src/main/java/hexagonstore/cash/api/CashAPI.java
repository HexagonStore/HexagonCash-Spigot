package hexagonstore.cash.api;

import hexagonstore.cash.CashSpigot;
import hexagonstore.cash.dao.AccountsDao;
import hexagonstore.cash.utils.EC_Config;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CashAPI {

    public HashMap<String, Double> accounts = AccountsDao.accounts;
    public EC_Config config = CashSpigot.getPlugin().config;

    public double getCashAndCreateAccount(String playerName, boolean initialCash) {
        playerName = playerName.toLowerCase();
        if(accounts.containsKey(playerName)) {
            return accounts.get(playerName);
        }

        createAccount(playerName, initialCash);
        return getCash(playerName);
    }

    public void createAccount(String playerName, boolean initialCash) {
        if(!accounts.containsKey(playerName)) {
            playerName = playerName.toLowerCase();
            double cashInicial = initialCash ? (config.getBoolean("CashInicial.ativar") ? config.getDouble("CashInicial.cash") : 0.0) : 0.0;
            accounts.put(playerName, cashInicial);
        }
    }

    public void openShop(Player player) { CashSpigot.getPlugin().shopManager.open(player); }

    public void setCash(String playerName, double cash) {
        accounts.put(playerName.toLowerCase(), cash);
    }
    public double getCash(String playerName) {
        return accounts.get(playerName.toLowerCase());
    }
    public boolean containsCash(String playerName, double cash) {
        return getCash(playerName) >= cash;
    }
}
