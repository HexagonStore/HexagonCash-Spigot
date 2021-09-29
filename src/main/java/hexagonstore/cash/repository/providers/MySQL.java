package hexagonstore.cash.repository.providers;

import hexagonstore.cash.CashSpigot;
import hexagonstore.cash.repository.Database;
import org.bukkit.Bukkit;

import java.sql.*;

public class MySQL implements Database {

    private Connection connection;

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void open() {
        String host = CashSpigot.getPlugin().config.getString("MySQL.host");
        String user = CashSpigot.getPlugin().config.getString("MySQL.user");
        String password = CashSpigot.getPlugin().config.getString("MySQL.pass");
        String database = CashSpigot.getPlugin().config.getString("MySQL.database");
        String url = "jdbc:mysql://" + host + "/" + database + "?autoReconnect=true";

        try {
            connection = DriverManager.getConnection(url, user, password);
            createTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(CashSpigot.getPlugin());
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void createTable() {
        try {
            PreparedStatement stm = this.connection.prepareStatement(
                    "create table if not exists hexagoncash(`player` TEXT NOT NULL, " +
                            "`cash` DOUBLE NOT NULL)");
            stm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}