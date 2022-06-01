package org.milaifontanals.models;

/**
 *
 * @author g3cas
 */
public enum Gender {
    FEMALE("F", "Famale"), MALE("M", "Male"), OTHER("O", "Other");
    
    private String gender;
    private String name;
    
    private Gender(String gender, String name){
        this.gender = gender;
        this.name = name;
    }
    
    public String getGender(){
        return this.gender;
    }
    
    public String getName(){
        return this.name;
    }
}
