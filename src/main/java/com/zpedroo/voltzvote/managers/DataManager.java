package com.zpedroo.voltzvote.managers;

import com.zpedroo.voltzvote.managers.cache.DataCache;
import com.zpedroo.voltzvote.mysql.DBConnection;
import com.zpedroo.voltzvote.objects.PlayerData;
import com.zpedroo.voltzvote.objects.ShopItem;
import com.zpedroo.voltzvote.utils.FileUtils;
import com.zpedroo.voltzvote.utils.builder.ItemBuilder;
import com.zpedroo.voltzvote.utils.formatter.NumberFormatter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;

public class DataManager {

    private static DataManager instance;
    public static DataManager getInstance() { return instance; }

    private DataCache dataCache;

    public DataManager() {
        instance = this;
        this.dataCache = new DataCache();
        this.loadShopItems();
    }

    public PlayerData load(Player player) {
        PlayerData data = dataCache.getPlayerData().get(player);
        if (data == null) {
            data = DBConnection.getInstance().getDBManager().loadData(player);
            dataCache.getPlayerData().put(player, data);
        }

        return data;
    }

    public void save(Player player) {
        PlayerData data = dataCache.getPlayerData().get(player);
        if (data == null) return;
        if (!data.isQueueUpdate()) return;

        DBConnection.getInstance().getDBManager().saveData(data);
        data.setUpdate(false);
    }

    public void saveAll() {
        new HashSet<>(dataCache.getPlayerData().keySet()).forEach(this::save);
    }

    private void loadShopItems() {
        FileUtils.Files file = FileUtils.Files.SHOP;
        for (String str : FileUtils.get().getSection(file, "Inventory.items")) {
            if (str == null) continue;

            BigInteger price = NumberFormatter.getInstance().filter(FileUtils.get().getString(file, "Inventory.items." + str + ".price"));
            int defaultAmount = FileUtils.get().getInt(file, "Inventory.items." + str + ".default-amount", 1);
            ItemStack display = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Inventory.items." + str + ".display", new String[]{
                    "{price}"
            }, new String[]{
                    NumberFormatter.getInstance().format(price)
            }).build();
            ItemStack shopItem = null;
            if (FileUtils.get().getFile(file).get().contains("Inventory.items." + str + ".shop-item")) {
                shopItem = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Inventory.items." + str + ".shop-item").build();
            }
            List<String> commands = FileUtils.get().getStringList(file, "Inventory.items." + str + ".commands");

            dataCache.getShopItems().add(new ShopItem(price, defaultAmount, display, shopItem, commands));
        }
    }

    public DataCache getCache() {
        return dataCache;
    }
}