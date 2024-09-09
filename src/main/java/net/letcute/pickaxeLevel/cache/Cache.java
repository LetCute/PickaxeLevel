package net.letcute.pickaxeLevel.cache;

import lombok.Getter;

public class Cache {

    @Getter
    private PickaxeLevelCache pickaxeLevelCache;

    public Cache(){
        this.pickaxeLevelCache = new PickaxeLevelCache();
    }
}
