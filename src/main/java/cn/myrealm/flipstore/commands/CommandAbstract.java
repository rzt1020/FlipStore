package cn.myrealm.flipstore.commands;

import org.bukkit.command.CommandExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: FlipStore
 * @description: abstract class for commands
 * @author: rzt1020
 * @create: 2022/11/06
 **/
public abstract class CommandAbstract {
    // vars
    protected static final Map<String, CommandExecutor> subCommands = new HashMap<>(); // command name, subcommand map

    /**
     * @Description: execute the command
     * @Param: []
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public abstract void executeCommand();
    
    /**
     * @Description: Removing the first element
     * @Param: [args]
     * @return: java.lang.String[]
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
  protected String[] argsUpdate (String[] args) {
        if (args.length == 0) {
            return args;
        }
        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, args.length - 1);
        return newArgs;
    }
}
