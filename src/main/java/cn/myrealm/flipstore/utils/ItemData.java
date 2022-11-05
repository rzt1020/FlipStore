package cn.myrealm.flipstore.utils;

import org.bukkit.inventory.ItemStack;

/**
 * @program: FlipStore
 * @description: Item's data
 * @author: rzt1020
 * @create: 2022/11/06
 **/
public class ItemData {
    // vars
    private final ItemStack itemStack; // item
    private final double price, // price of an item
                         economic; // total economic sum caused by the item
    private final int times; // total number of times the item was acquired

    /**
     * @Description: Constructor
     * @Param: [itemStack, price, times, economic]
     * @return:
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public ItemData(ItemStack itemStack, double price, int times, double economic) {
        this.itemStack = itemStack;
        this.price = price;
        this.economic = economic;
        this.times = times;
    }
    
    /**
     * @Description: get times
     * @Param: []
     * @return: int
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public int getTimes() {
        return times;
    }
    
    /**
     * @Description: get price
     * @Param: []
     * @return: double
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public double getPrice() {
        return price;
    }
    
    /**
     * @Description: get price
     * @Param: []
     * @return: double
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public double getEconomic() {
        return economic;
    }
    
    /**
     * @Description: get itemStack
     * @Param: []
     * @return: org.bukkit.inventory.ItemStack
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public ItemStack getItemStack() {
        return itemStack;
    }
}
