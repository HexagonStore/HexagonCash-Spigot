package hexagonstore.cash.listeners;

import hexagonstore.cash.CashSpigot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    @EventHandler
    void evento(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        CashSpigot.getPlugin().cashAPI.createAccount(player.getName(), player.hasPlayedBefore());
    }
}
