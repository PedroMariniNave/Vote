package com.zpedroo.voltzvote.listeners;

import com.zpedroo.voltzvote.VoltzVote;
import com.zpedroo.voltzvote.managers.DataManager;
import com.zpedroo.voltzvote.managers.InventoryManager;
import com.zpedroo.voltzvote.objects.PlayerData;
import com.zpedroo.voltzvote.objects.ShopItem;
import com.zpedroo.voltzvote.utils.config.Messages;
import com.zpedroo.voltzvote.utils.formatter.NumberFormatter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.*;

public class PlayerChatListener implements Listener {

    private static Map<Player, PlayerChat> playerChat;

    static {
        playerChat = new HashMap<>(8);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!getPlayerChat().containsKey(event.getPlayer())) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        PlayerChat playerChat = getPlayerChat().remove(player);

        BigInteger amount = NumberFormatter.getInstance().filter(event.getMessage());
        if (amount.signum() <= 0) {
            player.sendMessage(Messages.INVALID_AMOUNT);
            return;
        }

        ShopItem item = playerChat.getItem();
        int limit = item.getDisplay().getMaxStackSize() == 1 ? 36 : 2304;
        if (amount.compareTo(BigInteger.valueOf(limit)) > 0) amount = BigInteger.valueOf(limit);

        Integer freeSpace = InventoryManager.getFreeSpace(player, item.getDisplay());
        if (freeSpace < amount.intValue()) {
            player.sendMessage(StringUtils.replaceEach(Messages.NEED_SPACE, new String[]{
                    "{has}",
                    "{need}"
            }, new String[]{
                    NumberFormatter.getInstance().formatDecimal(freeSpace.doubleValue()),
                    NumberFormatter.getInstance().formatDecimal(amount.doubleValue())
            }));
            return;
        }

        PlayerData data = DataManager.getInstance().load(player);
        BigInteger points = data.getPointsAmount();
        BigInteger price = item.getPrice().multiply(amount);

        if (points.compareTo(price) < 0) {
            player.sendMessage(StringUtils.replaceEach(Messages.INSUFFICIENT_POINTS, new String[]{
                    "{has}",
                    "{need}"
            }, new String[]{
                    NumberFormatter.getInstance().format(points),
                    NumberFormatter.getInstance().format(price)
            }));
            return;
        }

        data.removePoints(price);
        if (item.getShopItem() != null) {
            ItemStack toGive = item.getShopItem().clone();
            if (toGive.getMaxStackSize() == 64) {
                toGive.setAmount(amount.intValue());
                player.getInventory().addItem(toGive);
                return;
            }

            for (int i = 0; i < amount.intValue(); ++i) {
                player.getInventory().addItem(toGive);
            }
        }

        for (String cmd : item.getCommands()) {
            if (cmd == null) continue;

            final BigInteger finalAmount = amount;
            VoltzVote.get().getServer().getScheduler().runTaskLater(VoltzVote.get(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtils.replaceEach(cmd, new String[]{
                    "{player}",
                    "{amount}"
            }, new String[]{
                    player.getName(),
                    String.valueOf(finalAmount.intValue() * item.getDefaultAmount())
            })), 0L);
        }

        for (String msg : Messages.SUCCESSFUL_PURCHASED) {
            if (msg == null) continue;

            player.sendMessage(StringUtils.replaceEach(msg, new String[]{
                    "{item}",
                    "{amount}",
                    "{price}"
            }, new String[]{
                    item.getDisplay().hasItemMeta() ? item.getDisplay().getItemMeta().hasDisplayName() ? item.getDisplay().getItemMeta().getDisplayName() : item.getDisplay().getType().toString() : item.getDisplay().getType().toString(),
                    NumberFormatter.getInstance().formatDecimal(amount.doubleValue()),
                    NumberFormatter.getInstance().format(price)
            }));
        }

        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 0.5f, 100f);
    }

    public static Map<Player, PlayerChat> getPlayerChat() {
        return playerChat;
    }

    public static class PlayerChat {

        private Player player;
        private ShopItem item;

        public PlayerChat(Player player, ShopItem item) {
            this.player = player;
            this.item = item;
        }

        public Player getPlayer() {
            return player;
        }

        public ShopItem getItem() {
            return item;
        }
    }
}