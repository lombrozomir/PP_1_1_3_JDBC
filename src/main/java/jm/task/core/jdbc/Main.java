package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;


public class Main {
    public static void main(String[] args) {

        UserDao userDaoJDBC = new UserDaoJDBCImpl();
        userDaoJDBC.createUsersTable();
        userDaoJDBC.saveUser("dffd", "wdsgwe", (byte) 12);
        userDaoJDBC.dropUsersTable();


    }
}
