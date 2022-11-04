package cn.myrealm.flipstore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: FlipShop
 * @description: Root command
 * @author: rzt1020
 * @create: 2022/10/14
 **/
public class CommandFlipstore implements CommandExecutor {
    private static final Map<String,CommandExecutor> sub_commands = new HashMap<>(); // command name, subcommand map
    static {
        sub_commands.put("reload",new CommandReload());
    }

    private void executeCommand() {

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

            return true;
        }
        return false;
    }
}
