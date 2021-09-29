package hexagonstore.cash.manager;

import hexagonstore.cash.CashPlugin;
import hexagonstore.cash.api.CashAPI;
import hexagonstore.cash.dao.AccountsDao;
import hexagonstore.cash.repository.Database;
import hexagonstore.cash.utils.EC_Config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AccountManager {

    public HashMap<String, Double> accounts;
    public Database database;
    public CashAPI cashAPI;

    public AccountManager(CashPlugin plugin) {
        this.accounts = AccountsDao.accounts;
        this.database = plugin.database;
        this.cashAPI = plugin.cashAPI;
    }

    public void loadAccounts() {
        try {
            PreparedStatement stm = database.getConnection().prepareStatement(
                    "select * from hexagoncash"
            );

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String playerName = rs.getString("player");
                double cash = rs.getDouble("cash");

                cashAPI.createAccount(playerName, false);
                cashAPI.setCash(playerName, cash);
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private boolean containsAccountSQL(String playerName) {
        try {
            PreparedStatement stm = database.getConnection().prepareStatement(
                    "select * from hexagoncash where player = ?"
            );
            stm.setString(1, playerName);
            return stm.executeQuery().next();
        }catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void saveAccounts() {
        PreparedStatement stm;
        try {
            for (Map.Entry<String, Double> map : accounts.entrySet()) {
                String playerName = map.getKey();
                double cash = map.getValue();
                if(containsAccountSQL(playerName)) {
                    stm = database.getConnection().prepareStatement(
                            "update hexagoncash set cash = ? where player = ?"
                    );

                    stm.setDouble(1, cash);
                    stm.setString(2, playerName);

                    stm.executeUpdate();
                }else {
                    stm = database.getConnection().prepareStatement(
                            "insert into hexagoncash(player, cash) VALUES(?,?)"
                    );

                    stm.setString(1, playerName);
                    stm.setDouble(2, cash);

                    stm.executeUpdate();
                }
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
