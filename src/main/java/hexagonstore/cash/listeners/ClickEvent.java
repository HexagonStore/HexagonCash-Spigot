package hexagonstore.cash.listeners;

import hexagonstore.cash.CashSpigot;
import hexagonstore.cash.api.CashAPI;
import hexagonstore.cash.manager.ShopManager;
import hexagonstore.cash.model.ShopItem;
import hexagonstore.cash.utils.EC_Config;
import hexagonstore.cash.utils.NumberFormatter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickEvent implements Listener {

    private EC_Config config;
    private EC_Config shopConfig;
    private ShopManager shopManager;
    private CashAPI cashAPI;

    public ClickEvent(CashSpigot plugin) {
        config = plugin.config;
        shopConfig = plugin.shopConfig;

        shopManager = plugin.shopManager;
        cashAPI = plugin.cashAPI;
    }

    @EventHandler
    void evento(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase(shopConfig.getString("Shop.name").replace("&", "§"))) {
            if (shopManager.products.containsKey(e.getSlot())) {
                e.setCancelled(true);
                if(e.isLeftClick() && e.isShiftClick()) {
                    ShopItem shopItem = shopManager.products.get(e.getSlot());
                    if(cashAPI.containsCash(player.getName(), shopItem.getPrice())) {
                        cashAPI.setCash(player.getName(), cashAPI.getCash(player.getName()) - shopItem.getPrice());
                        shopItem.getCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("@player", player.getName())));

                        player.closeInventory();
                        config.getStringList("Messages.product_buy").forEach(line -> player.sendMessage(line.replace("@cash", String.valueOf(cashAPI.getCash(player.getName()))).replace("@productPrice", NumberFormatter.formatNumber(shopItem.getPrice())).replace("@productName", shopItem.getName()).replace("&", "§")));
                    }else {
                        player.closeInventory();
                        player.sendMessage(config.getString("Messages.no_cash").replace("&", "§"));
                    }
                }else {
                    player.closeInventory();
                    player.sendMessage(config.getString("Messages.no_click").replace("&", "§"));
                }
            }
        }
    }
}
