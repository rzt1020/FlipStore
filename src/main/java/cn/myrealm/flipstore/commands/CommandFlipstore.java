package cn.myrealm.flipstore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: FlipStore
 * @description: Root command
 * @author: rzt1020
 * @create: 2022/10/14
 **/
public class CommandFlipstore implements CommandExecutor {
    private static final Map<String,CommandExecutor> subCommands = new HashMap<>(); // command name, subcommand map
    static {
        subCommands.put("reload",new CommandReload());
    }

    public void executeCommand() {

    }

    /**
     * @Description: Call when root command is sent
     * @Param: [sender, command, label, args]
     * @return: boolean
     * @Author: rzt1020
     * @Date: 2022/10/14
    **/
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("flipstore")) {
            if (args.length == 0) {
                return true;
            } else {
                if (subCommands.containsKey(args[0].toLowerCase())) {
                    return subCommands.get(args[0].toLowerCase()).onCommand(sender, command, label, args);
                }
            }
        }
        return false;
    }
}
