package ru.lightsoff.server.entities;

import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;

import java.awt.*;
import java.util.ArrayList;

/**
 * Player entity
 */
public class Player {
    private String id;
    private String name;
    private ArrayList<ArrayList<Integer>> inventory;
    private ArrayList<Integer> stats;
    private String userID;
    private int x = 0;
    private int y = 0;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ArrayList<Integer>> getInventory() {
        return inventory;
    }

    public ArrayList<Integer> getStats() {
        return stats;
    }

    public String getUserID() {
        return userID;
    }


    public void move(int dx, int dy){
        x += dx;
        y += dy;
    }

    public Player withId(String id) {
        this.id = id;
        return this;
    }

    public Player withName(String name) {
        this.name = name;
        return this;
    }



    public Player withUserID(String userID) {
        this.userID = userID;
        return this;
    }

    public Player withInventory(ArrayList<ArrayList<Integer>> inventory) {
        this.inventory = inventory;
        return this;
    }

    public Player withStats(ArrayList<Integer> stats) {
        this.stats = stats;
        return this;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setInventory(ArrayList<ArrayList<Integer>> inventory) {
        this.inventory = inventory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStats(ArrayList<Integer> stats) {
        this.stats = stats;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Point getPoint(){
        return Geometries.point(x, y);
    }

    public Player withPoint(int x, int y){
        this.x = x;
        this.y = y;
        return this;
    }
}
