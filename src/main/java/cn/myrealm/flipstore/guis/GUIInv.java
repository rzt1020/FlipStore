package cn.myrealm.flipstore.guis;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.Set;

/**
* @program: FlipStore
* @description: Abstract class for inventory gui
* @author: rzt1020
* @create: 2022/11/06
**/
public abstract class GUIInv extends GUI {
    protected Inventory inv; // the minecraft inventory which used in this GUI
    /**
     * @Description: Constructor
     * @Param: [player]
     * @return:
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public GUIInv(Player owner) {
        super(owner);
    }

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
    @Override
    public void openGUI() {
        owner.openInventory(inv);
    }

    /**
     * @Description: get the gui's inventory
     * @Param: []
     * @return: org.bukkit.inventory.Inventory
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public Inventory getInv() {
        return inv;
    }

    
    /**
     * @Description: set all extra slots given itemStack
     * @Param: [itemStack]
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/8
    **/
    protected void setExtraSlots (@NonNull ItemStack itemStack) {
        if (Objects.nonNull(inv)) {
            for (int i = 0; i < inv.getSize(); i++) {
                if (Objects.isNull(inv.getItem(i)) || Objects.requireNonNull(inv.getItem(i)).getType().isAir()) {
                    inv.setItem(i, itemStack);
                }
            }
        }
    }
}
