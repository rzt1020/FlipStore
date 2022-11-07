package cn.myrealm.flipstore.guis;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
* @program: FlipStore
*
* @description: Abstract class for gui
*
* @author: rzt1020
*
* @create: 2022/11/06
**/
public abstract class InventoryGUI {
    protected Player owner; // the owner who requests this GUI
    protected Inventory inventory; // the minecraft inventory which used in this GUI
    /**
     * @Description: Constructor
     * @Param: [player]
     * @return:
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public InventoryGUI(Player owner) {
        this.owner = owner;
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
     * @Description: handle the drag event in this method, return true to allow drag
     * @Param: [slots]
     * @return: boolean
     * @Author: rzt1020
     * @Date: 2022/11/8
    **/
    public abstract boolean dragEventHandle(Set<Integer> slots);
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

    /**
     * @Description: get the slots that can be drag or can enter items
     * @Param: []
     * @return: java.util.Set<java.lang.Integer>
     * @Author: rzt1020
     * @Date: 2022/11/8
    **/
    public Set<Integer> getMovableSlots() {
        return new HashSet<>();
    }
    
    /**
     * @Description: set all extra slots given itemStack
     * @Param: [itemStack]
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/8
    **/
    protected void setExtraSlots (@NonNull ItemStack itemStack) {
        if (Objects.nonNull(inventory)) {
            for (int i = 0; i < inventory.getSize(); i++) {
                if (Objects.isNull(inventory.getItem(i)) || Objects.requireNonNull(inventory.getItem(i)).getType().isAir()) {
                    inventory.setItem(i, itemStack);
                }
            }
        }
    }
}
