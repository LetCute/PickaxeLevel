package net.letcute.pickaxeLevel.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;

public class Config {

    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration config;

    @Getter
    @Setter
    private int nextExpLevel;
    
    @Getter
    @Setter
    private String showInfo;

    @Getter
    @Setter
    private String levelUp;

    @Getter
    @Setter
    private String namePickaxe;

    @Getter
    @Setter
    private String lorePickaxe;

    @Getter
    @Setter
    private String addPickaxe;
    
    @Getter
    @Setter
    private int RatioEfficiency;

    @Getter
    @Setter
    private int RatioFortuneLevel;

    @Getter
    @Setter
    private int RatioUnbreaking;

    @Getter
    @Setter
    private int maxLevel;

    @Getter
    @Setter
    private String showMaxLevel;

    public Config(JavaPlugin plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
        loadConfig();
    }

    public void saveDefaultConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
    }

    public void loadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        setNextExpLevel(config.getInt("next-exp-level"));
        setShowInfo(format(config.getString("show-info")));
        setLevelUp(format(config.getString("level-up")));
        setNamePickaxe(format(config.getString("name-pickaxe")));
        setLorePickaxe(format(config.getString("lore-pickaxe")));
        setAddPickaxe(format(config.getString("add-pickaxe")));
        setRatioEfficiency(config.getInt("ratio-efficiency"));
        setRatioFortuneLevel(config.getInt("ratio-fortune"));
        setRatioUnbreaking(config.getInt("ratio-unbreaking"));
        setMaxLevel(config.getInt("max-level"));
        setShowMaxLevel(format(config.getString("show-max-level")));
    }

    public String getString(String key) {
        return config.getString(key);
    }

    public int getInt(String key) {
        return config.getInt(key);
    }

    public boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    public void reloadConfig() {
        loadConfig();
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String format(String text) {
        if (text != null) {
            return text.replace('&', '§');
        }
        return null;
    }
}
