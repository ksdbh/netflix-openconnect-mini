package com.seanhayes.cdnmonitor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DeviceRepository {
    private final Map<String, Device> store = new ConcurrentHashMap<>();

    public Collection<Device> findAll() {
        return store.values();
    }

    public Optional<Device> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public Device save(Device d) {
        store.put(d.getId(), d);
        return d;
    }
}
