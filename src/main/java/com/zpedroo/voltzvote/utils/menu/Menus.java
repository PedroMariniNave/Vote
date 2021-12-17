package com.zpedroo.voltzvote.utils.menu;

import com.zpedroo.voltzvote.listeners.PlayerChatListener;
import com.zpedroo.voltzvote.managers.DataManager;
import com.zpedroo.voltzvote.objects.PlayerData;
import com.zpedroo.voltzvote.objects.ShopItem;
import com.zpedroo.voltzvote.utils.FileUtils;
import com.zpedroo.voltzvote.utils.builder.InventoryBuilder;
import com.zpedroo.voltzvote.utils.builder.InventoryUtils;
import com.zpedroo.voltzvote.utils.builder.ItemBuilder;
import com.zpedroo.voltzvote.utils.config.Messages;
import com.zpedroo.voltzvote.utils.formatter.NumberFormatter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Menus extends InventoryUtils {

    private static Menus instance;
    public static Menus getInstance() { return instance; }

    private ItemStack nextPageItem;
    private ItemStack previousPageItem;

    public Menus() {
        instance = this;
        this.nextPageItem = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).get(), "Next-Page").build();
        this.previousPageItem = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).get(), "Previous-Page").build();
    }

    public void openMainMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.MAIN;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);

        PlayerData data = DataManager.getInstance().load(player);

        for (String items : FileUtils.get().getSection(file, "Inventory.items")) {
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Inventory.items." + items, new String[]{
                    "{player}",
                    "{votes}",
                    "{points}"
            }, new String[]{
                    player.getName(),
                    NumberFormatter.getInstance().formatDecimal((double) data.getVotesAmount()),
                    NumberFormatter.getInstance().format(data.getPointsAmount())
            }).build();
            int slot = FileUtils.get().getInt(file, "Inventory.items." + items + ".slot");
            String action = FileUtils.get().getString(file, "Inventory.items." + items + ".action");

            inventory.addItem(item, slot, () -> {
                switch (action.toUpperCase()) {
                    case "VOTE":
                        player.closeInventory();
                        player.playSound(player.getLocation(), Sound.CLICK, 2f, 2f);

                        for (String msg : Messages.VOTE_LINKS) {
                            player.sendMessage(msg);
                        }
                        break;
                    case "SHOP":
                        openShopMenu(player);
                        player.playSound(player.getLocation(), Sound.CLICK, 2f, 2f);
                        break;
                    case "TOP":
                        openTopVotesMenu(player);
                        player.playSound(player.getLocation(), Sound.CLICK, 2f, 2f);
                        break;
                }
            }, ActionType.ALL_CLICKS);
        }

        inventory.open(player);
    }

    public void openTopVotesMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.TOP_VOTES;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);

        int pos = 0;
        String[] slots = FileUtils.get().getString(file, "Inventory.slots").replace(" ", "").split(",");

        for (PlayerData data : DataManager.getInstance().getCache().getTopVotes()) {
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Item", new String[]{
                    "{player}",
                    "{points}",
                    "{votes}",
                    "{pos}"
            }, new String[]{
                    Bukkit.getOfflinePlayer(data.getUUID()).getName(),
                    NumberFormatter.getInstance().format(data.getPointsAmount()),
                    NumberFormatter.getInstance().formatDecimal((double) data.getVotesAmount()),
                    String.valueOf(++pos)
            }).build();

            int slot = Integer.parseInt(slots[pos - 1]);

            inventory.addItem(item, slot);
        }

        inventory.open(player);
    }

    public void openShopMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.SHOP;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        int nextPageSlot = FileUtils.get().getInt(file, "Inventory.next-page-slot");
        int previousPageSlot = FileUtils.get().getInt(file, "Inventory.previous-page-slot");

        InventoryBuilder inventory = new InventoryBuilder(title, size, previousPageItem, previousPageSlot, nextPageItem, nextPageSlot);

        List<ShopItem> shopItems = DataManager.getInstance().getCache().getShopItems();

        int i = -1;
        String[] slots = FileUtils.get().getString(file, "Inventory.item-slots").replace(" ", "").split(",");
        for (ShopItem item : shopItems) {
            if (item == null) continue;
            if (++i >= slots.length) i = 0;

            ItemStack display = item.getDisplay().clone();
            int slot = Integer.parseInt(slots[i]);

            inventory.addItem(display, slot, () -> {
                player.closeInventory();
                PlayerChatListener.getPlayerChat().put(player, new PlayerChatListener.PlayerChat(player, item));
                for (String msg : Messages.CHOOSE_AMOUNT) {
                    if (msg == null) continue;

                    player.sendMessage(StringUtils.replaceEach(msg, new String[]{
                            "{item}",
                            "{price}"
                    }, new String[]{
                            item.getDisplay().hasItemMeta() ? item.getDisplay().getItemMeta().hasDisplayName() ? item.getDisplay().getItemMeta().getDisplayName() : item.getDisplay().getType().toString() : item.getDisplay().getType().toString(),
                            NumberFormatter.getInstance().format(item.getPrice())
                    }));
                }
            }, ActionType.ALL_CLICKS);
        }

        inventory.open(player);
    }
}