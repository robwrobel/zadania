/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zadania;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Robert
 */
public class DBConnection {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/zadania";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "";    

    private static final String UNKNOWN_USER = "Nieznany";
    
    private static final String LOGIN_SELECT = "SELECT * from pracownik WHERE login=? AND haslo=?";
    private static final String USER_SELECT = "SELECT imie, nazwisko FROM pracownik WHERE login=?";
    private static final String USER_TASKS_SELECT = "SELECT * FROM lista_zadan WHERE `Przypisane do`=? ";
    private static final String ALL_TASKS_SELECT = "SELECT * FROM lista_zadan";
    private static final String TASKS_INSERT = "INSERT INTO zadanie (data_stworzenia,id_status) "
                                             + "VALUES (CURRENT_TIMESTAMP,1)"; 
    private static final String TASKS_DELETE = "DELETE FROM zadanie WHERE id=?";
    private static final String TASK_DONE_UPDATE = "UPDATE zadanie SET id_status=3,data_wykonania=CURRENT_TIMESTAMP WHERE id=?";
    
    private static DBConnection instance;
    private Connection connection; 
    private ResultSet rs;
    
    private DBConnection() {
        try {
            connection = DriverManager.getConnection(DEFAULT_URL,DEFAULT_USERNAME,DEFAULT_PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);     
        }
    }    
    
    public static DBConnection getDBConnection() {
        if (instance==null)
            instance = new DBConnection();
        return instance;
    }    

    public boolean isValidUserPassword(String user, String password) {
        
        String sql=LOGIN_SELECT;
        
        try {
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,user);
            ps.setString(2,password);
            rs=ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public String getUserFullName(String login) {
        
        String sql=USER_SELECT;
        
        String firstName, lastName;
        try {
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,login);
            rs=ps.executeQuery();
            if (rs.next()) {
                firstName = rs.getString("imie");
                lastName = rs.getString("nazwisko");
                return firstName + " " + lastName;
            } else
                return UNKNOWN_USER;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return UNKNOWN_USER;
        }        
    }

    
    public void prepareTableDataFromDatabase(DefaultTableModel dtm, String login) {
        if (login !=null) {
            String sql=USER_TASKS_SELECT;
            
            PreparedStatement ps;
            try {
                ps = connection.prepareStatement(sql);
                ps.setString(1,login);
                rs=ps.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int colNo = rsmd.getColumnCount(); 
                String [] colNames = new String[colNo];
                for (int i=0;i<colNo;i++) {
                    colNames[i]=rsmd.getColumnName(i+1);
                }
                dtm.setColumnIdentifiers(colNames);
                while(rs.next()) {
                    Object[] objects = new Object[colNo];
                    for (int i=0;i<colNo;i++) {
                        objects[i]=rs.getObject(i+1);
                    }
                    dtm.addRow(objects);
                } 
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            String sql=ALL_TASKS_SELECT;
           
            try {
                Statement s = connection.createStatement();
                rs=s.executeQuery(sql);
                ResultSetMetaData rsmd = rs.getMetaData();
                int colNo = rsmd.getColumnCount(); 
                String [] colNames = new String[colNo];
                for (int i=0;i<colNo;i++) {
                    colNames[i]=rsmd.getColumnName(i+1);
                }
                dtm.setColumnIdentifiers(colNames);
                while(rs.next()) {
                    Object[] objects = new Object[colNo];
                    for (int i=0;i<colNo;i++) {
                        objects[i]=rs.getObject(i+1);
                    }
                    dtm.addRow(objects);
                } 
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
        
    }
    
    public void addTask() {
        
        String sql=TASKS_INSERT;

        try {
            Statement s = connection.createStatement();
            s.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void deleteTask(int id) {
        
        String sql=TASKS_DELETE;
        
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    void updateTask(int id) {
        
        String sql=TASK_DONE_UPDATE;
        
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }    

}
