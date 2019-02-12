package ru.lightsoff.server.security;

import com.sun.corba.se.spi.monitoring.StatisticsAccumulator;
import javafx.util.Pair;

import java.util.Optional;
import java.util.stream.Collectors;

public class Base64SecurityPackage<T> implements SecurityPackage<T> {
    private Base64Credentials credentials = new Base64Credentials();
    private T body;

    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public Optional<T> getBody() {
        return Optional.of(body);
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString(){
        return credentials.getStorage().keySet()
                .stream()
                .map(key -> new Pair<String, String>(key, credentials.getStorage().get(key)).toString())
                .collect(Collectors.joining("\n"));
    }
}
