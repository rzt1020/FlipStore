package cn.myrealm.flipstore.guis;

import cn.myrealm.flipstore.FlipStore;
import cn.myrealm.flipstore.managers.DatabaseManager;
import cn.myrealm.flipstore.managers.LanguageManager;
import cn.myrealm.flipstore.utils.ItemData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;


/**
 * @program: FlipStore
 * @description: Gui for setup an item
 * @author: rzt1020
 * @create: 2022/11/06
 **/
public class GUIInvSetup extends GUIInv {
    // vars
    private final ItemStack itemStack; // the itemStack be setup
    private final String hash;
    private ItemData itemData; // the data of this item
    /**
     * @Description: Constructor
     * @Param: [player]
     * @Author: rzt1020
     * @Date: 2022/11/6
     */
    public GUIInvSetup(Player owner, ItemStack itemStack) {
        super(owner);
        itemStack.setAmount(1);
        this.itemStack = itemStack;
        hash = FlipStore.toHash(this.itemStack.toString());
        constructGUI();
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
        this.itemData = DatabaseManager.instance.NBTInsert(hash);

        if (Objects.isNull(inv)) {
            inv = Bukkit.createInventory(owner, 18, LanguageManager.instance.getText("setup-title"));
        }
        inv.setItem(4,itemStack);
        ItemMeta itemMeta;
        List<String> lore;

        ItemStack hopper = new ItemStack(Material.HOPPER);
        itemMeta = hopper.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(LanguageManager.instance.getText("setup-price-down"));
        lore = new ArrayList<>(Arrays.asList(
                LanguageManager.instance.getVarText("setup-now-price", "price", String.format("%.2f",itemData.getPrice())),
                LanguageManager.instance.getVarText("setup-after-price", "price", String.format("%.2f",itemData.getPrice() * 0.8)),
                LanguageManager.instance.getText("setup-click")));
        itemMeta.setLore(lore);
        hopper.setItemMeta(itemMeta);
        inv.setItem(12, hopper);

        ItemStack sunflower = new ItemStack(Material.SUNFLOWER);
        itemMeta = sunflower.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(LanguageManager.instance.getText("setup-set-price"));
        lore = new ArrayList<>(Arrays.asList(
                LanguageManager.instance.getVarText("setup-now-price", "price", String.format("%.2f",itemData.getPrice())),
                LanguageManager.instance.getText("setup-click")));
        itemMeta.setLore(lore);
        sunflower.setItemMeta(itemMeta);
        inv.setItem(13, sunflower);

        ItemStack redstone = new ItemStack(Material.REDSTONE);
        itemMeta = redstone.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(LanguageManager.instance.getText("setup-price-up"));
        lore = new ArrayList<>(Arrays.asList(
                LanguageManager.instance.getVarText("setup-now-price", "price", String.format("%.2f",itemData.getPrice())),
                LanguageManager.instance.getVarText("setup-after-price", "price", String.format("%.2f",itemData.getPrice() * 1.2)),
                LanguageManager.instance.getText("setup-click")));
        itemMeta.setLore(lore);
        redstone.setItemMeta(itemMeta);
        inv.setItem(14, redstone);

        ItemStack wool;
        if (itemData.isAble()) wool = new ItemStack(Material.LIME_WOOL);
        else wool = new ItemStack(Material.RED_WOOL);
        itemMeta = wool.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(LanguageManager.instance.getText("setup-able-disable"));
        String state = itemData.isAble() ? LanguageManager.instance.getText("setup-able") : LanguageManager.instance.getText("setup-disable");
        lore = new ArrayList<>(Arrays.asList(
                LanguageManager.instance.getVarText("setup-now-state", "state", state),
                LanguageManager.instance.getText("setup-click")));
        itemMeta.setLore(lore);
        wool.setItemMeta(itemMeta);
        inv.setItem(17, wool);

        ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        itemMeta = glassPane.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(" ");
        glassPane.setItemMeta(itemMeta);
        setExtraSlots(glassPane);
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
        switch (slot) {
            case 12:
                DatabaseManager.instance.NBTPriceUpdate(hash, itemData.getPrice() * 0.8);
                break;
            case 13:
                GUISignPrice signPrice =new GUISignPrice(owner, itemData, this);
                signPrice.constructGUI();
                owner.closeInventory();
                signPrice.openGUI();
                break;
            case 14:
                DatabaseManager.instance.NBTPriceUpdate(hash, itemData.getPrice() * 1.2);
                break;
            case 17:
                DatabaseManager.instance.NBTAbleUpdate(hash);
                break;
        }
        this.itemData = DatabaseManager.instance.NBTInsert(FlipStore.toHash(this.itemStack.toString()));
        constructGUI();
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

    /**
     * @Description: handle the drag event
     * @Param: [slots]
     * @return: boolean
     * @Author: rzt1020
     * @Date: 2022/11/8
    **/
    @Override
    public boolean dragEventHandle(Set<Integer> slots) {
        return false;
    }
}
