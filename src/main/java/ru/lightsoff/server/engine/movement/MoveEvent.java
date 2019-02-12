package ru.lightsoff.server.engine.movement;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;

import javax.management.AttributeNotFoundException;

public class MoveEvent {
    private String className = MoveEvent.class.getName();
    private String id;
    private int STEP = 3;
    private int deltaX = 0;
    private int deltaY = 0;

    public MoveEvent(){
    }

    public MoveEvent(String dir, String id){
        this.id = id;
        switch (dir){
            case "LEFT":
                deltaX -= STEP;
                break;
            case "RIGHT":
                deltaX += STEP;
                break;
            case "DOWN":
                deltaY += STEP;
                break;
            case "UP":
                deltaY -= STEP;
                break;
        }
    }

    public MoveEvent(LinkedTreeMap<String, Object> tree) {
        id = (String)tree.get("id");
        STEP = ((Double) tree.get("STEP")).intValue();
        deltaX = ((Double) tree.get("deltaX")).intValue();
        deltaY = ((Double) tree.get("deltaY")).intValue();
    }

    public MoveEvent(String id, int STEP, int deltaX, int deltaY){
        this.id = id;
        this.STEP = STEP;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public MoveEvent(String id, int deltaX, int deltaY){
        this.id = id;
        this.STEP = STEP;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSTEP() {
        return STEP;
    }

    public void setSTEP(int STEP) {
        this.STEP = STEP;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public void setDeltaY(int deltaY) {
        this.deltaY = deltaY;
    }
}
