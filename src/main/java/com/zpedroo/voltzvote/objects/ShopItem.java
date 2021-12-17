package com.zpedroo.voltzvote.objects;

import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;

public class ShopItem {

    private BigInteger price;
    private int defaultAmount;
    private ItemStack display;
    private ItemStack shopItem;
    private List<String> commands;

    public ShopItem(BigInteger price, int defaultAmount, ItemStack display, ItemStack shopItem, List<String> commands) {
        this.price = price;
        this.defaultAmount = defaultAmount;
        this.display = display;
        this.shopItem = shopItem;
        this.commands = commands;
    }

    public BigInteger getPrice() {
        return price;
    }

    public int getDefaultAmount() {
        return defaultAmount;
    }

    public ItemStack getDisplay() {
        return display;
    }

    public ItemStack getShopItem() {
        return shopItem;
    }

    public List<String> getCommands() {
        return commands;
    }
}