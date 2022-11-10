package cn.myrealm.flipstore.guis;

import org.bukkit.entity.Player;

/**
 * @program: FlipStore
 * @description: Abstract class for GUI
 * @author: rzt1020
 * @create: 2022/11/08
 **/
public abstract class GUI {
    protected Player owner; // the owner who requests this GUI

    /**
     * @Description: Constructor
     * @Param: [player]
     * @return:
     * @Author: rzt1020
     * @Date: 2022/11/6
     **/
    public GUI(Player owner) {
        this.owner = owner;
    }

    /**
     * @Description: init the GUI in this method
     * @Param: []
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/6
     **/
    protected abstract void constructGUI();

    /**
     * @Description: Open the GUI for player
     * @Param: []
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/7
     **/
    public abstract void openGUI();

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
