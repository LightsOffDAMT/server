package ru.lightsoff.server.channels.transport;

import java.util.Optional;

public interface TransportPackage<T> {
    Optional<T> getBody();
}
