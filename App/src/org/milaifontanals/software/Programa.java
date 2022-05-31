/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.software;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.milaifontanals.User;
import org.milaifontanals.persistencia.IProjecteDaw;
import org.milaifontanals.persistencia.ProjecteDawException;

/**
 *
 * @author Usuari
 */
public class Programa {
    public static void main(String[] args) {
        
        
        
        IProjecteDaw obj = null;
        /* Suposant que obj sigui un objecte de la capa que pertoqui en temps 
        d'execució...
        */
        String nomCapa = "org.milaifontanals.CapaBDR";     // Aquest nom s'ha d'obtenir
        try {
            // des de fitxer de propietats o via args. Mai per codi
            obj = (IProjecteDaw) Class.forName(nomCapa).newInstance();
            System.out.println("Capa carregada i en funcionament");
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            System.out.println("No es pot carregar capa per motiu.....");
            return; 
        } catch (ProjecteDawException ex) {
            System.out.println("Informar del motiu de petada...");
            return;
        }
        
        
        User u = new User();
        obj.insertUser(u);
        /* Mentre escrivim l'aplicació es pot invocar qualsevol dels mètodes
        definits a la interfície
        */
        
        
        
        
        
        
        // Abans de finalitzar l'aplicació
        obj.tancarCapa();
        
    }
}
