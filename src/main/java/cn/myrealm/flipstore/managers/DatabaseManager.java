package cn.myrealm.flipstore.managers;

import cn.myrealm.flipstore.FlipStore;
import cn.myrealm.flipstore.utils.ItemData;
import org.bukkit.configuration.ConfigurationSection;

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
                String sql = "CREATE TABLE IF NOT EXISTS FS_NBT " +
                             "(HASH VARCHAR(16) PRIMARY KEY     NOT NULL," +
                             " PRICE    REAL, " +
                             " TIMES    INTEGER, " +
                             " ECONOMIC REAL, " +
                             " ABLE     TINYINT);";
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
                String sql = "CREATE TABLE IF NOT EXISTS FS_NBT " +
                             "(HASH TEXT PRIMARY KEY     NOT NULL," +
                             " PRICE    REAL, " +
                             " TIMES    INT, " +
                             " ECONOMIC REAL, " +
                             " ABLE     INT);";
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
     * @Description: insert an item record, returns item data, if successful
     * @Param: [hash]
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/5
    **/
    public ItemData NBTInsert(String hash) {
        ItemData itemData = NBTSelect(hash);
        if (Objects.nonNull(itemData)) {
            return itemData;
        }
        Statement stmt;
        if (useMySql) {
            try {
                stmt = mysql.createStatement();
                String sql = "INSERT INTO FS_NBT "+
                             "(HASH,PRICE,TIMES,ECONOMIC,ABLE) " +
                             "VALUES (" + "'" + hash + "'" + "," +
                             FlipStore.instance.getConfig().getDouble("default-price", 0.01) + ",0,0,1);";
                stmt.executeUpdate(sql);
                stmt.close();
            } catch ( Exception e ) {
                LanguageManager.instance.severe(e);
            }
        } else {
            try {
                stmt = sqlite.createStatement();
                String sql = "INSERT INTO FS_NBT "+
                             "(HASH,PRICE,TIMES,ECONOMIC,ABLE) " +
                             "VALUES (" + "'" + hash + "'" + "," +
                             FlipStore.instance.getConfig().getDouble("default-price", 0.01) + ",0,0,1);";
                stmt.executeUpdate(sql);
                stmt.close();
            } catch ( Exception e ) {
                LanguageManager.instance.severe(e);
            }
        }
        return NBTSelect(hash);
    }

    /**
     * @Description: select an item by material name
     * @Param: [hash]
     * @return: cn.myrealm.flipstore.utils.ItemData
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public ItemData NBTSelect(String hash) {
        ItemData itemData = null; // item data
        Statement stmt;
        if(useMySql) {
            ResultSet rs;
            try {
                stmt = mysql.createStatement();
                String sql = "SELECT * " +
                             "FROM FS_NBT " +
                             "WHERE HASH = " + "'" + hash + "';";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    itemData = new ItemData(rs.getDouble("PRICE"),
                                            rs.getInt("TIMES"),
                                            rs.getDouble("ECONOMIC"),
                                            rs.getBoolean("ABLE"));
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
                             "FROM FS_NBT " +
                             "WHERE HASH = " + "'" + hash + "';";
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    itemData = new ItemData(rs.getDouble("PRICE"),
                                            rs.getInt("TIMES"),
                                            rs.getDouble("ECONOMIC"),
                                            rs.getBoolean("ABLE"));
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
     * @Description: update an item's price by material name
     * @Param: [hash, price]
     * @return: boolean
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public void NBTPriceUpdate(String hash, double price) {
        ItemData itemData = NBTSelect(hash);
        if (Objects.isNull((itemData))) {
            return;
        }
        Statement stmt;
        if (useMySql) {
            try {
                stmt = mysql.createStatement();
                String sql = "UPDATE FS_NBT " +
                             "SET PRICE = " + price + " " +
                             "WHERE HASH = '" + hash +"';";
                stmt.executeUpdate(sql);
            } catch ( Exception e ) {
                LanguageManager.instance.severe(e);
            }
        } else {
            try {
                stmt = sqlite.createStatement();
                String sql = "UPDATE FS_NBT " +
                             "SET PRICE = " + price + " " +
                             "WHERE HASH = '" + hash +"';";
                stmt.executeUpdate(sql);
            } catch ( Exception e ) {
                LanguageManager.instance.severe(e);
            }
        }
    }
    
    /**
     * @Description: update an item's record by material name, returns true, if successful
     * @Param: [hash, thisTimes]
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public boolean NBTRecordUpdate(String hash, int thisTimes) {
        ItemData itemData = NBTSelect(hash);
        if (Objects.isNull((itemData))) {
            return false;
        }
        int times = itemData.getTimes() + thisTimes; // count times
        double economic = itemData.getEconomic() + itemData.getPrice() * thisTimes; // count economic
        Statement stmt;
        if (useMySql) {
            try {
                stmt = mysql.createStatement();
                String sql = "UPDATE FS_NBT " +
                             "SET TIMES = " + times + ", " +
                             "ECONOMIC = " + economic + " " +
                             "WHERE HASH = '" + hash +"';";
                stmt.executeUpdate(sql);
            } catch ( Exception e ) {
                LanguageManager.instance.severe(e);
            }
        } else {
            try {
                stmt = sqlite.createStatement();
                String sql = "UPDATE FS_NBT " +
                             "SET TIMES = " + times + ", " +
                             "ECONOMIC = " + economic + " " +
                             "WHERE HASH = '" + hash +"';";
                stmt.executeUpdate(sql);
            } catch ( Exception e ) {
                LanguageManager.instance.severe(e);
            }
        }
        return true;
    }
    
    /**
     * @Description: if the original is able, set to disable; if the original is disabled, set to be able
     * @Param: [hash]
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/8
    **/
    public void NBTAbleUpdate(String hash) {
        ItemData itemData = NBTSelect(hash);
        if (Objects.isNull((itemData))) {
            return;
        }
        
        Statement stmt;
        if (useMySql) {
            try {
                stmt = mysql.createStatement();
                String sql = "UPDATE FS_NBT " +
                        "SET ABLE = " + (itemData.isAble() ? 0 : 1) + " " +
                        "WHERE HASH = '" + hash +"';";
                stmt.executeUpdate(sql);
            } catch ( Exception e ) {
                LanguageManager.instance.severe(e);
            }
        } else {
            try {
                stmt = sqlite.createStatement();
                String sql = "UPDATE FS_NBT " +
                        "SET ABLE = " + (itemData.isAble() ? 0 : 1) + " " +
                        "WHERE HASH = '" + hash +"';";
                stmt.executeUpdate(sql);
            } catch ( Exception e ) {
                LanguageManager.instance.severe(e);
            }
        }
    }

}
