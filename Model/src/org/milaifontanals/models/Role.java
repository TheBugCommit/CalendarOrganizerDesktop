package org.milaifontanals.models;

import java.util.Objects;

/**
 *
 * @author Gerard Casas
 */
public enum Role {
    ADMIN(1, "Admin"),CUSTOMER(1, "Customer");
    
    private long id;
    private String name;
    
    private Role(long id, String name){
        this.id = id;
        this.name = name;
    }
    
    public long getId(){
        return this.id;
    }
    
    public String getName(){
        return this.name;
    }
}
