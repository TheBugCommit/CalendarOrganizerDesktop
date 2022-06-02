package org.milaifontanals.models;

import java.sql.Timestamp;
import java.util.Objects;

/**
 *
 * @author Gerard Casas
 */
public class Event implements Cloneable {

    private long id;
    private Category category;
    private User user;
    private String title;
    private String description;
    private String location;
    private boolean published;
    private String color;
    private Timestamp start;
    private Timestamp end;

    public Event(long id, Category category, User user,
            String title, String description, String location,
            boolean published, String color, Timestamp start, Timestamp end) {
        this.setId(id);
        this.setCategory(category);
        this.setUser(user);
        this.setTitle(title);
        this.setDescription(description);
        this.setLocation(location);
        this.setPublished(published);
        this.setColor(color);
        this.setStart(start);
        this.setEnd(end);
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    /*public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        if (calendar == null) {
            throw new RuntimeException("Calendar can't be null");
        }
        this.calendar = calendar;
    }*/

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (category == null) {
            throw new RuntimeException("Category can't be null");
        }
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (user == null) {
            throw new RuntimeException("User can't be null");
        }
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null) {
            throw new RuntimeException("Title can't be null");
        }
        if (title.length() < 1 || title.length() > 30) {
            throw new RuntimeException("Title lenght between 1 and 30");
        }
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            throw new RuntimeException("Description can't be null");
        }
        if (description.length() < 1 || description.length() > 1000) {
            throw new RuntimeException("Description lenght between 1 and 1000");
        }
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location == null) {
            throw new RuntimeException("Location can't be null");
        }
        if (location.length() < 1 || location.length() > 1000) {
            throw new RuntimeException("Location lenght between 1 and 300");
        }
        this.location = location;
    }

    public boolean isPublished() {
        return published;
    }

    private void setPublished(boolean published) {
        this.published = published;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if (color == null) {
            throw new RuntimeException("Color can't be null");
        }
        if (color.length() != 7) {
            throw new RuntimeException("Color lenght must be 7");
        }
        this.color = color;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        if (start == null) {
            throw new RuntimeException("Start can't be null");
        }

        if (this.end != null && (start.after(this.end) || start.equals(this.end))) {
            throw new RuntimeException("Start can't be greather or equal than end");
        }

        this.start = (Timestamp) start.clone();
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        if (end == null) {
            throw new RuntimeException("End can't be null");
        }
        
        if(this.start != null && (end.before(this.start) || end.equals(this.start))){
            throw new RuntimeException("End can't be less or equal than start");
        }

        this.end = (Timestamp) end.clone();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Event other = (Event) obj;
        if (this.id != other.getId()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Event{" + "id=" + id + ", category=" + category + ", user=" + user + ", title=" + title + ", description=" + description + ", location=" + location + ", published=" + published + ", color=" + color + ", start=" + start + ", end=" + end + '}';
    }

    @Override
    public Object clone() {
        return new Event(this.getId(),this.getCategory(), this.getUser(), this.getTitle(),
                this.getDescription(), this.getLocation(),
                this.isPublished(), this.getColor(), this.getStart(), this.getEnd());
    }
}
