package hexagonstore.cash.manager;

import hexagonstore.cash.CashSpigot;
import hexagonstore.cash.api.CashAPI;
import hexagonstore.cash.model.ShopItem;
import hexagonstore.cash.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ShopManager {

    private EC_Config config;
    public HashMap<Integer, ShopItem> products = new HashMap<>();

    private ItemStack nextPageItem;
    private ItemStack previousPageItem;

    public ShopManager(CashSpigot plugin) {
        this.config = plugin.shopConfig;

        nextPageItem =
                new ItemBuilder(
                        config.getBoolean("Shop.next-page-item.skull-url.ativar")
                                ? SkullURL.getSkull(config.getString("Shop.next-page-item.skull-url.code"))
                                : new ItemStack(Material.valueOf(config.getString("Shop.next-page-item.material")), 1, config.getShort("Shop.next-page-item.data")))
                        .setName(config.getString("Shop.next-page-item.name").replace("&", "§"))
                        .setLore(config.getStringList("Shop.next-page-item.lore").stream().map(line -> line.replace("&", "§")).collect(Collectors.toList()))
                        .glow(config.getBoolean("Shop.next-page-item.glow"))
                        .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .toItemStack();

        previousPageItem =
                new ItemBuilder(
                        config.getBoolean("Shop.previous-page-item.skull-url.ativar")
                                ? SkullURL.getSkull(config.getString("Shop.previous-page-item.skull-url.code"))
                                : new ItemStack(Material.valueOf(config.getString("Shop.previous-page-item.material")), 1, config.getShort("Shop.previous-page-item.data")))
                        .setName(config.getString("Shop.previous-page-item.name").replace("&", "§"))
                        .setLore(config.getStringList("Shop.previous-page-item.lore").stream().map(line -> line.replace("&", "§")).collect(Collectors.toList()))
                        .glow(config.getBoolean("Shop.previous-page-item.glow"))
                        .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .toItemStack();
    }

    public void open(Player player) {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        products.values().forEach(shopItem -> items.put(shopItem.getSlot(), shopItem.getIt().clone()));

        Scroller scroller = Scroller.builder()
                .withName(config.getString("Shop.name").replace("&", "§"))
                .withItems(new ArrayList<>(items.values()))
                .withSize(config.getInt("Shop.rows") * 9)
                .withAllowedSlots(new ArrayList<>(items.keySet()))
                .withNextPageSlot(config.getInt("Shop.next-page-item.slot"))
                .withPreviousPageSlot(config.getInt("Shop.previous-page-item.slot"))
                .withNextPageItem(nextPageItem)
                .withPreviousPageItem(previousPageItem)
                .build();
        scroller.open(player);
    }

    public void loadProducts() {
        ConfigurationSection section = config.getConfigurationSection("Products");
        if (section != null) {
            for (String productID : section.getKeys(false)) {
                ConfigurationSection key = config.getConfigurationSection("Products." + productID);
                double price = 1;
                try {
                    price = NumberFormatter.parseString(key.getString("price"));
                } catch (Exception ignored) {}
                ArrayList<String> newLore = new ArrayList<>();
                ItemStack productItem = new ItemBuilder(
                        key.getBoolean("skull-url.ativar")
                                ? SkullURL.getSkull(key.getString("skull-url.code"))
                                : new ItemStack(Material.valueOf(key.getString("material")), 1, (short) key.getInt("data")))
                        .setName(key.getString("name").replace("&", "§"))
                        .setLore(key.getStringList("lore").stream().map(line -> line.replace("&", "§")).collect(Collectors.toList()))
                        .glow(key.getBoolean("glow"))
                        .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .toItemStack();
                for (String line : productItem.getItemMeta().getLore()) {
                    newLore.add(line.replace("{price}", NumberFormatter.formatNumber(price)));
                }
                ItemMeta mt = productItem.getItemMeta();
                mt.setLore(newLore);
                productItem.setItemMeta(mt);
                int slot = key.getInt("slot");
                products.put(slot, new ShopItem(key.getString("displayName"), productItem, slot, price));
            }
        } else
            Bukkit.getConsoleSender().sendMessage("§c[HexagonCash] A section 'Products' não foi encontrada no arquivo 'shop.yml', por favor, contate o suporte do plugin.");
    }
}
