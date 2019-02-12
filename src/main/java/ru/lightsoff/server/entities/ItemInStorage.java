package ru.lightsoff.server.entities;

import java.util.HashMap;
import java.util.Optional;

/**
 * ItemInStorage entity
 */
public class    ItemInStorage {
    private Long id;
    private String name;
    private HashMap<String, String> properties = new HashMap<>();

    public ItemInStorage(){
        super();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public ItemInStorage withId(Long id){
        this.id = id;
        return this;
    }

    public ItemInStorage withName(String name){
        this.name = name;
        return this;
    }

    public ItemInStorage withProperties(HashMap<String, String> properties){
        this.properties = properties;
        return this;
    }

    public ItemInStorage withProperty(String key, String value){
        try {
            Optional.of(properties).orElseThrow(Exception::new).put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}
