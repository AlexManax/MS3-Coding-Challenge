import org.sqlite.SQLiteConfig;

import java.sql.*;

public class DBservice {
    private Connection connection = null;
    private PreparedStatement pstmt = null;

    private Connection connect() { //establish DB connection + config
        try {
            if (connection != null && !connection.isClosed()) return connection;
            else
                try {
                    SQLiteConfig config = new SQLiteConfig();

                    config.setPragma(SQLiteConfig.Pragma.SYNCHRONOUS, "OFF"); //SQLite config - don't wait complete write operation
                    config.setPragma(SQLiteConfig.Pragma.JOURNAL_MODE, "Memory"); //Journal in memory - dramatically increase productivity

                    connection = DriverManager.getConnection("jdbc:sqlite::memory", config.toProperties());
//                    connection = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite",config.toProperties());

                } catch (SQLException e) {
                    e.printStackTrace();
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void initDB(String[] columnsNames) { //creates DB table
        String sql = null;

        try {
            sql = String.format( //define columns names from first record
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

    public void insert(String[] record) { //insert record in DB
        getPrepareStatement(); //init singleton PrepareStatement
        try {
            for (int i = 1; i < record.length + 1; i++) { //add fields from record to PrepareStatement
                pstmt.setString(i, record[i - 1]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getPrepareStatement() {
        if (pstmt == null) {
            try {
                pstmt = connection.prepareStatement("INSERT INTO userData VALUES(?,?,?,?,?,?,?,?,?,?)"); //PS with undefined columns in order to admit dynamic columns names functionality.
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void closeConnection(){ //close connection to DB
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } //I got fun, hope you too =) Thx.
    }
}

