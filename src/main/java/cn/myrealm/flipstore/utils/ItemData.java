package cn.myrealm.flipstore.utils;

/**
 * @program: FlipStore
 * @description: Item's data
 * @author: rzt1020
 * @create: 2022/11/06
 **/
public class ItemData {
    // vars
    private final double price, // price of an item
                         economic; // total economic sum caused by the item
    private final int times; // total number of times the item was acquired
    private final boolean able; // the item can be acquired

    /**
     * @Description: Constructor
     * @Param: [price, times, economic, able]
     * @return:
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public ItemData(double price, int times, double economic, boolean able) {
        this.price = price;
        this.economic = economic;
        this.times = times;
        this.able = able;
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
     * @Description: is able
     * @Param: []
     * @return: boolean
     * @Author: rzt1020
     * @Date: 2022/11/8
    **/
    public boolean isAble() {
        return able;
    }
    
}
