package org.milaifontanals;

import org.milaifontanals.models.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.milaifontanals.models.Calendar;
import org.milaifontanals.models.Category;
import org.milaifontanals.models.Event;
import org.milaifontanals.models.Gender;
import org.milaifontanals.models.Nation;
import org.milaifontanals.models.Role;
import org.milaifontanals.persistencia.CalendarOrganizerException;
import org.milaifontanals.persistencia.ICalendarOrganizer;
import org.milaifontanals.utils.PasswordManager;
import org.milaifontanals.utils.ReadProperties;

public class BDRLayer implements ICalendarOrganizer {

    private Connection con;

    public BDRLayer() throws CalendarOrganizerException {

        HashMap<String, String> props = null;
        try {
            ArrayList<String> neededProps = new ArrayList<>() {
                {
                    add("db_url");
                    add("db_username");
                    add("db_password");
                }
            };

            props = new ReadProperties("env.properties", neededProps).getPropertiesReaded();

            for (String prop : neededProps) {
                if (props.get(prop) == null) {
                    throw new CalendarOrganizerException("Need property: " + prop);
                }
            }
        } catch (Exception ex) {
            throw new CalendarOrganizerException(ex.getMessage());
        }

        try {
            this.con = DriverManager.getConnection(props.get("db_url"), props.get("db_username"), props.get("db_password"));
            this.con.setAutoCommit(false);
            System.out.println("connexio establerta");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

            throw new CalendarOrganizerException("Error trying to connecto to database", ex);
        }
    }

    @Override
    public boolean checkAuth(String email, String password) throws CalendarOrganizerException {
        try {
            PreparedStatement ps = con.prepareStatement("select email, password from users where role_id = ? and email like ? and password like ?");
            ps.setLong(1, Role.ADMIN.getId());
            ps.setString(2, email);
            ps.setString(3, PasswordManager.hash(password));
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            throw new CalendarOrganizerException("An error ocurred can't check auth Error: " + ex.getMessage(), ex);
        }
    }

    @Override
    public User searchUserByEmail(String email) throws CalendarOrganizerException {
        User u = null;
        try {
            PreparedStatement ps = con.prepareStatement("select u.*, n.id as nation_id, n.code, n.name as nation_name "
                    + "from users u inner join nations n on u.nation_id = n.id "
                    + "where lower(u.email) = lower(?)");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                u = collectUser(rs);
            }
        } catch (SQLException ex) {
            throw new CalendarOrganizerException("An error occured can't search user", ex);
        }

