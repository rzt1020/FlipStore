package cn.myrealm.flipstore.managers;

import cn.myrealm.flipstore.FlipStore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.util.Map;
import java.util.Objects;

/**
 * @program: FlipStore
 * @description: Managing language information
 * @author: rzt1020
 * @create: 2022/09/28
 **/
public class LanguageManager implements Manager{
    // vars
    public static LanguageManager instance; // language manager instance
    private YamlConfiguration langYml; // the loaded language file <Yaml>
    
    /**
     * @Description: Constructor
     * @Param: []
     * @return: 
     * @Author: rzt1020
     * @Date: 2022/9/28
    **/
    public LanguageManager() {
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
        String lang = FlipStore.instance.getConfig().getString("language", "english");
        File file = new File(FlipStore.instance.getDataFolder(), "language/" + lang + ".yml");
        langYml = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * @Description: Get text from language file
     * @Param: [key]
     * @return: java.lang.String
     * @Author: rzt1020
     * @Date: 2022/9/28
    **/
    public String getText(@NonNull String key) {
        String text = ""; // result of the key
        try {
            if (langYml.contains(key)) {
                text = langYml.getString(key,"");
                text = FlipStore.parseColor(text);
            } else {
                throw new IllegalArgumentException("No such language key: " + key);
            }
        } catch (Exception e) {
            severe(e);

        }
        return text;
    }
    
    /**
     * @Description: Adding variables to text
     * @Param: [key, varMap]
     * @return: java.lang.String
     * @Author: rzt1020
     * @Date: 2022/9/28
    **/
    public String getVarText(@NonNull String key, Map<String,String> varMap) {
        String text = getText(key);
        if (Objects.isNull(varMap)) {
            return text;
        }
        for (String var : varMap.keySet()) {
            if (Objects.nonNull(varMap.get(var))) {
                text = text.replace("%"+var+"%",varMap.get(var));
            }
        }
        return text;
    }
    
    /**
     * @Description: Adding variables to text
     * @Param: [key, var, content]
     * @return: java.lang.String
     * @Author: rzt1020
     * @Date: 2022/9/29
    **/
    public String getVarText(@NonNull String key, String var, String content) {
        String text = getText(key);
        if (Objects.isNull(var) || Objects.isNull(content)) {
            return text;
        }
        text = text.replace("%"+var+"%",content);
        return text;
    }
    
    /**
     * @Description: Output the error report to the server console
     * @Param: [e]
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public void severe(@NonNull Exception e) {
        FlipStore.instance.getLogger().severe( e.getClass().getName() + ": " + e.getMessage() );
    }
    
    /**
     * @Description: Log to the server console
     * @Param: [msg]
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public void log(@NonNull String msg) {
        FlipStore.instance.getLogger().info(msg);
    }
    
    /**
     * @Description: send message to a player synchronized
     * @Param: [player, msg]
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public void sendMessage(@NonNull Player player, @NonNull String msg) {
        Bukkit.getScheduler().runTask(FlipStore.instance, () -> player.sendMessage(msg));
    }

    /**
     * @Description: send message by key, to a playee synchronized
     * @Param: [key, player]
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/6
    **/
    public void sendMessage(@NonNull String key, @NonNull Player player) {
        sendMessage(player, getText(key));
    }
}
