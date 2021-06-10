package sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, описывающий соединение с БД
 * @author Admin
 */
public abstract class DBConnection {
        /**
     * Метод открытия соединения
     * @return соединение
     */
    public Connection open(){
        return SQLiteConnection.getConnection();
    }
    
    /**
     * Метод закрытия соединения
     * @param conn соединение
     */
    public void close(Connection conn){
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}