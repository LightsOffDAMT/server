package ru.lightsoff.server.channels.handlers.chains;

import ru.lightsoff.server.channels.transport.TransportPackage;

public interface ResourcefulChain<T> {
    void doNext(T resource);
}
