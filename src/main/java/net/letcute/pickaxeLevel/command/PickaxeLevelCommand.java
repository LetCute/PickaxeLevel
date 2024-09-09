package net.letcute.pickaxeLevel.command;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.letcute.pickaxeLevel.PickaxeLevel;
import net.letcute.pickaxeLevel.cache.PickaxeLevelCache;
import net.letcute.pickaxeLevel.config.Config;
import net.letcute.pickaxeLevel.database.PickaxeLevelDB;
import net.letcute.pickaxeLevel.database.entity.PickaxeLevelEntity;
import net.letcute.pickaxeLevel.item.ItemPickaxeLevel;

public class PickaxeLevelCommand extends PluginsCommand {

    private Config config = PickaxeLevel.getInstance().getData();

    private PickaxeLevelCache pickaxeLevelCache = PickaxeLevel.getInstance().getCache().getPickaxeLevelCache();
    private PickaxeLevelDB pickaxeLevelDB = PickaxeLevel.getInstance().getDatabase().getPickaxeLevelDB();

    public PickaxeLevelCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, String[] args) {
        Player player = (Player) commandSender;

        if (args.length == 0 || !player.isOp()) {
            givePickaxe(player);
            return false;
        }
        switch (args[0]) {
            case "setexp":
                try {
                    int level = Integer.parseInt(args[2]); // Thử chuyển đổi chuỗi thành số nguyên

                    PickaxeLevelEntity pickaxeLevelEntity = pickaxeLevelCache.getCacheByname(args[1]);

                    if (pickaxeLevelEntity == null) {
                        pickaxeLevelEntity = pickaxeLevelDB.getPickaxeLevelByName(args[1]);
                        if (pickaxeLevelEntity == null) {
                            player.sendMessage("Không tìm thấy người chơi: " + args[1]);
                            return false;
                        }
                        pickaxeLevelEntity.setLevel(level);
                        pickaxeLevelDB.updatePickaxeLevel(pickaxeLevelEntity);
                        player.sendMessage("Set level thành công");
                    } else {
                        pickaxeLevelCache.removeCache(pickaxeLevelEntity);
                        pickaxeLevelEntity.setLevel(level);
                        pickaxeLevelCache.addCache(pickaxeLevelEntity);
                        player.sendMessage("Set level thành công");
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(
                            "Lệnh không hợp lệ /pickaxe addexp [player] [level] - Level phải là một số nguyên.");
                }
                break;
            case "reload":
                PickaxeLevel.getInstance().getData().reloadConfig();
                player.sendMessage("reload config thành công");
                break;
            default:
                player.sendMessage("Lệnh không hợp lệ");
                break;
        }
        return false;
    }

    private void givePickaxe(Player player) {
        PickaxeLevelEntity pickaxeLevelEntity = pickaxeLevelCache.getCacheByname(player.getName());
        ItemStack item = ItemPickaxeLevel.createPickaxe(
                Material.DIAMOND_PICKAXE,
                player.getName(),
                String.format(config.getNamePickaxe(), player.getName(), pickaxeLevelEntity.getLevel()),
                config.getLorePickaxe());
        item = ItemPickaxeLevel.uplevel(item, pickaxeLevelEntity.getLevel());
        player.getInventory().addItem(item);
        player.sendMessage(config.getAddPickaxe());
    }

    @Nullable
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args)
            throws IllegalArgumentException {
        List<String> suggestions = new ArrayList<>();
        if (sender.isOp()) {
            switch (args.length) {
                case 1:
                    suggestions.add("setexp");
                    suggestions.add("reload");
                    break;
                case 2:
                    sender.getServer().getOnlinePlayers().stream()
                            .map(Player::getName)
                            .forEach(suggestions::add);
                    break;
                case 3:
                    suggestions.addAll(
                            IntStream.rangeClosed(1, 100)
                                    .mapToObj(String::valueOf)
                                    .collect(Collectors.toList()));
                    break;
                default:
                    break;
            }
        }

        return suggestions;
    }

}
