package org.milaifontanals.models;

import java.util.Objects;

/**
 *
 * @author Gerard casas
 */
public class Category implements Cloneable {

    private long id;
   // private User user;
    private String name;

    public Category(long id, String name) {
        this.setId(id);
        //this.setUser(user);
        this.setName(name);
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    /*public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (user == null) {
            throw new RuntimeException("User can't be null");
        }
        this.user = user;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new RuntimeException("Name can't be null");
        }
        
        if(name.length() < 1 || name.length() > 30)
            throw new RuntimeException("Name length between 1 and 30");
        
        this.name = name;
    }

    @Override
    public Object clone() {
        return new Category(this.getId(), this.getName());
    }

    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", name=" + name + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final Category other = (Category) obj;
        if (this.id != other.getId()) {
            return false;
        }
        return true;
    }

}
