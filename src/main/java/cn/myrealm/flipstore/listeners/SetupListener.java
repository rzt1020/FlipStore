package cn.myrealm.flipstore.listeners;

import cn.myrealm.flipstore.managers.LanguageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

/**
 * @program: FlipStore
 * @description: Listener for all setup actions
 * @author: rzt1020
 * @create: 2022/11/06
 **/
public class SetupListener implements Listener {
    // vars
    private final Player player; // player who be listened

    /**
     * @Description: Constructor
     * @Param: [player]
     * @return:
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public SetupListener(Player player) {
        this.player = player;
    }

    /**
     * @Description: Cancel this listener when player send "cancel"
     * @Param: [e]
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    @EventHandler
    public void onCancelSetup (AsyncPlayerChatEvent e) {
        if (e.getPlayer().equals(player) && e.getMessage().equals("cancel")) {
            LanguageManager.instance.sendMessage("setup-cancel", player);
            e.setCancelled(true);
            HandlerList.unregisterAll(this);
        }
    }

    /**
     * @Description: Call when press F
     * @Param: [e]
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    @EventHandler
    public void onPress_F_Key (PlayerSwapHandItemsEvent e) {
        if (e.getPlayer().equals(player)) {
            //LanguageManager.instance.sendMessage(player, String.valueOf(e.getOffHandItem()));
        }
    }
}
