package cn.myrealm.flipstore.listeners;

import cn.myrealm.flipstore.FlipStore;
import cn.myrealm.flipstore.guis.SetupGUI;
import cn.myrealm.flipstore.managers.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.Objects;

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
            LanguageManager.instance.sendMessage("setup-canceled", player);
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
            if (Objects.nonNull(e.getMainHandItem()) && !e.getMainHandItem().getType().isAir()) {
                SetupGUI setupGUI = new SetupGUI(player, e.getMainHandItem());
                Listener guiListener = new GUIListener(setupGUI);
                Bukkit.getPluginManager().registerEvents(guiListener, FlipStore.instance);
                setupGUI.openGUI();
            }
            e.setCancelled(true);
        }
    }
}
