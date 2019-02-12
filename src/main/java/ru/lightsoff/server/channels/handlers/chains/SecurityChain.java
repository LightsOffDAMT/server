package ru.lightsoff.server.channels.handlers.chains;

public interface SecurityChain extends ResourcefulChain<Package> {
    void doNext(Package pack);
}
