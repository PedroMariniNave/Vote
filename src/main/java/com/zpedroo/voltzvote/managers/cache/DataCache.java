package com.zpedroo.voltzvote.managers.cache;

import com.zpedroo.voltzvote.mysql.DBConnection;
import com.zpedroo.voltzvote.objects.PlayerData;
import com.zpedroo.voltzvote.objects.ShopItem;
import org.bukkit.entity.Player;

import java.util.*;

public class DataCache {

    private Map<Player, PlayerData> playerData;
    private List<ShopItem> shopItems;
    private List<PlayerData> topVotes;

    public DataCache() {
        this.playerData = new HashMap<>(64);
        this.shopItems = new ArrayList<>(8);
    }

    public Map<Player, PlayerData> getPlayerData() {
        return playerData;
    }

    public List<ShopItem> getShopItems() {
        return shopItems;
    }

    public List<PlayerData> getTopVotes() {
        if (topVotes == null) {
            this.topVotes = DBConnection.getInstance().getDBManager().getTopVotes();
        }

        return topVotes;
    }

    public void setTop(List<PlayerData> topOnline) {
        this.topVotes = topOnline;
    }
}