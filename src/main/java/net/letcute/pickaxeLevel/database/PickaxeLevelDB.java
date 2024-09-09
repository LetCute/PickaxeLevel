package net.letcute.pickaxeLevel.database;

import net.letcute.pickaxeLevel.PickaxeLevel;
import net.letcute.pickaxeLevel.database.entity.PickaxeLevelEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;

public class PickaxeLevelDB {

    private final DataBase database;

    public PickaxeLevelDB(DataBase database) {
        this.database = database;
    }

    public void addPickaxeLevel(PickaxeLevelEntity pickaxeLevel) {
        String sql = "INSERT INTO pickaxe_level(name, level, exp) VALUES(?, ?, ?)";

        try {
            Connection conn = database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pickaxeLevel.getName());
            pstmt.setInt(2, pickaxeLevel.getLevel());
            pstmt.setDouble(3, pickaxeLevel.getExp());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error adding PickaxeLevel: " + pickaxeLevel, e);
        }
    }

    public void updatePickaxeLevel(PickaxeLevelEntity pickaxeLevel) {
        String sql = "UPDATE pickaxe_level SET level = ?, exp = ? WHERE name = ?";
        try {
            Connection conn = database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pickaxeLevel.getLevel());
            pstmt.setDouble(2, pickaxeLevel.getExp());
            pstmt.setString(3, pickaxeLevel.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error updating PickaxeLevel: " + pickaxeLevel, e);
        }
    }

    public PickaxeLevelEntity getPickaxeLevelByName(String name) {
        String sql = "SELECT id, name, level, exp FROM pickaxe_level WHERE name = ?";
        PickaxeLevelEntity pickaxeLevel = null;

        try {
            Connection conn = database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    pickaxeLevel = PickaxeLevelEntity.builder()
                            .id(rs.getInt("id"))
                            .name(rs.getString("name"))
                            .level(rs.getInt("level"))
                            .exp(rs.getInt("exp"))
                            .build();
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error fetching PickaxeLevel by name: " + name, e);
        }

        return pickaxeLevel;
    }

    public List<PickaxeLevelEntity> getAllPickaxeLevels() {
        String sql = "SELECT id, name, level, exp FROM pickaxe_level";
        List<PickaxeLevelEntity> pickaxeLevels = new ArrayList<>();

        try {
            Connection conn = database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                PickaxeLevelEntity pickaxeLevel = PickaxeLevelEntity.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .level(rs.getInt("level"))
                        .exp(rs.getInt("exp"))
                        .build();
                pickaxeLevels.add(pickaxeLevel);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error fetching all PickaxeLevels", e);
        }

        return pickaxeLevels;
    }

    public void deletePickaxeLevelByName(String name) {
        String sql = "DELETE FROM pickaxe_level WHERE name = ?";

        try {
            Connection conn = database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error deleting PickaxeLevel by name: " + name, e);
        }
    }

    public List<PickaxeLevelEntity> getTop10PlayersByLevel() {
        String sql = "SELECT id, name, level, exp FROM pickaxe_level ORDER BY level DESC, exp DESC LIMIT 10";
        List<PickaxeLevelEntity> topPlayers = new ArrayList<>();

        try {
            Connection conn = database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                PickaxeLevelEntity pickaxeLevel = PickaxeLevelEntity.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .level(rs.getInt("level"))
                        .exp(rs.getInt("exp"))
                        .build();
                topPlayers.add(pickaxeLevel);
            }
            PickaxeLevel.getInstance().getCache().getPickaxeLevelCache().setPickaxeLevelEntityCacheTop10(topPlayers);
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error fetching top 10 players by level", e);
        }

        return topPlayers;
    }

}
