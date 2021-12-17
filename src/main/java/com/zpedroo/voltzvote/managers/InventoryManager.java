package com.zpedroo.voltzvote.managers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {

    public static int getFreeSpace(Player player, ItemStack item) {
        int free = 0;

        for (int slot = 0; slot < 36; ++slot) {
            ItemStack items = player.getInventory().getItem(slot);
            if (items == null || items.getType().equals(Material.AIR)) {
                free += item.getMaxStackSize();
                continue;
            }

            if (!items.isSimilar(item)) continue;

            free += item.getMaxStackSize() - items.getAmount();
        }

        return free;
    }
}