package cn.myrealm.flipstore.guis;

import cn.myrealm.flipstore.managers.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @program: FlipStore
 * @description: Gui for setup a item
 * @author: rzt1020
 * @create: 2022/11/06
 **/
public class SetupGUI extends FlipGUI{
    // vars
    private final ItemStack itemStack;
    /**
     * @Description: Constructor
     * @Param: [player]
     * @Author: rzt1020
     * @Date: 2022/11/6
     */
    public SetupGUI(Player owner, ItemStack itemStack) {
        super(owner);
        this.itemStack = itemStack;
        inventory.setItem(4,itemStack);
    }

    /**
     * @Description: init the inventory
     * @Param: []
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/7
    **/
    @Override
    protected void constructGUI() {
        inventory = Bukkit.createInventory(null, 54, LanguageManager.instance.getText("setup-title"));
    }

    /**
     * @Description: handle the click event
     * @Param: [slot]
     * @return: boolean
     * @Author: rzt1020
     * @Date: 2022/11/7
    **/
    @Override
    public boolean clickEventHandle(int slot) {
        return true;
    }

    /**
     * @Description: handle the close event
     * @Param: []
     * @return: boolean
     * @Author: rzt1020
     * @Date: 2022/11/7
    **/
    @Override
    public boolean closeEventHandle() {
        LanguageManager.instance.sendMessage("setup-F-key",owner);
        LanguageManager.instance.sendMessage("setup-cancel",owner);
        return true;
    }
}
