package hexagonstore.cash.model;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ShopItem {

    private String name;
    private ArrayList<String> commands;
    private ItemStack it;

    private double price;
    private int slot;

    public ShopItem(String name, ItemStack it, int slot, double price) {
        this.name =  name;
        this.it = it;
        this.slot = slot;
        this.price = price;

        this.commands = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getIt() {
        return it;
    }

    public void setIt(ItemStack it) {
        this.it = it;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<String> getCommands() {
        return commands;
    }

    public void setCommands(ArrayList<String> commands) {
        this.commands = commands;
    }
}
