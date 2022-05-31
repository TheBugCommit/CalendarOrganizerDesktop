package org.milaifontanals.models;

import java.util.Objects;

/**
 *
 * @author Gerard Casas
 */
public class Nation implements Cloneable {
    private long id;
    private String code;
    private String name;

    public Nation(long id, String code, String name) {
        this.setId(id);
        this.setCode(code);
        this.setName(name);
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if(code == null)
            throw new RuntimeException("Code can't be null");
        if(code.length() != 2)
            throw new RuntimeException("Invalid code length");
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null)
            throw new RuntimeException("Name can't be null");
        if(name.length() < 1 || name.length() > 30)
            throw new RuntimeException("Name length between 1 and 30");

        this.name = name;
    }

    @Override
    public String toString() {
        return "Nation{" + "id=" + id + ", code=" + code + ", name=" + name + '}';
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final Nation other = (Nation) obj;
        if (this.id != other.getId()) {
            return false;
        }
        return true;
    }

    @Override
    public Object clone() {
        try {
            return (Nation) super.clone();
        } catch (CloneNotSupportedException ex) {
            return new Nation(this.getId(), this.getCode(), this.getName());
        }
    }
}
