package cn.myrealm.flipstore.listeners;

import cn.myrealm.flipstore.guis.FlipGUI;
import cn.myrealm.flipstore.managers.LanguageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.Objects;

/**
 * @program: FlipStore
 * @description: Listener for guis
 * @author: rzt1020
 * @create: 2022/11/06
 **/
public class GUIListener implements Listener {
    // vars
    private final Player player; // the player this gui open for
    private final FlipGUI gui; // the gui call this listener

    /**
     * @Description: Constructor
     * @Param: [player]
     * @return:
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public GUIListener(FlipGUI gui) {
        this.gui = gui;
        this.player = gui.getOwner();
    }

    
    /**
     * @Description: cancel the click event if handle returns true
     * @Param: [e]
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/7
    **/
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked().equals(player) && Objects.equals(e.getClickedInventory(), gui.getInventory())) {
            e.setCancelled(gui.clickEventHandle(e.getSlot()));
            if(e.getClick().toString().equals("SWAP_OFFHAND") && e.isCancelled()) {
                player.getInventory().setItemInOffHand(player.getInventory().getItemInOffHand());
            }
        }
    }
    
    /**
     * @Description: cancel the drag event
     * @Param: [e]
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/7
    **/
    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (e.getWhoClicked().equals(player)) {
            //e.setCancelled(true);
            LanguageManager.instance.sendMessage(player,e.getInventorySlots().toString());
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getPlayer().equals(player)) {
            if (gui.closeEventHandle()) {
                HandlerList.unregisterAll(this);
                player.updateInventory();
            } else {
                gui.openGUI();
            }
        }
    }
}
