package cn.myrealm.flipstore.guis;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
* @program: FlipStore
*
* @description: Abstract class for gui
*
* @author: rzt1020
*
* @create: 2022/11/06
**/
public abstract class FlipGUI {
    protected Player owner;
    protected Inventory inventory;
    /**
     * @Description: Constructor
     * @Param: [player]
     * @return:
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public FlipGUI(Player owner) {
        this.owner = owner;
        constructGUI();
    }

    /**
     * @Description: init the inventory in this method
     * @Param: []
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    protected abstract void constructGUI();

    /**
     * @Description: handle the click event in this method, return true to allow move the item
     * @Param: [slot]
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public abstract boolean clickEventHandle(int slot);
    
    /**
     * @Description: handle the close event in this method, return true to really close the gui
     * @Param: []
     * @return: boolean
     * @Author: rzt1020
     * @Date: 2022/11/7
    **/
    public abstract boolean closeEventHandle();
    
    /**
     * @Description: Open the inventory for player
     * @Param: []
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/7
    **/
    public void openGUI() {
        owner.openInventory(inventory);
    }

    /**
     * @Description: get the gui's inventory
     * @Param: []
     * @return: org.bukkit.inventory.Inventory
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * @Description: get the owner of this gui
     * @Param: []
     * @return: org.bukkit.entity.Player
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public Player getOwner() {
        return owner;
    }
}
