package org.milaifontanals.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 *
 * @author Gerard Casas
 */
public class ReadProperties {

    private ArrayList<String> properties;
    private HashMap<String,String> propertiesReaded = new HashMap<String,String>();

    public ReadProperties(String file, ArrayList<String> properties) throws Exception  {
        setProperties(properties);

        Properties p = new Properties();
        try {
            p.load(new FileReader(file));
        } catch (IOException ex) {
            throw new Exception("Problem loading "+ file, ex.getCause());
        }

        properties.forEach((prop) -> {
            propertiesReaded.put(prop, p.getProperty(prop));
        });
    }

    public void setProperties(ArrayList<String> properties) {
        this.properties = properties;
    }
    
    public HashMap<String,String> getPropertiesReaded(){
        return this.propertiesReaded;
    }
}
