package org.milaifontanals.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Gerard Casas
 */
public class User implements Cloneable {

    private long id;
    private String name;
    private String email;
    private String surname1;
    private String surname2;
    private boolean locked;
    private Date birthDate;
    private String phone;
    private Gender gender;
    private Role role;
    private Nation nation;
    
    private ArrayList<Calendar> helperCalendars = new ArrayList<>();
    private ArrayList<Calendar> ownerCalendars = new ArrayList<>();


    public User(long id, String name, String email,
            String surname1, String surname2, boolean locked, Date birthDate,
            Gender gender, Role role, Nation nation, String phone) {
        this.setId(id);
        this.setName(name);
        this.setEmail(email);
        this.setSurname1(surname1);
        this.setSurname2(surname2);
        this.setLocked(locked);
        this.setBirthDate(birthDate);
        this.setPhone(phone);
        this.setGender(gender);
        this.setRole(role);
        this.setNation(nation);
    }

    public User(long id, String name, String email,
            String surname1, String surname2, boolean locked, Date birthDate,
            Gender gender, Role role, Nation nation) {
        this.setId(id);
        this.setName(name);
        this.setEmail(email);
        this.setSurname1(surname1);
        this.setSurname2(surname2);
        this.setLocked(locked);
        this.setBirthDate(birthDate);
        this.setGender(gender);
        this.setRole(role);
        this.setNation(nation);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        if (role == null) {
            throw new RuntimeException("Role can't be null");
        }
        this.role = role;
    }

    public Nation getNation() {
        return nation;
    }

    public void setNation(Nation nation) {
        if (nation == null) {
            throw new RuntimeException("Nation can't be null");
        }
        this.nation = (Nation) nation.clone();
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

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

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        if (email == null) {
            throw new RuntimeException("Email can't be null");
        }
        
        if(email.length() < 1 || email.length() > 100)
            throw new RuntimeException("Email length between 1 and 100");
        
        this.email = email;
    }

    public String getSurname1() {
        return surname1;
    }

    public void setSurname1(String surname1) {
        if (surname1 == null) {
            throw new RuntimeException("Surname1 can't be null");
        }
        
        if(surname1.length() < 1 || surname1.length() > 30)
            throw new RuntimeException("Surname1 length between 1 and 30");
        
        this.surname1 = surname1;
    }

    public String getSurname2() {
        return surname2;
    }

    public void setSurname2(String surname2) {
        if (surname2 == null) {
            throw new RuntimeException("Surname2 can't be null");
        }
        
        
        if(surname2.length() < 1 || surname2.length() > 30)
            throw new RuntimeException("Surname2 length between 1 and 30");
        
        this.surname2 = surname2;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        if (birthDate == null) {
            throw new RuntimeException("birthDate can't be null");
        }
        this.birthDate = (Date) birthDate.clone();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        if (gender == null) {
            throw new RuntimeException("Gender can't be null");
        }
        this.gender = gender;
    }
    
    public void addOwnerCalendar(Calendar c){
        if(c == null)
            throw new RuntimeException("Calendar can't be null");
        
        this.ownerCalendars.add(c);
    }
    
    public void addHelperCalendar(Calendar c){
        if(c == null)
            throw new RuntimeException("Calendar can't be null");
        
        this.helperCalendars.add(c);
    }
    
    public ArrayList<Calendar> getOwnerCalendars(){
        return (ArrayList<Calendar>) this.ownerCalendars.clone();
    }
    
    public ArrayList<Calendar> getHelCalendars(){
        return (ArrayList<Calendar>) this.helperCalendars.clone();
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
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
        final User other = (User) obj;
        if (this.id != other.getId()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return email + " " + name;
    }

    @Override
    public Object clone() {
        return new User(this.getId(), this.getName(), this.getEmail(),
                this.getSurname1(), this.getSurname2(), this.isLocked(), this.getBirthDate(),
                this.getGender(), this.getRole(), this.getNation());
    }
}
