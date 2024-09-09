package net.letcute.pickaxeLevel;

import net.letcute.pickaxeLevel.cache.Cache;
import net.letcute.pickaxeLevel.command.PickaxeLevelCommand;
import net.letcute.pickaxeLevel.config.Config;
import net.letcute.pickaxeLevel.database.DataBase;
import net.letcute.pickaxeLevel.database.entity.PickaxeLevelEntity;
import net.letcute.pickaxeLevel.placeholder.TopPickaxeLevel;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import lombok.Getter;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Logger;

public final class PickaxeLevel extends JavaPlugin {

    private static DataBase database;
    @Getter
    private final Logger log = Logger.getLogger("PickaxeLevel");
    private SimpleCommandMap commandMap;
    @Getter
    private static PickaxeLevel instance;
    @Getter
    private Cache cache;
    @Getter
    private Config data;

    @Override
    public void onEnable() {
        instance = this;
        cache = new Cache();
        loadConfig();
        createPluginFolder();
        initializeDatabase();
        registerCommand();
        registerEventListeners();
        hook();
        taskTop();
        getLog().info(ChatColor.GREEN + "Plugin PickaxeLevel By LetCute đã Bật");
    }

    @Override
    public void onDisable() {
        if (database != null) {
            database.closeConnection();
        }

        getLog().info("Plugin PickaxeLevel By LetCute đã tắt");
    }

    public void registerCommand(){
        String command  = getData().getString("command");
        String description = getData().getString("command-description");
        registerCommand(new PickaxeLevelCommand(command), description);
    }

    public void hook() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new TopPickaxeLevel(this).register();
        }
    }

    private void taskTop() {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<PickaxeLevelEntity> listTop = getDatabase().getPickaxeLevelDB().getTop10PlayersByLevel();
                getCache().getPickaxeLevelCache().setPickaxeLevelEntityCacheTop10(listTop);
            }
        }.runTaskTimer(this, 0L, 6000L);
    }

    private void loadConfig() {
        this.data = new Config(this);
    }

    private void createPluginFolder() {
        File pluginFolder = getDataFolder();

        if (!pluginFolder.exists()) {
            boolean created = pluginFolder.mkdirs();

            if (created) {
                getLogger().info("Plugin folder created successfully.");
            } else {
                getLogger().warning("Failed to create plugin folder.");
            }
        } else {
            getLogger().info("Plugin folder already exists.");
        }
    }

    private void initializeDatabase() {
        database = new DataBase(getDataFolder(), "pickaxeLevel", getLog());
        database.openConnection();
        database.executeUpdate(
                "CREATE TABLE IF NOT EXISTS pickaxe_level (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, level INT, exp INT)");

    }

    private void registerCommand(@NotNull Command command, @NotNull String description) {
        try {
            if (commandMap == null) {
                Field commandMapField = getServer().getClass().getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                commandMap = (SimpleCommandMap) commandMapField.get(getServer());
            }
            command.setDescription(description);
            
            commandMap.register("", command);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    

    private void registerEventListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new EventListener(), this);
    }

    public DataBase getDatabase() {
        return database;
    }
}
