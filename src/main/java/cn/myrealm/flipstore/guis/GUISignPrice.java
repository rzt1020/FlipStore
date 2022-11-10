package cn.myrealm.flipstore.guis;

import cn.myrealm.flipstore.FlipStore;
import cn.myrealm.flipstore.listeners.GUIListener;
import cn.myrealm.flipstore.managers.DatabaseManager;
import cn.myrealm.flipstore.managers.LanguageManager;
import cn.myrealm.flipstore.utils.ItemData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * @program: FlipStore
 * @description: Gui for set an item's price
 * @author: rzt1020
 * @create: 2022/11/08
 **/
public class GUISignPrice extends GUISign{
    // vars
    private final ItemData itemData;
    private final GUIInvSetup setupGUI;

    /**
     * @Description: Constructor
     * @Param: [owner]
     * @return:
     * @Author: rzt1020
     * @Date: 2022/11/8
     **/
    public GUISignPrice(Player owner, ItemData itemData, GUIInvSetup setupGUI) {
        super(owner);
        this.itemData =  itemData;
        this.setupGUI = setupGUI;
    }
    
    /**
     * @Description: construct the text of sign
     * @Param: []
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/10
    **/
    @Override
    protected void constructGUI() {
        text = new ArrayList<>(Arrays.asList("" ,
                LanguageManager.instance.getVarText("setup-now-price", "price", String.format("%.2f", itemData.getPrice())),
                LanguageManager.instance.getText("price-intro-1"),
                LanguageManager.instance.getText("price-intro-2")));
    }
    
    /**
     * @Description: Read the 3rd row of sign texts and write it to the database
     * @Param: []
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/10
    **/
    @Override
    protected void doneHandle() {
        double price;
        try {
            price = Double.parseDouble(text.get(0));
        } catch (Exception e) {
            openSetupGUI();
            return;
        }
        DatabaseManager.instance.NBTPriceUpdate(itemData.getHash(),price);
        openSetupGUI();
    }

    private void openSetupGUI() {
        Bukkit.getScheduler().runTaskLater(FlipStore.instance, ()->{
            setupGUI.constructGUI();
            setupGUI.openGUI();
            Listener guiListener = new GUIListener(setupGUI);
            Bukkit.getPluginManager().registerEvents(guiListener, FlipStore.instance);
        },3L);
    }
}
