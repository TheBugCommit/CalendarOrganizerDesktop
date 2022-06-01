package org.milaifontanals.persistencia;

import java.sql.ResultSet;
import java.util.ArrayList;
import org.milaifontanals.models.Calendar;
import org.milaifontanals.models.User;

/**
 *
 * @author Gerard Casas
 */
public interface ICalendarOrganizer {
    public void insertUser(User user) throws CalendarOrganizerException;
    public User searchUserByEmail(String email) throws CalendarOrganizerException;
    public ArrayList<User> searchUserByNameSurname(String nameSurname) throws CalendarOrganizerException;
    public User getUserCalendars(User u) throws CalendarOrganizerException;
    public User collectUser(ResultSet rs) throws CalendarOrganizerException;
    public Calendar collectCalendar(ResultSet rs) throws CalendarOrganizerException;
    public boolean checkAuth(String email, String password) throws CalendarOrganizerException;
    
    public void commit() throws CalendarOrganizerException;
    public void rollBack() throws CalendarOrganizerException;
    public void close() throws CalendarOrganizerException;
}
