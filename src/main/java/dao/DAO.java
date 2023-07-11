package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import global.INIT;

public class DAO {
    private final String url = INIT.JDBC;
    private final String username = INIT.USER_NAME_DB;
    private final String password = INIT.PASS_WORD_DB;
    protected Connection connection = null;
    public DAO() {
        super();
    }
    public boolean connect(){
        boolean resp = false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            resp = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        return resp;
    }
    public boolean close(){
        boolean resp = false;
        try {
            connection.close();
            resp = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resp;
    }
    public String getUrl() {
        return url;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}