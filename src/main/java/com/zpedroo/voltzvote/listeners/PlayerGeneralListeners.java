package com.zpedroo.voltzvote.listeners;

import com.vexsoftware.votifier.model.VotifierEvent;
import com.zpedroo.voltzvote.managers.DataManager;
import com.zpedroo.voltzvote.objects.PlayerData;
import com.zpedroo.voltzvote.utils.config.Messages;
import com.zpedroo.voltzvote.utils.config.Settings;
import com.zpedroo.voltzvote.utils.formatter.NumberFormatter;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.math.BigInteger;

public class PlayerGeneralListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        DataManager.getInstance().save(event.getPlayer());
    }

    @EventHandler
    public void onVote(VotifierEvent event) {
        Player player = Bukkit.getPlayer(event.getVote().getUsername());
        if (player == null) return;

        PlayerData data = DataManager.getInstance().load(player);
        if (data == null) return;

        BigInteger pointsPerVote = Settings.POINTS_PER_VOTE;

        data.addPoints(pointsPerVote);
        data.addVotesAmount(1);

        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
        for (String msg : Messages.SUCCESSFUL_VOTE) {
            player.sendMessage(StringUtils.replaceEach(msg, new String[]{
                    "{points}"
            }, new String[]{
                    NumberFormatter.getInstance().format(pointsPerVote)
            }));
        }
    }
}