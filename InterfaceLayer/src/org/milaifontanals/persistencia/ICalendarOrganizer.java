package org.milaifontanals.persistencia;

import java.sql.ResultSet;
import java.util.ArrayList;
import org.milaifontanals.models.Calendar;
import org.milaifontanals.models.Category;
import org.milaifontanals.models.Event;
import org.milaifontanals.models.Nation;
import org.milaifontanals.models.User;

/**
 *
 * @author Gerard Casas
 */
public interface ICalendarOrganizer {
    public User searchUserByEmail(String email) throws CalendarOrganizerException;
    public ArrayList<User> searchUserByNameSurname(String nameSurname) throws CalendarOrganizerException;
    public ArrayList<Calendar> getUserOwnerCalendars(long userId) throws CalendarOrganizerException;
    public ArrayList<Calendar> getUserHelperCalendars(long userId) throws CalendarOrganizerException;
    public User collectUser(ResultSet rs) throws CalendarOrganizerException;
    public Calendar collectCalendar(ResultSet rs) throws CalendarOrganizerException;
    public Event collectEvent(ResultSet rs) throws CalendarOrganizerException;
    public boolean checkAuth(String email, String password) throws CalendarOrganizerException;
    public ArrayList<Nation> getNations() throws CalendarOrganizerException;
    public void updateUser(User user) throws CalendarOrganizerException;
    public ArrayList<Event> getCalendarEvents(long calendarId) throws CalendarOrganizerException;
    public void commit() throws CalendarOrganizerException;
    public void rollBack() throws CalendarOrganizerException;
    public void close() throws CalendarOrganizerException;
    public void deleteEvent(long id) throws CalendarOrganizerException;
    public boolean eventIsPublished(long id) throws CalendarOrganizerException;
    public ArrayList<Category> getUserCategories(long userId) throws CalendarOrganizerException; 
    public ArrayList<Category> getUserCategoriesByEmail(String email) throws CalendarOrganizerException; 
    public void createEvent(Event ev, long calendarId) throws CalendarOrganizerException;
    public void updateEvent(Event ev) throws CalendarOrganizerException;
}
