package com.zpedroo.voltzvote.tasks;

import com.zpedroo.voltzvote.managers.DataManager;
import com.zpedroo.voltzvote.mysql.DBConnection;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import static com.zpedroo.voltzvote.utils.config.Settings.SAVE_INTERVAL;

public class SaveTask extends BukkitRunnable {

    public SaveTask(Plugin plugin) {
        this.runTaskTimerAsynchronously(plugin, SAVE_INTERVAL, SAVE_INTERVAL);
    }

    @Override
    public void run() {
        DataManager.getInstance().saveAll();
        DataManager.getInstance().getCache().setTop(DBConnection.getInstance().getDBManager().getTopVotes());
    }
}