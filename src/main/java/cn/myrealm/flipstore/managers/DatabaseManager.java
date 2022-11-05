package cn.myrealm.flipstore.managers;

import cn.myrealm.flipstore.FlipStore;
import cn.myrealm.flipstore.utils.ItemData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.*;
import java.util.Objects;

/**
 * @program: FlipStore
 * @description: Managing database
 * @author: rzt1020
 * @create: 2022/11/05
 **/
public class DatabaseManager implements Manager{
    // vars
    public static DatabaseManager instance; // database manager instance
    private boolean useMySql; // is use mysql
    private Connection sqlite; // sqlite connection
    /**
     * @Description: Constructor
     * @Param: []
     * @return:
     * @Author: rzt1020
     * @Date: 2022/11/5
    **/
    public DatabaseManager() {
        reload();
    }

    /**
     * @Description: init and reload the manager
     * @Param: []
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/5
    **/
    @Override
    public void reload() {
        instance = this;
        useMySql = FlipStore.instance.getConfig().getBoolean("use-mysql", false);
        if (useMySql) {

        } else {
            Statement stmt;
            try {
                Class.forName("org.sqlite.JDBC");
                sqlite = DriverManager.getConnection("jdbc:sqlite:"+FlipStore.instance.getDataFolder()+"/data.db");
                stmt = sqlite.createStatement();
                String sql = "CREATE TABLE FS_VANILLA " +
                             "(MATERIAL TEXT PRIMARY KEY     NOT NULL," +
                             " PRICE    REAL, " +
                             " TIMES    INT, " +
                             " ECONOMIC REAL)";
                stmt.executeUpdate(sql);
                stmt.close();
            } catch ( Exception e ) {
                FlipStore.instance.getLogger().severe(e.getClass().getName() + ": " + e.getMessage() );
            }
            FlipStore.instance.getLogger().info(LanguageManager.instance.getText("database-connect-successful"));
        }
    }
    
    /**
     * @Description: shutdown the connection
     * @Param: []
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/5
    **/
    public void shutdown() {
        if (useMySql) {

        } else {
            try {
                sqlite.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    /**
     * @Description: insert a vanilla item record
     * @Param: [material, cmd]
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/5
    **/
    public void vanillaInsert(String material, int cmd) {
        if (Objects.nonNull(vanillaSelect(material, cmd))) {
            return;
        }
        material = material + ":" + cmd; // combine material and cmd
        if (useMySql) {

        } else {
            Statement stmt;
            try {
                stmt = sqlite.createStatement();
                String sql = "INSERT INTO FS_VANILLA "+
                             "(MATERIAL,PRICE,TIMES,ECONOMIC) " +
                             "VALUES (" + "'" + material + "'" + ",0,0,0);";
                stmt.executeUpdate(sql);
                stmt.close();
            } catch ( Exception e ) {
                FlipStore.instance.getLogger().severe(e.getClass().getName() + ": " + e.getMessage() );
            }
        }
    }

    /**
     * @Description: select a vanilla item by material name
     * @Param: [material, cmd]
     * @return: cn.myrealm.flipstore.utils.ItemData
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public ItemData vanillaSelect(String material, int cmd) {
        ItemData itemData = null; // item data
        material = material + ":" + cmd; // combine material and cmd
        if(useMySql) {

        } else {
            Statement stmt;
            ResultSet rs;
            try {
                stmt = sqlite.createStatement();
                String sql = "SELECT * " +
                             "FROM FS_VANILLA " +
                             "WHERE MATERIAL = " + "'" + material + "';";
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    ItemStack itemStack = new ItemStack(Objects.requireNonNull(Material.getMaterial(material)));
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemStack.setItemMeta(itemMeta);
                    itemData = new ItemData(itemStack,
                                            rs.getDouble("PRICE"),
                                            rs.getInt("TIMES"),
                                            rs.getDouble("ECONOMIC"));
                }
                rs.close();
                stmt.close();
            } catch (Exception e) {
                FlipStore.instance.getLogger().severe(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        return itemData;
    }

    /**
     * @Description: update a vanilla item's price by material name
     * @Param: [material, cmd, price]
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public void vanillaPriceUpdate(String material, int cmd, double price) {
        material = material + ":" + cmd; // combine material and cmd
        if (useMySql) {

        } else {
            Statement stmt;
            try {
                stmt = sqlite.createStatement();
                String sql = "UPDATE FS_VANILLA " +
                             "SET PRICE = " + price + " " +
                             "WHERE MATERIAL = '" + material +"';";
                stmt.executeUpdate(sql);
            } catch ( Exception e ) {
                FlipStore.instance.getLogger().severe( e.getClass().getName() + ": " + e.getMessage() );
            }
        }
    }
    
    /**
     * @Description: update a vanilla item's record by material name
     * @Param: [material, cmd, thisTimes]
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public void vanillaRecordUpdate(String material, int cmd, int thisTimes) {
        material = material + ":" + cmd; // combine material and cmd
        if (useMySql) {

        } else {
            ItemData itemData = vanillaSelect(material, cmd);
            if (Objects.nonNull(itemData)) {
                int times = itemData.getTimes() + thisTimes; // count times
                double economic = itemData.getEconomic() + itemData.getPrice() * thisTimes; // count economic
                Statement stmt;
                try {
                    stmt = sqlite.createStatement();
                    String sql = "UPDATE FS_VANILLA " +
                                 "SET TIMES = " + times + " " +
                                 "ECONOMIC" + economic + " " +
                                 "WHERE MATERIAL = '" + material +"';";
                    stmt.executeUpdate(sql);
                } catch ( Exception e ) {
                    FlipStore.instance.getLogger().severe( e.getClass().getName() + ": " + e.getMessage() );
                }
            }
        }
    }
}
