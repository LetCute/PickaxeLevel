package net.letcute.pickaxeLevel.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Getter;

public class DataBase {

    private Connection connection;
    private final String databasePath;
    private final Logger logger;

    @Getter
    private final PickaxeLevelDB pickaxeLevelDB;

    public DataBase(File pluginFolder, String databaseName, Logger logger) {
        File dbFile = new File(pluginFolder, databaseName + ".db");
        this.databasePath = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        this.logger = logger;
        this.pickaxeLevelDB = new PickaxeLevelDB(this); // Pass the DataBase instance to PickaxeLevelDB
    }

    public void openConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(databasePath);
                logger.info("SQLite database connection established successfully.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not establish SQLite database connection.", e);
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("SQLite database connection closed successfully.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not close SQLite database connection.", e);
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            if (connection == null || connection.isClosed()) {
                openConnection();
            }
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error executing query: " + query, e);
            return null;
        }
    }

    public void executeUpdate(String query) {
        try {
            if (connection == null || connection.isClosed()) {
                openConnection();
            }
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error executing update: " + query, e);
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                openConnection();
            }
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
