package ru.lightsoff.server.entities;

import java.util.ArrayList;

/**
 * GameMap Entity
 */
public class GameMap {
    private Long id;
    private String name;
    private ArrayList<ArrayList<Double>> map;

    public GameMap(){
        super();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ArrayList<Double>> getMap() {
        return map;
    }

    public GameMap withId(Long id){
        this.id = id;
        return this;
    }

    public GameMap withName(String name){
        this.name = name;
        return this;
    }

    public GameMap withMap(ArrayList<ArrayList<Double>> map){
        this.map = map;
        return this;
    }
}
