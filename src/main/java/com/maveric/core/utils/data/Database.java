package com.maveric.core.utils.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database {
    private String connectionUrl;
    private final String user;
    private final String password;
    public List<String> columns;
    private static final Logger logger = LogManager.getLogger();

    public Database(String type, String user, String password, String hostname, String port, String database) {
        this.user = user;
        this.password = password;
        try {
            switch (type) {
                case "mysql":
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    this.connectionUrl = "jdbc:mysql://" +
                            hostname + ":" +
                            port + "/" + database
                            + "?useSSL=false&serverTimezone=UTC";
                    break;
                case "postgre":
                    this.connectionUrl = "jdbc:postgresql://" + hostname + ":" + port + "/" + database;
                    break;
                case "oracle":
                    Class.forName("oracle.jdbc.OracleDriver");
                    this.connectionUrl = "jdbc:oracle:thin:@" + hostname + ":" + port + ":" + database;
                    break;
                case "db2":
                    Class.forName("com.ibm.db2.jcc.DB2Driver");
                    this.connectionUrl = "jdbc:db2://" + hostname + ":" + port + "/" + database;
                    break;
                default:
                    throw new RuntimeException("Invalid Database Type Provided");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.connectionUrl, this.user, this.password);
    }

    public int getRowCount(String query) {
        verifyQuery(query);
        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = statement.executeQuery(query)) {
            return rs.last() ? rs.getRow() + 1 : 0;
        } catch (Exception e) {
            logger.error(e);
            return 0;
        }
    }

    public String readValue(String query, String column, int line) {

        verifyQuery(query);
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            if (line < 1) {
                return column;
            }
            while (rs.next() && rs.getRow() < line) {
            }
            return rs.getString(column);
        } catch (Exception e) {
            logger.error(e);
            return "";
        }
    }

    public String[] readLine(String query, int line, boolean readResult) {
        getColumns(query);
        verifyQuery(query);
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            if (rs == null || rs.getString(1).isEmpty()) {
                return null;
            } else {
                final String[] ret = readResult ? new String[columns.size()] : new String[columns.size() - 1];
                if (line == 0) {
                    for (int i = 0; i < ret.length; i++) {
                        ret[i] = columns.get(i);
                    }
                } else {
                    while (rs.next() && rs.getRow() < line) {
                    }
                    for (int i = 1; i <= ret.length; i++) {
                        ret[i - 1] = rs.getString(i);
                    }
                }
                return ret;
            }
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    private void getColumns(String query) {
        ArrayList<String> columns = new ArrayList<>();
        verifyQuery(query);
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            if (rs.getMetaData().getColumnCount() < 1) {
                throw new RuntimeException("Columns Not Found");
            }
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                columns.add(rs.getMetaData().getColumnLabel(i));
            }
            this.columns = columns;
        } catch (Exception e) {
            logger.error(e);
        }

    }

    private static void verifyQuery(String query) {
        final String[] keywords = {"drop", "delete", "truncate", "update"};
        Arrays.stream(keywords).forEach(keyword -> {
            if (query.equalsIgnoreCase(keyword)) {
                throw new RuntimeException("Unsupported Query " + keyword + " : " + query);
            }
        });

    }

}
