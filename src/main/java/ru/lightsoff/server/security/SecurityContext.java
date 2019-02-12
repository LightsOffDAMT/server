package ru.lightsoff.server.security;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class SecurityContext {
    private ConcurrentHashMap<String, Role> users = new ConcurrentHashMap<>();

    public Role getRole(String encodedId){
        return users.getOrDefault(encodedId, Role.NONE);
    }

    public void addUser(String encodedId, Role role){
        System.out.println(encodedId + " " + role);
        users.put(encodedId, role);
    }

    public boolean isNewUser(String id){
        System.out.println("check " + id);
        return users.get(id) == null;
    }
}
