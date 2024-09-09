package net.letcute.pickaxeLevel.block;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Set;
import java.util.HashSet;

public class BlockPickaxeLevel {

    private static final Set<Material> ORE_MATERIALS = new HashSet<>();

    static {
        ORE_MATERIALS.add(Material.DIAMOND_ORE);
        ORE_MATERIALS.add(Material.IRON_ORE);
        ORE_MATERIALS.add(Material.GOLD_ORE);
        ORE_MATERIALS.add(Material.LAPIS_ORE);
        ORE_MATERIALS.add(Material.EMERALD_ORE);
        ORE_MATERIALS.add(Material.COAL_ORE);
    }

    public static boolean checkBlock(Block block) {
        return ORE_MATERIALS.contains(block.getType());
    }
}
