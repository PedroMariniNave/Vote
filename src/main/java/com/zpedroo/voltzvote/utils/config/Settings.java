package com.zpedroo.voltzvote.utils.config;

import com.zpedroo.voltzvote.utils.FileUtils;
import com.zpedroo.voltzvote.utils.formatter.NumberFormatter;

import java.math.BigInteger;
import java.util.List;

public class Settings {

    public static final String COMMAND = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.command");

    public static final List<String> ALIASES = FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Settings.aliases");

    public static final Long SAVE_INTERVAL = FileUtils.get().getLong(FileUtils.Files.CONFIG, "Settings.save-interval");

    public static final BigInteger POINTS_PER_VOTE = NumberFormatter.getInstance().filter(FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.save-interval"));
}