package org.milaifontanals.models;

import java.util.Objects;

/**
 *
 * @author Gerard Casas
 */
public class Target implements Cloneable {

    private Calendar calendar;
    private String email;

    public Target(Calendar calendar, String email) {
        this.setCalendar(calendar);
        this.setEmail(email);
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        if (calendar == null) {
            throw new RuntimeException("Calendar can't be null");
        }
        this.calendar = (Calendar) calendar.clone();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null) {
            throw new RuntimeException("Email can't be null");
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return "Target{" + "calendar=" + calendar + ", email=" + email + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.calendar);
        hash = 71 * hash + Objects.hashCode(this.email);
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
        final Target other = (Target) obj;
        if (!this.email.equals(other.email)) {
            return false;
        }
        if (!this.calendar.equals(other.getCalendar())) {
            return false;
        }
        return true;
    }

    @Override
    public Object clone() {
        return new Target(this.getCalendar(), this.getEmail());
    }
}
