package tdengine;

import com.taosdata.jdbc.TSDBDriver;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class RestInsertExample {
    private static Connection getConnection() throws SQLException, ClassNotFoundException {
        String jdbcUrl = "jdbc:TAOS-RS://10.50.15.6:6041?user=root&password=taosdata";
//        Properties connProps = new Properties();
//        connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
//        connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
//        connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");
//        System.out.println("Connected");

        Class.forName("com.taosdata.jdbc.rs.RestfulDriver");
//        String jdbcUrl = "jdbc:TAOS-RS://taosdemo.com:6041/test?user=root&password=taosdata";
        Connection conn = DriverManager.getConnection(jdbcUrl);

//        Connection conn = DriverManager.getConnection(jdbcUrl, connProps);
//        boolean closed = conn.isClosed();
//        return DriverManager.getConnection(jdbcUrl, connProps);
        return conn;
    }

    public static Connection getRestConn() throws Exception{
        Class.forName("com.taosdata.jdbc.rs.RestfulDriver");
        String jdbcUrl = "jdbc:TAOS-RS://10.50.15.7:6041/test?user=root&password=taosdata";
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_BATCH_LOAD, "true");
        Connection conn = DriverManager.getConnection(jdbcUrl, connProps);
        return conn;
    }

    private static List<String> getRawData() {
        return Arrays.asList(
                "v1001,2018-10-03 14:38:05.000,10.12,40.00,33.00,'TESTVIN001',1",
                "v1001,2018-10-03 14:38:15.000,12.00,40.00,32.00,'TESTVIN001',1",
                "v1001,2018-10-03 14:38:16.800,13.33,40.00,31.00,'TESTVIN001',1",
                "v1002,2018-10-03 14:38:16.650,10.30,40.00,33.00,'TESTVIN002',2",
                "v1003,2018-10-03 14:38:05.500,11.80,40.00,23.00,'TESTVIN003',1",
                "v1003,2018-10-03 14:38:16.600,13.40,40.00,22.00,'TESTVIN003',1",
                "v1004,2018-10-03 14:38:05.000,10.80,40.00,33.00,'TESTVIN004',1",
                "v1004,2018-10-03 14:38:06.500,11.50,40.00,13.00,'TESTVIN004',1"
        );
    }


    /**
     * The generated SQL is:
     * INSERT INTO power.d1001 USING power.meters TAGS(California.SanFrancisco, 2) VALUES('2018-10-03 14:38:05.000',10.30000,219,0.31000)
     * power.d1001 USING power.meters TAGS(California.SanFrancisco, 2) VALUES('2018-10-03 14:38:15.000',12.60000,218,0.33000)
     * power.d1001 USING power.meters TAGS(California.SanFrancisco, 2) VALUES('2018-10-03 14:38:16.800',12.30000,221,0.31000)
     * power.d1002 USING power.meters TAGS(California.SanFrancisco, 3) VALUES('2018-10-03 14:38:16.650',10.30000,218,0.25000)
     * power.d1003 USING power.meters TAGS(California.LosAngeles, 2) VALUES('2018-10-03 14:38:05.500',11.80000,221,0.28000)
     * power.d1003 USING power.meters TAGS(California.LosAngeles, 2) VALUES('2018-10-03 14:38:16.600',13.40000,223,0.29000)
     * power.d1004 USING power.meters TAGS(California.LosAngeles, 3) VALUES('2018-10-03 14:38:05.000',10.80000,223,0.29000)
     * power.d1004 USING power.meters TAGS(California.LosAngeles, 3) VALUES('2018-10-03 14:38:06.500',11.50000,221,0.35000)
     */
    private static String buildInsertSql(String dbName, String sTableName) {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        for (String line : getRawData()) {
            String[] ps = line.split(",");
            sb.append(dbName + "." + ps[0]).append(" USING " + dbName + "." + sTableName + " TAGS(")
                    .append(ps[5]).append(", ") // tag: location
                    .append(ps[6]) // tag: groupId
                    .append(") VALUES(")
                    .append('\'').append(ps[1]).append('\'').append(",") // ts
                    .append(ps[2]).append(",") // current
                    .append(ps[3]).append(",") // voltage
                    .append(ps[4]).append(") "); // phase
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    public static void insertData(String dbName, String sTableName) throws Exception {
        Connection conn = getRestConn();
        Statement stmt = conn.createStatement();
        String ddlSql = "CREATE STABLE if not exists " + dbName + "." + sTableName + " (ts TIMESTAMP, mileage FLOAT, speed FLOAT, soc FLOAT) " +
                "TAGS (vin BINARY(64), tenant_id INT)";
        System.out.println("execute ddl sql: " + ddlSql);
        System.out.println("conn is closed: " + conn.isClosed());
        stmt.execute(ddlSql);

        String insertSql = buildInsertSql(dbName, sTableName);
        boolean execute = stmt.execute(insertSql);
        System.out.println("execute res: " + execute);

        String querySql = "select * from test.v1001";
        ResultSet resultSet = stmt.executeQuery(querySql);

        while (resultSet.next()) {
            Timestamp ts = resultSet.getTimestamp(1);
            int anInt = resultSet.getInt(2);
            float soc = resultSet.getFloat("soc");
            System.out.printf("%s, %d, %s\n", ts, anInt, soc);
        }

        conn.close();
    }

    public static void main(String[] args) throws Exception {
        String dbName = "test";
        String sTableName = "vdcu";
        insertData(dbName, sTableName);
    }


}
