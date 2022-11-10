package cn.myrealm.flipstore.commands;

import cn.myrealm.flipstore.FlipStore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @program: FlipStore
 * @description: Reload this plugin
 * @author: rzt1020
 * @create: 2022/10/14
 **/
public class CommandReload extends CommandAbstract implements CommandExecutor {

    /**
     * @Description: execute the reload command
     * @Param: []
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/5
    **/
    @Override
    public void executeCommand() {
        FlipStore.instance.reload();
    }

    /**
     * @Description: Call when reload command is sent
     * @Param: [sender, command, label, args]
     * @return: boolean
     * @Author: rzt1020
     * @Date: 2022/11/5
    **/
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        executeCommand();
        return true;
    }
}
