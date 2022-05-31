package org.milaifontanals.persistencia;

import org.milaifontanals.User;

/**
 *
 * @author Gerard Casas
 */
public interface IProjecteDaw {
    public void insertUser(User user) throws ProjecteDawException;
    public User cercarUser(String eMail) throws ProjecteDawException;
    
    
    public void validar() throws ProjecteDawException;
    public void desfer() throws ProjecteDawException;
    public void tancarCapa() throws ProjecteDawException;
    
}
