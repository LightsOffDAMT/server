package ru.lightsoff.server.engine;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Point;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.lightsoff.server.channels.handlers.GameResponseHandler;
import ru.lightsoff.server.engine.movement.MoveEvent;
import ru.lightsoff.server.entities.GameMap;
import ru.lightsoff.server.entities.Player;
import rx.Observable;
import rx.functions.Func0;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class CycleRunner implements Runnable{
    @Autowired
    private GameResponseHandler responseHandler;
    private ConcurrentLinkedQueue<MoveEvent> queue = new ConcurrentLinkedQueue<>();
    private AtomicBoolean interrupted = new AtomicBoolean(false);
    private ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();
    private RTree<Player, Geometry> map = RTree.create();
    private int CYCLE_SIZE = 5;

    public CycleRunner(int CYCLE_SIZE){
        this.CYCLE_SIZE = CYCLE_SIZE;
    }

    public boolean newMove(MoveEvent moveEvent){
        System.out.println("Move received: " + moveEvent.getId());
        return  queue.add(moveEvent);
    }

    public boolean isNewPlayer(String id){
        return players.get(id) == null;
    }

    public boolean addPlayer(String id, Player player){
        players.put(id, player);
        map = map.add(player, player.getPoint());
        return true;
    }

    public Observable<Entry<Player, Geometry>> searchInRadius(Point point, float radius){
        return map.search(point, radius);
    }

    public void refreshForPlayerById(String id){
        Player player = players.get(id);
        searchInRadius(player.getPoint(), 40).collect((Func0<ArrayList>) ArrayList::new, ArrayList::add)
        .map(l -> new Gson().toJson(l))
        .forEach(m -> responseHandler.message(id, m));
    }

    @Override
    public void run() {
        for(int i = 0; i < CYCLE_SIZE && !interrupted.get() && !queue.isEmpty(); i++){
            MoveEvent currentMove = queue.poll();
            Player toMove = players.get(currentMove.getId());
            System.out.println(currentMove.getId() + " " + currentMove.getDeltaX() + ":" + currentMove.getDeltaY());
            if(toMove == null)
                toMove = new Player();
            toMove.move(currentMove.getDeltaX(), currentMove.getDeltaY());
        }
    }
}
