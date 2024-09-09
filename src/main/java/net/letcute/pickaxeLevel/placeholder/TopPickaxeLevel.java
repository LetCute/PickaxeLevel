package net.letcute.pickaxeLevel.placeholder;

import java.util.List;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.letcute.pickaxeLevel.PickaxeLevel;
import net.letcute.pickaxeLevel.cache.PickaxeLevelCache;
import net.letcute.pickaxeLevel.database.entity.PickaxeLevelEntity;

public class TopPickaxeLevel extends PlaceholderExpansion  {

    private final PickaxeLevel plugin;

    public TopPickaxeLevel(PickaxeLevel plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "pickaxelevel";
    }

    @Override
    public String getAuthor() {
        return "LetCute";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getPlugin() {
        return null;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        PickaxeLevelCache cache = plugin.getCache().getPickaxeLevelCache();
        List<PickaxeLevelEntity> top10 = cache.getPickaxeLevelEntityCacheTop10();

        for (int i = 0; i < top10.size(); i++) {
            PickaxeLevelEntity entity = top10.get(i);
            if (identifier.equals("top_" + (i + 1) + "_name")) {
                return entity.getName();
            } else if (identifier.equals("top_" + (i + 1) + "_level")) {
                return String.valueOf(entity.getLevel());
            }
        }
        return null;
    }
}
