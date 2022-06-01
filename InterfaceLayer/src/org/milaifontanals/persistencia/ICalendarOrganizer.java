package org.milaifontanals.persistencia;

import java.sql.ResultSet;
import org.milaifontanals.models.User;

/**
 *
 * @author Gerard Casas
 */
public interface ICalendarOrganizer {
    public void insertUser(User user) throws CalendarOrganizerException;
    public User searchUserByEmail(String email) throws CalendarOrganizerException;
    public User searchUserByNameSurname(String nameSurname) throws CalendarOrganizerException;
    
    public boolean checkAuth(String email, String password) throws CalendarOrganizerException;
    
    public void commit() throws CalendarOrganizerException;
    public void rollBack() throws CalendarOrganizerException;
    public void close() throws CalendarOrganizerException;
}
