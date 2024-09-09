package net.letcute.pickaxeLevel;

import net.letcute.pickaxeLevel.block.BlockPickaxeLevel;
import net.letcute.pickaxeLevel.cache.PickaxeLevelCache;
import net.letcute.pickaxeLevel.config.Config;
import net.letcute.pickaxeLevel.database.PickaxeLevelDB;
import net.letcute.pickaxeLevel.database.entity.PickaxeLevelEntity;
import net.letcute.pickaxeLevel.item.ItemPickaxeLevel;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EventListener implements Listener {

    private Config config = PickaxeLevel.getInstance().getData();

    private PickaxeLevelCache pickaxeLevelCache = PickaxeLevel.getInstance().getCache().getPickaxeLevelCache();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (BlockPickaxeLevel.checkBlock(block)
                && itemStack.getType().equals(Material.DIAMOND_PICKAXE)
                && ItemPickaxeLevel.isPickaxe(itemStack, player.getName())) {

            PickaxeLevelEntity pickaxeLevelEntity = pickaxeLevelCache.getCacheByname(player.getName());

            if (pickaxeLevelEntity == null) {
                player.sendMessage("Bạn không có dữ liệu cúp nào để xử lý vui lòng liên hệ admin.");
                return;
            }
            if(pickaxeLevelEntity.getLevel() == config.getMaxLevel()){
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(config.getShowMaxLevel()));
                return;
            }

            int level = pickaxeLevelEntity.getLevel();
            int exp = pickaxeLevelEntity.getExp();
            int nextExp = level * config.getNextExpLevel();
            int newExp = exp + 1;

            pickaxeLevelCache.removeCache(pickaxeLevelEntity);

            if (newExp < nextExp) {
                pickaxeLevelEntity.setExp(newExp);
                PickaxeLevel.getInstance().getCache().getPickaxeLevelCache().addCache(pickaxeLevelEntity);
                String message = String.format(config.getShowInfo(), level, newExp, nextExp);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
                return;
            }

            level++;
            pickaxeLevelEntity.setLevel(level);
            pickaxeLevelEntity.setExp(0);
            pickaxeLevelCache.addCache(pickaxeLevelEntity);

            // Cập nhật ItemMeta với tên mới
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName(String.format(config.getNamePickaxe(), player.getName(), level));
                itemStack.setItemMeta(itemMeta);
            }

            // Gọi phương thức uplevel để cập nhật enchantments
            itemStack = ItemPickaxeLevel.uplevel(itemStack, level);
            player.getInventory().setItemInMainHand(itemStack);

            player.sendMessage(String.format(config.getLevelUp(), level));
            String message = String.format(config.getShowInfo(), level, 0, level * config.getNextExpLevel());
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PickaxeLevelDB pickaxeLevelDB = PickaxeLevel.getInstance().getDatabase().getPickaxeLevelDB();
        PickaxeLevelCache pickaxeLevelCache = PickaxeLevel.getInstance().getCache().getPickaxeLevelCache();
        if (pickaxeLevelDB.getPickaxeLevelByName(player.getName()) == null) {
            PickaxeLevelEntity pickaxeLevelEntity = PickaxeLevelEntity.builder()
                    .name(player.getName())
                    .level(1)
                    .exp(0)
                    .build();
            pickaxeLevelDB.addPickaxeLevel(pickaxeLevelEntity);
            pickaxeLevelCache.addCache(pickaxeLevelEntity);
            ItemStack item = ItemPickaxeLevel.createPickaxe(
                    Material.DIAMOND_PICKAXE,
                    player.getName(),
                    String.format(config.getNamePickaxe(), player.getName(), pickaxeLevelEntity.getLevel()),
                    config.getLorePickaxe());
            player.getInventory().addItem(item);
            player.sendMessage(config.getAddPickaxe());
        }
        PickaxeLevelEntity pickaxeLevelEntity = pickaxeLevelDB.getPickaxeLevelByName(player.getName());
        pickaxeLevelCache.addCache(pickaxeLevelEntity);

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PickaxeLevelCache pickaxeLevelCache = PickaxeLevel.getInstance().getCache().getPickaxeLevelCache();
        PickaxeLevelDB pickaxeLevelDB = PickaxeLevel.getInstance().getDatabase().getPickaxeLevelDB();
        PickaxeLevelEntity pickaxeLevelEntity = pickaxeLevelCache.getCacheByname(player.getName());
        pickaxeLevelDB.updatePickaxeLevel(pickaxeLevelEntity);
        pickaxeLevelCache.removeCache(pickaxeLevelCache.getCacheByname(player.getName()));
    }
}
