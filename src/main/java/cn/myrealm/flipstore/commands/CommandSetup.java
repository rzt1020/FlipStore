package cn.myrealm.flipstore.commands;


import cn.myrealm.flipstore.FlipStore;
import cn.myrealm.flipstore.listeners.SetupListener;
import cn.myrealm.flipstore.managers.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;


/**
 * @program: FlipStore
 * @description: Setup command
 * @author: rzt1020
 * @create: 2022/11/06
 **/
public class CommandSetup extends CommandAbstract implements CommandExecutor {
    // vars
    public static Set<Player> players = new HashSet<>(); // players who are listened

    /**
     * @Description: execute the setup command
     * @Param: [sender]
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public void executeCommand(Player player) {
        if (!players.contains(player)) {
            LanguageManager.instance.sendMessage("setup-start", player);
            LanguageManager.instance.sendMessage("setup-F-key", player);
            LanguageManager.instance.sendMessage("setup-cancel", player);
            Listener listener = new SetupListener(player);
            Bukkit.getPluginManager().registerEvents(listener, FlipStore.instance);
            players.add(player);
        } else {
            LanguageManager.instance.sendMessage("setup-F-key", player);
            LanguageManager.instance.sendMessage("setup-cancel", player);
        }
    }

    @Override
    public void executeCommand() {
    }

    /**
     * @Description: Call when reload command is sent
     * @Param: [sender, command, label, args]
     * @return: boolean
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    @Override
    public boolean onCommand( CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            executeCommand(player);
        } else {
            sender.sendMessage(LanguageManager.instance.getText("only-player"));
        }
        return true;
    }

}
