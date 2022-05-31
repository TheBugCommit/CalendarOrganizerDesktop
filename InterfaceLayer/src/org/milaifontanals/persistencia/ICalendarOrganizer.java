package org.milaifontanals.persistencia;

import org.milaifontanals.models.User;

/**
 *
 * @author Gerard Casas
 */
public interface ICalendarOrganizer {
    public void insertUser(User user) throws CalendarOrganizerException;
    public User searchUser(String eMail) throws CalendarOrganizerException;
    
    public boolean checkAuth(String email, String password) throws CalendarOrganizerException;
    
    public void commit() throws CalendarOrganizerException;
    public void rollBack() throws CalendarOrganizerException;
    public void close() throws CalendarOrganizerException;
}
