package ru.lightsoff.server.security;

public interface Credentials {
    String getCredential(String key);
    Credentials setCredential(String key, String value);
}
