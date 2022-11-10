package cn.myrealm.flipstore;

import cn.myrealm.flipstore.commands.CommandFlipstore;
import cn.myrealm.flipstore.managers.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FlipStore extends JavaPlugin {
    public static FlipStore instance; // plugin instance
    public static List<Manager> managers; // manager list

    /**
     * @Description: on enable
     * @Param: []
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/10/14
    **/
    @Override
    public void onEnable() {
        instance = this;
        File file = new File(getDataFolder(),"config.yml");
        if(!file.exists()) {
            saveDefaultConfig();
            resourcesOutput();
        }
        setupManager();
        Objects.requireNonNull(getCommand("flipstore")).setExecutor(new CommandFlipstore());
    }

    /**
     * @Description: on disable
     * @Param: []
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/10/14
    **/
    @Override
    public void onDisable() {
        instance = null;
        DatabaseManager.instance.shutdown();
    }

    /**
     * @Description: reload the plugin
     * @Param: []
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/9/28
     **/
    public void reload() {
        for (Manager manager : managers) {
            manager.reload();
        }
    }

    public void disable(String reason) {
        LanguageManager.instance.severe(reason);
        Bukkit.getPluginManager().disablePlugin(this);
    }

    public void setupManager() {
        managers = new ArrayList<>();
        managers.add(new LanguageManager());
        managers.add(new DatabaseManager());
    }

    /**
     * @Description: Resources output
     * @Param: []
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/9/28
     **/
    public void resourcesOutput() {
        saveResource("language/chinese.yml",false);
    }

    /**
     * @Description: Color symbols and dex for strings
     * @Param: [s]
     * @return: java.lang.String
     * @Author: rzt1020
     * @Date: 2022/9/28
     **/
    public static String parseColor(@NonNull String s){
        Pattern pattern = Pattern.compile("<#[a-fA-F0-9]{6}>");
        Matcher match = pattern.matcher(s);
        while (match.find()) {
            String color = s.substring(match.start(),match.end());
            s = s.replace(color, ChatColor.of(color.replace("<","").replace(">","")) + "");
            match = pattern.matcher(s);
        }
        return s.replace("&","\u00a7").replace("§§","&");
    }

    /**
     * @Description: Calculate the hash value of a string by MD5 standard
     * @Param: [s]
     * @return: java.lang.String
     * @Author: rzt1020
     * @Date: 2022/11/08
    **/
    public static String toHash(@NonNull String s) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            LanguageManager.instance.severe(e);
        }
        byte[] result = md != null ? md.digest() : new byte[0];
        return new BigInteger(1, result).toString(16);
    }
}
