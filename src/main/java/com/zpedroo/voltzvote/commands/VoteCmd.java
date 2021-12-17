package com.zpedroo.voltzvote.commands;

import com.zpedroo.voltzvote.managers.DataManager;
import com.zpedroo.voltzvote.objects.PlayerData;
import com.zpedroo.voltzvote.utils.formatter.NumberFormatter;
import com.zpedroo.voltzvote.utils.menu.Menus;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigInteger;

public class VoteCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = sender instanceof Player ? (Player) sender : null;

        if (args.length > 0) {
            Player target = null;
            PlayerData data = null;
            BigInteger amount = null;
            switch (args[0].toUpperCase()) {
                case "TOP":
                    if (player == null) break;

                    Menus.getInstance().openTopVotesMenu(player);
                    return true;
                case "SHOP":
                    if (player == null) break;

                    Menus.getInstance().openShopMenu(player);
                    return true;
                case "ADD_VOTES":
                    if (args.length < 3) break;
                    if (!sender.hasPermission("vote.admin")) break;

                    target = Bukkit.getPlayer(args[1]);
                    if (target == null) break;

                    amount = NumberFormatter.getInstance().filter(args[2]);
                    if (amount.intValue() <= 0) break;

                    data = DataManager.getInstance().load(target);
                    if (data == null) break;

                    data.addVotesAmount(amount.intValue());
                    return true;
                case "ADD_POINTS":
                    if (args.length < 3) break;
                    if (!sender.hasPermission("vote.admin")) break;

                    target = Bukkit.getPlayer(args[1]);
                    if (target == null) break;

                    amount = NumberFormatter.getInstance().filter(args[2]);
                    if (amount.intValue() <= 0) break;

                    data = DataManager.getInstance().load(target);
                    if (data == null) break;

                    data.addPoints(amount);
                    return true;
            }
        }


        if (player == null) return true;

        Menus.getInstance().openMainMenu(player);
        return false;
    }
}