package com.bbutner.Utils;

import com.bbutner.Core.Alfred;

import java.sql.*;

/**
 * Class that interacts with the database
 * @author bbutner
 * @version 1.0
 */
public class Database {

    private static Connection connection;

    /**
     * Submit query that we don't want the result of
     * @param query The SQL String
     */
    static void submitQuery(String query) {
        if (!connectionAlive()) {
            connectDatabase();
        }

        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Submit a query to the database that we want the result from
     * @param query The SQL string
     * @return the ResultSet from the query
     */
    static ResultSet submitReturnQuery(String query) {
        if (!connectionAlive()) {
            connectDatabase();
        }

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.execute(query);

            ResultSet result = statement.getResultSet();

            try {
                result.next();
            } catch (NullPointerException ex) {
                return null;
            }

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if the connection is still alive
     * @return if the database connection is still alive
     */
    private static Boolean connectionAlive() {
        try {
            return connection.isValid(1000);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Connect to the database
     * @return the connection
     */
    public static Connection connectDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlserver://" + Alfred.getHostname() + ";user=sa;password=" + Alfred.getPassword() + ";database=nexbot");

            Logging.infoBot("Connected to SQL.");

            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
