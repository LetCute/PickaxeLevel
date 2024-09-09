package net.letcute.pickaxeLevel.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import net.letcute.pickaxeLevel.database.entity.PickaxeLevelEntity;

public class PickaxeLevelCache {
    
    private Set<PickaxeLevelEntity> pickaxeLevelEntityCache = new HashSet<>();

    @Getter
    @Setter
    private List<PickaxeLevelEntity> pickaxeLevelEntityCacheTop10 = new ArrayList<>();

    public void addCache(PickaxeLevelEntity entity) {
        pickaxeLevelEntityCache.add(entity);
    }

    public PickaxeLevelEntity getCacheByname(String name) {
        for (PickaxeLevelEntity entity : pickaxeLevelEntityCache) {
            if (entity.getName().equals(name)) {
                return entity;
            }
        }
        return null;
    }

    public boolean isCache(PickaxeLevelEntity entity) {
        return pickaxeLevelEntityCache.contains(entity);
    }

    public void removeCache(PickaxeLevelEntity entity) {
        pickaxeLevelEntityCache.remove(entity);
    }

    public void clearCache() {
        pickaxeLevelEntityCache.clear();
    }

    public Set<PickaxeLevelEntity> getAll() {
        return new HashSet<>(pickaxeLevelEntityCache);
    }
}
