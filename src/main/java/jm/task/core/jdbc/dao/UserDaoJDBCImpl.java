package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {

    Logger logger = Logger.getLogger(getClass().getName());

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS User (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL, " +
                "lastName VARCHAR(50) NOT NULL, " +
                "age TINYINT)";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.executeUpdate(sql);
            logger.info("Table created");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, String.format("Error creating table: %s", e.getMessage()));
            logger.info("Table not created");
        }
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS User";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.executeUpdate(sql);
            logger.info("Table deleted");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, String.format("Error drop table: %s", e.getMessage()));
            logger.info("Deleted failed");

        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO User (name, lastName, age) VALUES (?, ?, ?)";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            preparedStatement.executeUpdate();

            logger.log(Level.INFO, () -> String.format("User с именем — %s добавлен в базу данных", name));


        } catch (SQLException e) {
            logger.log(Level.SEVERE, String.format("Error save user: %s", e.getMessage()));
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM User WHERE id = ?";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            logger.log(Level.SEVERE, String.format("Error remove by ID user: %s", e.getMessage()));
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name, lastName, age FROM User";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("lastName");
                Byte age = resultSet.getByte("age");
                users.add(new User(name, lastName, age));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, String.format("Error get all users: %s", e.getMessage()));
        }
        return users;
    }

    public void cleanUsersTable() {
        String sql = "DELETE FROM User";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, String.format("Error clean users table: %s", e.getMessage()));
        }
    }
}
