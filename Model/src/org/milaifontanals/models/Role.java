package org.milaifontanals.models;

import java.util.Objects;

/**
 *
 * @author Gerard Casas
 */
public enum Role {
    ADMIN(1),CUSTOMER(1);
    
    private long id;
    
    private Role(long id){
        this.id = id;
    }
    
    public long getId(){
        return this.id;
    }
}
