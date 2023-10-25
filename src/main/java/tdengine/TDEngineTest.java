package tdengine;

import com.taosdata.jdbc.TSDBDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TDEngineTest {
    public static void main(String[] args) throws SQLException {
        String jdbcUrl = "jdbc:TAOS-RS://10.50.15.6:6041?user=root&password=taosdata";

//        String jdbcUrl = "jdbc:TAOS://10.50.15.7:6030?user=root&password=taosdata";
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");
        Connection conn = DriverManager.getConnection(jdbcUrl, connProps);
        System.out.println("Connected");
        conn.close();


//        String libraryPath = System.getProperty("java.library.path");
//        System.out.println(libraryPath);

//        String os = System.getProperty("os.name");
//        String libraryName = "taos";
//
//        if (os.contains("Windows")) {
//            libraryName = libraryName + ".dll";
//        } else if (os.contains("Mac")) {
//            libraryName = "lib" + libraryName + ".dylib";
//        } else if (os.contains("Linux")) {
//            libraryName = "lib" + libraryName + ".so";
//        }
//
//        try {
//            System.loadLibrary(libraryName);
//            System.out.println("Library loaded successfully.");
//        } catch (UnsatisfiedLinkError e) {
//            System.err.println("Failed to load library: " + e.getMessage());
//        }
    }

}



