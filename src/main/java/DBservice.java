import org.sqlite.SQLiteConfig;

import java.sql.*;

public class DBservice {
    private Connection connection = null;
    private PreparedStatement pstmt = null;

    private Connection connect() {
        try {
            if (connection != null && !connection.isClosed()) return connection;
            else
                try {
                    SQLiteConfig config = new SQLiteConfig();

                    config.setPragma(SQLiteConfig.Pragma.SYNCHRONOUS, "OFF");
                    config.setPragma(SQLiteConfig.Pragma.JOURNAL_MODE, "Memory");

                    connection = DriverManager.getConnection("jdbc:sqlite::memory", config.toProperties());
//                    connection = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite",config.toProperties());

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void initDB(String[] columnsNames) {
        String sql = null;

        try {
            sql = String.format(
//                            "DELETE FROM userData;\n" +
//                            "VACUUM;\n" +
                    "CREATE TABLE IF NOT EXISTS userData(\n" +
                            "                         %s TEXT,\n" +
                            "                         %s TEXT,\n" +
                            "                         %s TEXT,\n" +
                            "                         %s TEXT,\n" +
                            "                         %s TEXT,\n" +
                            "                         %s TEXT,\n" +
                            "                         %s TEXT,\n" +
                            "                         %s TEXT,\n" +
                            "                         %s TEXT,\n" +
                            "                         %s TEXT\n" +
                            ");"
                    , columnsNames[0], columnsNames[1], columnsNames[2], columnsNames[3], columnsNames[4],
                    columnsNames[5], columnsNames[6], columnsNames[7], columnsNames[8], columnsNames[9]);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        connect();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(String[] record) {
        getPrepareStatement();
        try {
            for (int i = 1; i < record.length + 1; i++) {
                pstmt.setString(i, record[i - 1]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getPrepareStatement() {
        if (pstmt != null) return;
        else {
            try {
                pstmt = connection.prepareStatement("INSERT INTO userData VALUES(?,?,?,?,?,?,?,?,?,?)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

