package ru.lightsoff.server.conntector;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import ru.lightsoff.server.engine.CycleRunner;
import ru.lightsoff.server.entities.Player;
import ru.lightsoff.server.security.Role;
import ru.lightsoff.server.security.SecurityContext;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.awt.*;
import java.time.Duration;
import java.util.*;


@Slf4j
public class SyncPlayerSynchronizer implements Runnable{
    private Duration timeout;
    private RestTemplate client = new RestTemplate();
    private String baseUrl = "http://localhost:9002";
    @Autowired private SecurityContext securityContext;
    @Autowired private CycleRunner cycleRunner;
    private Gson json = new Gson();

    public SyncPlayerSynchronizer(Duration timeout) {
        this.timeout = timeout;
    }


    @Override
    public void run() {
        while (true){
            long start = System.currentTimeMillis();

            class EntityConstructor{
                private Player player(LinkedHashMap map) throws RuntimeException{
                    Point point = new Point();
                    LinkedHashMap pointMap = Optional.ofNullable((LinkedHashMap)map.get("position")).orElse(new LinkedHashMap());
                    Object xObject = pointMap.get("x");
                    Object yObject = pointMap.get("y");
                    if(xObject == null || yObject == null)
                        point.x = point.y = 0;
                    else{
                        point.x = Double.valueOf(xObject.toString()).intValue();
                        point.y = Double.valueOf(yObject.toString()).intValue();
                    }

                    ArrayList<ArrayList<Integer>> inventory = (ArrayList<ArrayList<Integer>>)map.get("inventory");
                    ArrayList<Integer> stats = (ArrayList<Integer>)map.get("stats");
                    return new Player()
                            .withId(((Integer)map.get("id")).toString())
                            .withName((String)map.get("name"))
                            .withPoint(point.x, point.y)
                            .withInventory(inventory)
                            .withStats(stats)
                            .withUserID(((Integer)map.get("userID")).toString());
                }
            }
            EntityConstructor entityConstructor = new EntityConstructor();

            Objects.requireNonNull(client.getForEntity(baseUrl + "/get/player", ArrayList.class)
                    .getBody()).stream()
                    .map(o -> entityConstructor.player((LinkedHashMap)o))
                    .forEach(player -> {
                        if(securityContext.isNewUser(((Player) player).getUserID()))
                            securityContext.addUser(((Player) player).getUserID(), Role.USER);
                        if(cycleRunner.isNewPlayer(((Player) player).getId()))
                            cycleRunner.addPlayer(((Player) player).getUserID(), (Player)player);
                    });

            if(System.currentTimeMillis() - start < 15000) {
                try {
                    Thread.sleep(start + 15000 - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
