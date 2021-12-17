package com.zpedroo.voltzvote.utils.config;

import com.zpedroo.voltzvote.utils.FileUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Messages {

    public static final String INVALID_AMOUNT = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.invalid-amount"));

    public static final String INSUFFICIENT_POINTS = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.insufficient-points"));

    public static final String NEED_SPACE = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.need-space"));

    public static final List<String> CHOOSE_AMOUNT = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.choose-amount"));

    public static final List<String> VOTE_LINKS = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.vote-links"));

    public static final List<String> SUCCESSFUL_PURCHASED = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.successful-purchased"));

    public static final List<String> SUCCESSFUL_VOTE = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.successful-vote"));

    private static String getColored(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    private static List<String> getColored(List<String> list) {
        List<String> colored = new ArrayList<>(list.size());
        for (String str : list) {
            colored.add(getColored(str));
        }

        return colored;
    }
}