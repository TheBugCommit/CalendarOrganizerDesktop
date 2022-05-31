/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
