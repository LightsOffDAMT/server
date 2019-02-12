package ru.lightsoff.server.entities;

import java.awt.*;

/**
 * ItemInGame entity
 */
public class ItemInGame {
    private Long id;
    private Long itemID;
    private Point position;

    public ItemInGame(){
        super();
    }

    public Long getId() {
        return id;
    }

    public Point getPosition() {
        return position;
    }

    public Long getItemID() {
        return itemID;
    }

    public ItemInGame withId(Long id){
        this.id = id;
        return this;
    }

    public ItemInGame withPosition(Point position){
        this.position = position;
        return this;
    }

    public ItemInGame withItemId(Long id){
        this.itemID = id;
        return this;
    }


}
