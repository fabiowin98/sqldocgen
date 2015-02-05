package org.dgl.manager;

public class DBManager {

    private final String DB_DRIVER = "db_driver";
    private final String DB_URL = "db_url";
    private final String DB_USERNAME = "db_username";
    private final String DB_PASSWORD = "db_password";
    private final int DEFAULT_QUERY_TIMEOUT = 30;
    private final int DEFAULT_LOGIN_TIMEOUT = 30;
    private java.sql.Connection connection;
    private java.sql.Statement statement;
    private java.sql.ResultSet rs;
    private final String driver, url, username, password;

    public DBManager() throws Exception {
        org.dgl.manager.ConfigurationManager config;
        config = new org.dgl.manager.ConfigurationManager();
        driver = config.get(DB_DRIVER);
        url = config.get(DB_URL);
        username = config.get(DB_USERNAME);
        password = config.get(DB_PASSWORD);
        Class.forName(driver);
        java.sql.DriverManager.setLoginTimeout(DEFAULT_LOGIN_TIMEOUT);
        connection = java.sql.DriverManager.getConnection(url + username + password);
    }

    public DBManager(String propertiesFile) throws Exception {
        org.dgl.manager.ConfigurationManager config;
        config = new org.dgl.manager.ConfigurationManager(propertiesFile);
        driver = config.get(DB_DRIVER);
        url = config.get(DB_URL);
        username = config.get(DB_USERNAME);
        password = config.get(DB_PASSWORD);
        Class.forName(driver);
        java.sql.DriverManager.setLoginTimeout(DEFAULT_LOGIN_TIMEOUT);
        connection = java.sql.DriverManager.getConnection(url + username + password);
    }

    public DBManager(String driver, String url, String username, String password) throws Exception {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        Class.forName(driver);
        java.sql.DriverManager.setLoginTimeout(DEFAULT_LOGIN_TIMEOUT);
        connection = java.sql.DriverManager.getConnection(url + username + password);
    }

    public boolean isConnected() throws Exception {
        if (connection != null) {
            return !connection.isClosed();
        }
        return false;
    }

    public void connect() throws Exception {
        if (connection != null) {
            connection = java.sql.DriverManager.getConnection(url + username + password);
        }
    }

    public java.sql.ResultSet query(String sql) throws Exception {
        statement = connection.createStatement();
        statement.setQueryTimeout(DEFAULT_QUERY_TIMEOUT);
        return statement.executeQuery(sql);
    }

    public void update(String sql) throws Exception {
        statement = connection.createStatement();
        statement.setQueryTimeout(DEFAULT_QUERY_TIMEOUT);
        statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
    }

    public void call(String sql) throws Exception {
        statement = connection.createStatement();
        statement.setQueryTimeout(DEFAULT_QUERY_TIMEOUT);
        statement.execute(sql);
    }

    public long getGeneratedKey() throws Exception {
        java.sql.ResultSet keys = statement.getGeneratedKeys();
        keys.next();
        return (keys).getLong(1);
    }

    public void close() throws Exception {
        if (statement != null) {
            statement.close();
        }
        connection.close();
    }

    public void setAutoCommit(boolean value) throws Exception {
        if (!connection.isClosed()) {
            connection.setAutoCommit(value);
        }
    }

    public void commit() throws Exception {
        if (!connection.isClosed()) {
            connection.commit();
        }
    }

    public void rollback() throws Exception {
        if (!connection.isClosed()) {

            connection.rollback();
        }
    }

    public String getDriver() {
        return driver;
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
}
