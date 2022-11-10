package cn.myrealm.flipstore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


/**
 * @program: FlipStore
 * @description: Root command
 * @author: rzt1020
 * @create: 2022/10/14
 **/
public class CommandFlipstore extends CommandAbstract implements CommandExecutor {
    static {
        subCommands.put("reload", new CommandReload());
        subCommands.put("setup", new CommandSetup());
    }

    @Override
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
                executeCommand();
                return true;
            } else {
                if (subCommands.containsKey(args[0].toLowerCase())) {
                    return subCommands.get(args[0].toLowerCase()).onCommand(sender, command, label, argsUpdate(args));
                }
            }
        }
        return false;
    }
}
