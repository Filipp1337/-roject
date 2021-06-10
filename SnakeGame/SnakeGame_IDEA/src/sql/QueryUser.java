package sql;

import data.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class QueryUser extends DBConnection {

    private final String queryGetUser = "SELECT id, login, password, score FROM users WHERE login = ? AND password = ?"; 
    private final String queryGetAllByScore = "SELECT id, login, password, score FROM users WHERE score > 0";  

    private final String queryAddUser = "INSERT INTO users (login, password) VALUES (?, ?)";
    private final String queryAddScore = "UPDATE users SET score = ? WHERE id = ?";


    private Connection conn;

    /**
     * Получение всех пользователей со статистикой больше 0
     *
     * @return список пользователей со статистикой больше 0
     */
    public List<User> getAllByScore() {
        List<User> list = new ArrayList<>();
        Statement stat = null;
        ResultSet rs = null;
        try {
            conn = open();
            //получение объекта типа Statement
            stat = conn.createStatement();
            //получение результирующего объекта типа ResultSet
            rs = stat.executeQuery(queryGetAllByScore);
            while (rs.next()) {
                //создание нового лица, полученного из БД
                User user = new User(rs.getInt("id"), rs.getString("login"), rs.getString("password"), rs.getInt("score"));
                //добавление в список пользователей
                list.add(user);
            }
            rs.close();
            stat.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                rs.close();
                stat.close();
                close(conn);
                Logger.getLogger(sql.QueryUser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryUser.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return list;
    }
    
    /**
     * Получение пользователя по данных входа
     * @param login
     * @param password
     * @return пользователь
     */
    public User getUser(String login, String password){
        User user = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = open();
            //получение объекта типа PreparedStatement
            ps = conn.prepareStatement(queryGetUser);
            ps.setString(1, login);
            ps.setString(2, password);
            //получение результирующего объекта типа ResultSet
            rs = ps.executeQuery();
            if (rs.next()) {
                //создание нового лица, полученного из БД
                user = new User(rs.getInt("id"), rs.getString("login"), rs.getString("password"), rs.getInt("score"));
                //добавление в список пользователей
            }
            rs.close();
            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                rs.close();
                ps.close();
                close(conn);
                Logger.getLogger(sql.QueryUser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryUser.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return user;
    }
    
   /**
    * Добавление пользователя в БД
     * @param login логин    
     * @param password пароль 
    * @return true в случае добавления, иначе - false
    */
    public boolean add(String login, String password) {
        PreparedStatement ps = null;
        int executeUpdate = -1;
        try {
            conn = open();
            //получение объекта типа PreparedStatement
            ps = conn.prepareStatement(queryAddUser);
            //установка параметров
            ps.setString(1, login);
            ps.setString(2, password);
            //запрос в БД
            executeUpdate = ps.executeUpdate();
            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
                Logger.getLogger(QueryUser.class.getName()).log(Level.SEVERE, null, ex);
                return executeUpdate > -1;
            } catch (SQLException ex1) {
                Logger.getLogger(QueryUser.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return executeUpdate > -1;
    }

    /**
    * Добавление пользователя в БД
    * @param user пользователь
    * 
    * @return true в случае добавления, иначе - false
    */
    public boolean addScore(User user) {
        PreparedStatement ps = null;
        int executeUpdate = -1;
        try {
            conn = open();
            //получение объекта типа PreparedStatement
            ps = conn.prepareStatement(queryAddScore);
            //установка параметров
            ps.setInt(1, user.getScore());
            ps.setInt(2, user.getId());
            //запрос в БД
            executeUpdate = ps.executeUpdate();
            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
                Logger.getLogger(QueryUser.class.getName()).log(Level.SEVERE, null, ex);
                return executeUpdate > -1;
            } catch (SQLException ex1) {
                Logger.getLogger(QueryUser.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return executeUpdate > -1;
    }
    
}
