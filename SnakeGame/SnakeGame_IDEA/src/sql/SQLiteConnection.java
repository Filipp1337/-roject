package sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class SQLiteConnection {
    private static Connection conn;
    
    /**
     * Создание соединения с базой данных
     * @return 
     */
    public static Connection getConnection(){
        try {
            
            Driver driver = (Driver) Class.forName("org.sqlite.JDBC").newInstance();

            //Создание подключения к базе данных
            String url = "jdbc:sqlite:statistic.db";
            Properties properties = new Properties();
            //Если conn=null или закрыто соединение, то создать его, иначе вернуть соединение              
            if (conn==null || conn.isClosed()) 
                conn = DriverManager.getConnection(url, properties);
            return conn;
              
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            
        }
        System.out.println("Error: Not found connection for DB");
        return null;
    }
    /**
     * Закрытие соединения с базой данных
     */
    public static void closeConnection(){
        if(conn!=null) try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: close connection");
        }
    }
}
