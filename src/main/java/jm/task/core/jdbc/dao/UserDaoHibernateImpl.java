package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {

    private static final SessionFactory sessionFactory = Util.getSession();
    private static final Logger logger = Logger.getLogger(UserDaoHibernateImpl.class.getName());

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS User (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL, " +
                "lastName VARCHAR(50) NOT NULL, " +
                "age TINYINT)";

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
            logger.info("Table created successfully");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, String.format("Error creating table: %s", e.getMessage()));
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS User";
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, String.format("Error dropping table: %s", e.getMessage()));
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, String.format("Error saving user: %s", e.getMessage()));
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);

            if (user != null) session.delete(user);
            else logger.log(Level.SEVERE, "User with id " + id + " not found");

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, String.format("Error removing user: %s", e.getMessage()));
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String hql = "FROM User";
        try (Session session = sessionFactory.openSession()) {
            users = session.createQuery(hql, User.class).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("Error getting all users: %s", e.getMessage()));
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            String hql = "DELETE FROM User";

            session.createQuery(hql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackException) {
                    logger.log(Level.SEVERE, String.format("Rollback failed: %s", rollbackException.getMessage()));
                }
            }
            logger.log(Level.SEVERE, String.format("Error cleaning table: %s", e.getMessage()));
        }
    }
}