        return u;
    }

    @Override
    public ArrayList<User> searchUserByNameSurname(String nameSurname) throws CalendarOrganizerException {
        ArrayList<User> u = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("select u.*, n.id as nation_id, n.code, n.name as nation_name "
                    + "from users u inner join nations n on u.nation_id = n.id "
                    + "where (lower(u.name) like concat('%',lower(?),'%') or "
                    + "lower(u.surname1) like concat('%',lower(?),'%') or "
                    + "lower(u.surname2) like concat('%',lower(?),'%'))");

            ps.setString(1, nameSurname);
            ps.setString(2, nameSurname);
            ps.setString(3, nameSurname);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                u.add(collectUser(rs));
            }
        } catch (SQLException ex) {
            throw new CalendarOrganizerException("An error occured can't search user", ex);
        }

        return u;
    }

    @Override
    public ArrayList<Calendar> getUserOwnerCalendars(long userId) throws CalendarOrganizerException {
        ArrayList<Calendar> calendars = new ArrayList<>();
        try {
            String sqlOwner = "select * from calendars c INNER join users u "
                    + "on u.id = c.user_id where u.id = ?";
            PreparedStatement psOwner = con.prepareStatement(sqlOwner);
            psOwner.setLong(1, userId);
            ResultSet rsOwner = psOwner.executeQuery();
            while (rsOwner.next()) {
                calendars.add(collectCalendar(rsOwner));
            }
        } catch (SQLException ex) {
            throw new CalendarOrganizerException("An error occured can't get user owner calendars", ex);
        }

        return calendars;
    }

    @Override
    public ArrayList<Calendar> getUserHelperCalendars(long userId) throws CalendarOrganizerException {
        ArrayList<Calendar> calendars = new ArrayList<>();
        try {
            String sqlHelper = "select * from calendars c INNER join calendar_user cu "
                    + "on c.id = cu.calendar_id INNER join users u on u.id = CU.user_id "
                    + "where u.id = ?";
            PreparedStatement psHelper = con.prepareStatement(sqlHelper);

            psHelper.setLong(1, userId);
            ResultSet rsHelper = psHelper.executeQuery();

            while (rsHelper.next()) {
                calendars.add(collectCalendar(rsHelper));
            }
        } catch (SQLException ex) {
            throw new CalendarOrganizerException("An error occured can't get user helper calendars", ex);
        }

        return calendars;
    }

    @Override
    public void commit() throws CalendarOrganizerException {
        try {
            con.commit();
        } catch (SQLException ex) {
            throw new CalendarOrganizerException("Error committing", ex);
        }
    }

    @Override
    public void rollBack() throws CalendarOrganizerException {
        try {
            con.rollback();
        } catch (SQLException ex) {
            throw new CalendarOrganizerException("Error doing rollback", ex);
        }
    }

    @Override
    public void close() throws CalendarOrganizerException {
        try {
            con.rollback();
            con.close();
        } catch (SQLException ex) {
            throw new CalendarOrganizerException("Error closing database connection", ex);
        }
    }

    public User collectUser(ResultSet rs) throws CalendarOrganizerException {
        User u = null;
        try {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String surname1 = rs.getString("surname1");
            String surname2 = rs.getString("surname2");
            boolean locked = rs.getBoolean("locked");
            Date birthDate = rs.getDate("birth_date");
            String phone = rs.getString("phone");
            String g = rs.getString("gender");
            Gender gender = g.equals(Gender.MALE.getGenderChar()) ? Gender.MALE
                    : g.equals(Gender.FEMALE.getGenderChar()) ? Gender.FEMALE : Gender.OTHER;
            Role role = rs.getLong("role_id") == Role.CUSTOMER.getId() ? Role.CUSTOMER : Role.ADMIN;
            Nation nation = new Nation(rs.getLong("nation_id"), rs.getString("code"), rs.getString("nation_name"));

            u = new User(id, name, email, surname1, surname2, locked, birthDate, gender, role, nation, phone);
        } catch (SQLException ex) {
            throw new CalendarOrganizerException(ex.getMessage(), ex);
        }

        return u;
    }

    @Override
    public Calendar collectCalendar(ResultSet rs) throws CalendarOrganizerException {
        Calendar c = null;
        try {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            String desc = rs.getString("description");
            Date start = rs.getDate("start_date");
            Date end = rs.getDate("end_date");
            c = new Calendar(id, title, start, end, desc);
        } catch (RuntimeException | SQLException ex) {
            throw new CalendarOrganizerException(ex.getMessage(), ex);
        }
        return c;
    }

    @Override
    public ArrayList<Nation> getNations() throws CalendarOrganizerException {
        ArrayList<Nation> nations = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("select * from nations");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                nations.add(new Nation(rs.getLong("id"), rs.getString("code"), rs.getString("name")));
            }
        } catch (SQLException ex) {
            throw new CalendarOrganizerException("Sorry can't get nations", ex);
        }

        return nations;
    }

    @Override
    public void updateUser(User user) throws CalendarOrganizerException {
        try {
            String sql = "update users set name=?, surname1=?, surname2=?, locked=?, "
                    + "birth_date=?, phone=?, gender=?, role_id=?, nation_id=? where "
                    + "id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2, user.getSurname1());
            ps.setString(3, user.getSurname2());
            ps.setBoolean(4, user.isLocked());
            ps.setDate(5, user.getBirthDate());
            ps.setString(6, user.getPhone());
            ps.setString(7, user.getGender().getGenderChar());
            ps.setLong(8, user.getRole().getId());
            ps.setLong(9, user.getNation().getId());
            ps.setLong(10, user.getId());

            ps.executeUpdate();
            commit();
        } catch (SQLException ex) {
            rollBack();
            throw new CalendarOrganizerException("Error updating user", ex);
        }
    }

    @Override
    public ArrayList<Event> getCalendarEvents(long calendarId) throws CalendarOrganizerException {
        ArrayList<Event> events = new ArrayList<Event>();
        try {
            String sql = "select e.id as event_id, e.calendar_id, e.category_id, e.title, e.description, "
                    + "e.location, e.published, e.color, e.start, e.end, "
                    + "c.id as cat_id, c.name as category_name, u.*, n.id as nation_id, n.code, n.name as nation_name "
                    + "from events e inner join categories c "
                    + "on c.id = e.category_id "
                    + "inner join users u on u.id = e.user_id "
                    + "inner join nations n on n.id = u.nation_id "
                    + "where e.calendar_id=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, calendarId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                events.add(collectEvent(rs));
            }
        } catch (SQLException ex) {
            throw new CalendarOrganizerException("Can't get calendar events", ex);
        }

        return events;
    }

    @Override
    public Event collectEvent(ResultSet rs) throws CalendarOrganizerException {
        User user = collectUser(rs);
        try {
            long id = rs.getLong("event_id");
            Category category = new Category(rs.getLong("cat_id"), rs.getString("category_name"));
            String title = rs.getString("title");
            String desc = rs.getString("description");
            String location = rs.getString("location");
            boolean published = rs.getBoolean("published");
            String color = rs.getString("color");
            Timestamp start = rs.getTimestamp("start");
            Timestamp end = rs.getTimestamp("end");
            return new Event(id, category, user, title, desc, location, published, color, start, end);
        } catch (SQLException | RuntimeException ex) {
            throw new CalendarOrganizerException(ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteEvent(long id) throws CalendarOrganizerException {
        try {
            String sql = "delete from events where id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
            commit();
        } catch (SQLException ex) {
            rollBack();
            throw new CalendarOrganizerException("Sorry can't delete this event", ex);
        }
    }

    @Override
    public boolean eventIsPublished(long id) throws CalendarOrganizerException {
        try {
            String sql = "select * from events where id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("");
            }
            boolean pub = rs.getBoolean("published");
            return pub;
        } catch (SQLException | RuntimeException ex) {
            throw new CalendarOrganizerException("Sorry can't check if event is published", ex);
        }
    }

    @Override
    public ArrayList<Category> getUserCategories(long userId) throws CalendarOrganizerException {
        ArrayList<Category> categories = new ArrayList<>();

        try {
            String sql = "select * from categories where user_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                categories.add(new Category(id, name));
            }

        } catch (SQLException ex) {
            throw new CalendarOrganizerException("Sorry can't get user categories", ex);
        }

        return categories;
    }

    @Override
    public void createEvent(Event ev, long calendarId) throws CalendarOrganizerException {
        try {
            String sql = "insert into events (calendar_id, category_id, user_id, title, description, location, published, color, start, end)"
                    + " values(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, calendarId);
            ps.setLong(2, ev.getCategory().getId());
            ps.setLong(3, ev.getUser().getId());
            ps.setString(4, ev.getTitle());
            ps.setString(5, ev.getDescription());
            ps.setString(6, ev.getLocation());
            ps.setBoolean(7, ev.isPublished());
            ps.setString(8, ev.getColor());
            ps.setTimestamp(9, ev.getStart());
            ps.setTimestamp(10, ev.getEnd());
            ps.executeUpdate();
            commit();
        } catch (SQLException ex) {
            rollBack();
            throw new CalendarOrganizerException("Sorry can't update event" + ex.getMessage());
        }
    }

    @Override
    public void updateEvent(Event ev) throws CalendarOrganizerException {
        try {
            String sql = "update events set title=?,description=?,location=?,color=?,start=?,end=?,category_id=? "
                    + "where id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, ev.getTitle());
            ps.setString(2, ev.getDescription());
            ps.setString(3, ev.getLocation());
            ps.setString(4, ev.getColor());
            ps.setTimestamp(5, ev.getStart());
            ps.setTimestamp(6, ev.getEnd());
            ps.setLong(7, ev.getCategory().getId());
            ps.setLong(8, ev.getId());
            ps.executeUpdate();
            commit();
        } catch (SQLException ex) {
            rollBack();
            throw new CalendarOrganizerException("Sorry can't update event" + ex.getMessage());
        }
    }

    @Override
    public ArrayList<Category> getUserCategoriesByEmail(String email) throws CalendarOrganizerException {
        ArrayList<Category> categories = new ArrayList<>();

        try {
            String sql = "select c.* from categories c inner join users u "
                    + "on u.id = c.user_id where u.email like ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                categories.add(new Category(id, name));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new CalendarOrganizerException("Sorry can't get user categories", ex);
        }

        return categories;
    }
}
