package cn.myrealm.flipstore.managers;

import cn.myrealm.flipstore.FlipStore;
import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.util.Map;
import java.util.Objects;

/**
 * @program: FlipStore
 * @description: Managing Language Information
 * @author: rzt1020
 * @create: 2022/09/28
 **/
public class LanguageManager extends Manager{
    // vars
    public static LanguageManager instance; // language manager instance
    private final YamlConfiguration langYml; // the loaded language file <Yaml>
    
    /**
     * @Description: Constructor
     * @Param: []
     * @return: 
     * @Author: rzt1020
     * @Date: 2022/9/28
    **/
    public LanguageManager() {
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
        if (langYml.contains(key)) {
            String text = langYml.getString(key,"");
            return FlipStore.parseColor(text);
        }
        throw new IllegalArgumentException("No such language key");
    }
    
    /**
     * @Description: Adding variables to text
     * @Param: [key, varMap]
     * @return: java.lang.String
     * @Author: rzt1020
     * @Date: 2022/9/28
    **/
    public String getVarText(@NonNull String key, Map<String,String> var_map) {
        String text = getText(key);
        if (Objects.isNull(var_map)) {
            return text;
        }
        for (String var : var_map.keySet()) {
            if (Objects.nonNull(var_map.get(var))) {
                text = text.replace("%"+var+"%",var_map.get(var));
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
}
