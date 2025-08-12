package com.seanhayes.cdnmonitor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api")
public class DeviceController {
    private final DeviceService service;
    public DeviceController(DeviceService service) { this.service = service; }

    @GetMapping("/devices")
    public Collection<Device> devices() {
        return service.all();
    }

    @GetMapping("/devices/{id}")
    public ResponseEntity<Device> device(@PathVariable String id) {
        return service.one(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/devices")
    public Device upsert(@RequestBody Device d) {
        return service.upsert(d);
    }
}
