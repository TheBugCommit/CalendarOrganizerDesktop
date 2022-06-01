package org.milaifontanals.models;

import java.util.ArrayList;
import java.sql.Date;
import java.util.Objects;

/**
 *
 * @author Gerard Casas
 */
public class Calendar implements Cloneable {

    private long id;
    //private User owner;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    
    public Calendar(long id, String title, Date startDate, Date endDate, String description) {
        this.setId(id);
        //this.setOwner(owner);
        this.setTitle(title);
        this.setDescription(description);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
    }
    
    public Calendar(long id, String title, Date startDate, Date endDate) {
        this.setId(id);
        //this.setOwner(owner);
        this.setTitle(title);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    /*public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        if (owner == null) {
            throw new RuntimeException("Owner can't be null");
        }
        this.owner = owner;
    }*/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null) {
            throw new RuntimeException("Title can't be null");
        }
        
        if(title.length() < 1 || title.length() > 30)
            throw new RuntimeException("Title length between 1 and 30");
        
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if(description != null && description.length() > 200)
            throw new RuntimeException("Description length between 1 and 200");
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        if (startDate == null) {
            throw new RuntimeException("Start date can't be null");
        }
        
        if(this.endDate != null && 
           (startDate.after(this.endDate) || startDate.equals(this.endDate))){
            throw new RuntimeException("Start date must be before end date");
        }
        
        this.startDate = (Date) startDate.clone();
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (endDate == null) {
            throw new RuntimeException("End date can't be null");
        }
        
        if(this.startDate != null && 
           (endDate.before(this.startDate) || endDate.equals(this.startDate))){
            throw new RuntimeException("End date must be after start date");
        }
        
        this.endDate = (Date) endDate.clone();
    }
    
    @Override
    public Object clone() {
        return new Calendar(this.getId(), this.getTitle(),
                this.getStartDate(), this.getEndDate(), this.getDescription());
    }

    @Override
    public String toString() {
        return "Calendar{" + "id=" + id + ", title=" + title + ", description=" + description + ", startDate=" + startDate + ", endDate=" + endDate + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Calendar other = (Calendar) obj;
        if (this.id != other.getId()) {
            return false;
        }
        return true;
    }

}
