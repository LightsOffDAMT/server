package ru.lightsoff.server.security;

import ru.lightsoff.server.channels.transport.TransportPackage;

public interface SecurityPackage<T> extends TransportPackage<T> {
    Credentials getCredentials();
}
