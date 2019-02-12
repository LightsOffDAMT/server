package ru.lightsoff.server.security;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.HashMap;



public class Base64Credentials implements Credentials {
    private HashMap<String, String> storage = new HashMap<>();


    @Override
    public String getCredential(String key) {
        return new String(
                Base64.decodeBase64(
                        storage.get(key)
                )
        );
    }

    @Override
    public Base64Credentials setCredential(String key, String value) {
        storage.put(key, Base64.encodeBase64String(value.getBytes()));
        return this;
    }

    public HashMap<String, String> getStorage(){
        return storage;
    }
}
