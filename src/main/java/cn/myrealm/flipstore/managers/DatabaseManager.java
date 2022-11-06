package cn.myrealm.flipstore.managers;

import cn.myrealm.flipstore.FlipStore;
import cn.myrealm.flipstore.utils.ItemData;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
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
    private Connection sqlite, // sqlite connection
                       mysql; // mysql connection
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
        shutdown();
        Statement stmt;
        if (useMySql) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                ConfigurationSection section = FlipStore.instance.getConfig().getConfigurationSection("mysql");
                if (Objects.isNull(section)) {
                    throw new RuntimeException("mysql setting is missing");
                }
                mysql = DriverManager.getConnection("jdbc:mysql://" +
                                                        section.getString("host", "localhost") +
                                                        ":" + section.getInt("port", 3306) +
                                                        "/" + section.getString("database", "data") +
                                                        "?" + "useSSL=" + section.getBoolean("use-ssl", false) +
                                                        "&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8",
                                                        section.getString("user", "root"),
                                                        section.getString("pass", "root"));
                stmt = mysql.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS FS_VANILLA " +
                             "(MATERIAL VARCHAR(50) PRIMARY KEY     NOT NULL," +
                             " PRICE    REAL, " +
                             " TIMES    INTEGER, " +
                             " ECONOMIC REAL);";
                stmt.executeUpdate(sql);
                stmt.close();
            } catch (Exception e) {
                LanguageManager.instance.severe(e);
            }
            LanguageManager.instance.log(LanguageManager.instance.getText("database-connect-successful"));
        } else {
            try {
                Class.forName("org.sqlite.JDBC");
                sqlite = DriverManager.getConnection("jdbc:sqlite:"+FlipStore.instance.getDataFolder()+"/data.db");
                stmt = sqlite.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS FS_VANILLA " +
                             "(MATERIAL TEXT PRIMARY KEY     NOT NULL," +
                             " PRICE    REAL, " +
                             " TIMES    INT, " +
                             " ECONOMIC REAL);";
                stmt.executeUpdate(sql);
                stmt.close();
            } catch ( Exception e ) {
                LanguageManager.instance.severe(e);
            }
            LanguageManager.instance.log(LanguageManager.instance.getText("database-connect-successful"));
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
            try {
                if (Objects.nonNull(mysql)) {
                    mysql.close();
                }
            } catch (Exception e) {
                LanguageManager.instance.severe(e);
            }
        } else {
            try {
                if (Objects.nonNull(sqlite)) {
                    sqlite.close();
                }
            } catch (Exception e) {
                LanguageManager.instance.severe(e);
            }
        }
    }
    
    /**
     * @Description: insert a vanilla item record, returns true, if successful
     * @Param: [material, cmd]
     * @return: boolean
     * @Author: rzt1020
     * @Date: 2022/11/5
    **/
    public boolean vanillaInsert(String material, int cmd) {
        ItemData itemData = vanillaSelect(material, cmd);
        if (Objects.nonNull(itemData)) {
            return false;
        }
        String combineMaterial = material.toUpperCase() + ":" + cmd; // combine material and cmd
        Statement stmt;
        if (useMySql) {
            try {
                stmt = mysql.createStatement();
                String sql = "INSERT INTO FS_VANILLA "+
                             "(MATERIAL,PRICE,TIMES,ECONOMIC) " +
                             "VALUES (" + "'" + combineMaterial + "'" + ",0,0,0);";
                stmt.executeUpdate(sql);
                stmt.close();
            } catch ( Exception e ) {
                LanguageManager.instance.severe(e);
            }
        } else {
            try {
                stmt = sqlite.createStatement();
                String sql = "INSERT INTO FS_VANILLA "+
                             "(MATERIAL,PRICE,TIMES,ECONOMIC) " +
                             "VALUES (" + "'" + combineMaterial + "'" + ",0,0,0);";
                stmt.executeUpdate(sql);
                stmt.close();
            } catch ( Exception e ) {
                LanguageManager.instance.severe(e);
            }
        }
        return true;
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
        String combineMaterial = material.toUpperCase() + ":" + cmd; // combine material and cmd
        Statement stmt;
        if(useMySql) {
            ResultSet rs;
            try {
                stmt = mysql.createStatement();
                String sql = "SELECT * " +
                             "FROM FS_VANILLA " +
                             "WHERE MATERIAL = " + "'" + combineMaterial + "';";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    ItemStack itemStack = new ItemStack(Objects.requireNonNull(Material.getMaterial(material)));
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    assert itemMeta != null;
                    itemMeta.setCustomModelData(cmd);
                    itemStack.setItemMeta(itemMeta);
                    itemData = new ItemData(itemStack,
                                            rs.getDouble("PRICE"),
                                            rs.getInt("TIMES"),
                                            rs.getDouble("ECONOMIC"));

                }
                rs.close();
                stmt.close();
            } catch (Exception e) {
                LanguageManager.instance.severe(e);
            }
        } else {
            ResultSet rs;
            try {
                stmt = sqlite.createStatement();
                String sql = "SELECT * " +
                             "FROM FS_VANILLA " +
                             "WHERE MATERIAL = " + "'" + combineMaterial + "';";
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
                LanguageManager.instance.severe(e);
            }
        }
        return itemData;
    }

    /**
     * @Description: update a vanilla item's price by material name, returns true, if successful
     * @Param: [material, cmd, price]
     * @return: boolean
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public boolean vanillaPriceUpdate(String material, int cmd, double price) {
        String combineMaterial = material.toUpperCase() + ":" + cmd; // combine material and cmd
        ItemData itemData = vanillaSelect(material, cmd);
        if (Objects.isNull((itemData))) {
            return false;
        }
        Statement stmt;
        if (useMySql) {
            try {
                stmt = mysql.createStatement();
                String sql = "UPDATE FS_VANILLA " +
                             "SET PRICE = " + price + " " +
                             "WHERE MATERIAL = '" + combineMaterial +"';";
                stmt.executeUpdate(sql);
            } catch ( Exception e ) {
                LanguageManager.instance.severe(e);
            }
        } else {
            try {
                stmt = sqlite.createStatement();
                String sql = "UPDATE FS_VANILLA " +
                             "SET PRICE = " + price + " " +
                             "WHERE MATERIAL = '" + combineMaterial +"';";
                stmt.executeUpdate(sql);
            } catch ( Exception e ) {
                LanguageManager.instance.severe(e);
            }
        }
        return true;
    }
    
    /**
     * @Description: update a vanilla item's record by material name, returns true, if successful
     * @Param: [material, cmd, thisTimes]
     * @return: boolean
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public boolean vanillaRecordUpdate(String material, int cmd, int thisTimes) {
        String combineMaterial = material.toUpperCase() + ":" + cmd; // combine material and cmd
        ItemData itemData = vanillaSelect(material, cmd);
        if (Objects.isNull((itemData))) {
            return false;
        }
        int times = itemData.getTimes() + thisTimes; // count times
        double economic = itemData.getEconomic() + itemData.getPrice() * thisTimes; // count economic
        Statement stmt;
        if (useMySql) {
            try {
                stmt = mysql.createStatement();
                String sql = "UPDATE FS_VANILLA " +
                             "SET TIMES = " + times + ", " +
                             "ECONOMIC = " + economic + " " +
                             "WHERE MATERIAL = '" + combineMaterial +"';";
                stmt.executeUpdate(sql);
            } catch ( Exception e ) {
                LanguageManager.instance.severe(e);
            }
        } else {
            try {
                stmt = sqlite.createStatement();
                String sql = "UPDATE FS_VANILLA " +
                             "SET TIMES = " + times + ", " +
                             "ECONOMIC = " + economic + " " +
                             "WHERE MATERIAL = '" + combineMaterial +"';";
                stmt.executeUpdate(sql);
            } catch ( Exception e ) {
                LanguageManager.instance.severe(e);
            }
        }
        return true;
    }
}
