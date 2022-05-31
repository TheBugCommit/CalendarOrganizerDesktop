/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals;

import org.milaifontanals.models.User;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.milaifontanals.models.Role;
import org.milaifontanals.persistencia.CalendarOrganizerException;
import org.milaifontanals.persistencia.ICalendarOrganizer;
import org.milaifontanals.security.PasswordManager;

public class BDRLayer implements ICalendarOrganizer {

    private Connection con;

    public BDRLayer() throws CalendarOrganizerException {
        Properties p = new Properties();
        try {
            p.load(new FileReader("connection.properties"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw new CalendarOrganizerException("Problem loading config file", ex.getCause());
        }

        String url = p.getProperty("db_url");
        String username = p.getProperty("db_username");
        String password = p.getProperty("db_password");
        if (url == null || username == null || password == null) {
            throw new CalendarOrganizerException("Need properites: db_url, db_username, db_password");
        }
        
        try {
            this.con = DriverManager.getConnection(url, username, password);
            this.con.setAutoCommit(false);
            System.out.println("connexio establerta");
        } catch (SQLException ex) {
                        System.out.println(ex.getMessage());

            throw new CalendarOrganizerException("Error trying to connecto to database", ex.getCause());
        }
    }
    
    @Override
    public void insertUser(User user) throws CalendarOrganizerException {

        throw new CalendarOrganizerException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User searchUser(String eMail) throws CalendarOrganizerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean checkAuth(String email, String password) throws CalendarOrganizerException {
        try{
            PreparedStatement ps = con.prepareStatement("select email, password from users where role_id = ? and email like ? and password like ?");
            ps.setLong(1, Role.ADMIN.getId());
            ps.setString(2, email);
            ps.setString(3, PasswordManager.hash(password));
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }catch(SQLException ex){
           throw new CalendarOrganizerException("An error ocurred can't check auth Error: " + ex.getMessage(), ex.getCause());
        }
    }
    
    @Override
    public void commit() throws CalendarOrganizerException {
        try {
            con.commit();
        } catch (SQLException ex) {
            throw new CalendarOrganizerException("Error en validar els canvis", ex);
        }
    }

    @Override
    public void rollBack() throws CalendarOrganizerException {
        try {
            con.rollback();
        } catch (SQLException ex) {
            throw new CalendarOrganizerException("Error en desfer els canvis", ex);
        }
    }

    @Override
    public void close() throws CalendarOrganizerException {
        try {
            con.rollback();
            con.close();
        } catch (SQLException ex) {
            throw new CalendarOrganizerException("Error en tancar la capa", ex);
        }
    }

}
