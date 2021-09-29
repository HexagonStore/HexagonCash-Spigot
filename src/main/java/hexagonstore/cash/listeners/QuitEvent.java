package hexagonstore.cash.listeners;

import hexagonstore.cash.CashSpigot;
import hexagonstore.cash.dao.AccountsDao;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {

    @EventHandler
    void evento(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        AccountsDao.accounts.remove(player.getName().toLowerCase());
    }
}
