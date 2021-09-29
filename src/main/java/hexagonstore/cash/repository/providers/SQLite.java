package hexagonstore.cash.repository.providers;

import hexagonstore.cash.CashSpigot;
import hexagonstore.cash.repository.Database;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;

public class SQLite implements Database {

    private Connection connection;

    @Override
    public Connection getConnection() {
        return connection;
    }

    private void createDirectory(String directory) {
        if(directory != null) {
            File currentDirectory = new File(CashSpigot.getPlugin().getDataFolder(), directory.replace("/", File.separator));
            currentDirectory.mkdirs();
        }
    }

    @Override
    public void open() {
        createDirectory("sqlite");
        File file = new File(CashSpigot.getPlugin().getDataFolder() + File.separator + "sqlite", "database.db");
        String url = "jdbc:sqlite:" + file;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);
            createTable();
        } catch (Exception ex) {
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(CashSpigot.getPlugin());
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTable() {
        try {
            PreparedStatement stm = this.connection.prepareStatement(
                    "create table if not exists hexagoncash(`player` TEXT, " +
                            "`cash` DOUBLE)"
            );
            stm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}