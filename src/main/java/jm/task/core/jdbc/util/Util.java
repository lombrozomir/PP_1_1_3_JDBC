package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/data_test";
    private static final String USER = "root";
    private static final String PASSWORD = "Zalypa.777";
    private static final SessionFactory sessionFactory;

    private Util() {

    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    static {
        try {
            sessionFactory = new Configuration()
                    .setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                    .setProperty("hibernate.connection.url", URL)
                    .setProperty("hibernate.connection.username", USER)
                    .setProperty("hibernate.connection.password", PASSWORD)
                    .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect")
                    .setProperty("hibernate.hbm2ddl.auto", "update")
                    .setProperty("hibernate.show_sql", "true")
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();

        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }

    }

    public static SessionFactory getSession() {
        return sessionFactory;
    }


}