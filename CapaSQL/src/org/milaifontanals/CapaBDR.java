/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.milaifontanals.persistencia.ProjecteDawException;
import org.milaifontanals.persistencia.IProjecteDaw;

/**
 * Capa per treballar amb BDR, Les dades de connexió han de residir en un fitxer
 * de propietats de nom ProjecteDaw.properties amb propietats<BR>: url = user =
 * passwd =
 *
 */
public class CapaBDR implements IProjecteDaw {

    private Connection con;

    public void CapaBDR() throws ProjecteDawException {
        // S'encarrega d'establir la connexió omplint con
        // i autocommit a false
        // Necessita url, usuari, contrasenya
    }

    @Override
    public void insertUser(User user) throws ProjecteDawException {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User cercarUser(String eMail) throws ProjecteDawException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void validar() throws ProjecteDawException {
        try {
            con.commit();
        } catch (SQLException ex) {
            throw new ProjecteDawException("Error en validar els canvis", ex);
        }
    }

    @Override
    public void desfer() throws ProjecteDawException {
        try {
            con.rollback();
        } catch (SQLException ex) {
            throw new ProjecteDawException("Error en desfer els canvis", ex);
        }
    }

    @Override
    public void tancarCapa() throws ProjecteDawException {
        try {
            con.rollback();
            con.close();
        } catch (SQLException ex) {
            throw new ProjecteDawException("Error en tancar la capa", ex);
        }
    }

}
