package cn.myrealm.flipstore.listeners;

import cn.myrealm.flipstore.guis.FlipGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

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
    public GUIListener(Player player, FlipGUI gui) {
        this.player = player;
        this.gui = gui;
    }


    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked().equals(player)) {
            e.setCancelled(true);
        }
        if(e.getClick().toString().equals("SWAP_OFFHAND") && e.isCancelled()) {
            player.getInventory().setItemInOffHand(player.getInventory().getItemInOffHand());
        }
        if (e.getInventory().equals(gui.getInventory())) {
            gui.clickEventHandle(e.getSlot());
        }
    }
    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (e.getWhoClicked().equals(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getPlayer().equals(player)) {
            if (gui.closeEventHandle()) {
                HandlerList.unregisterAll(this);
                player.updateInventory();
            } else {

            }
        }
    }
}
