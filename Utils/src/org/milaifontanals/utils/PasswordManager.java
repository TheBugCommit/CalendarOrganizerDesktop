package org.milaifontanals.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Gerard Casas
 */
public class PasswordManager {
    private static final String SALT = "$2a$10$8lMtqu7E3veYGcm1bHId5u";
                    
    public static String hash(String password){
        return BCrypt.hashpw(password, SALT);
    }
    
    public static boolean check(String password, String hash){
        return hash(password).equals(hash);
    }
}