package com.seanhayes.cdnmonitor;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class FleetTicker {
    private final DeviceService service;
    public FleetTicker(DeviceService service) { this.service = service; }

    // Update every 30 seconds
    @Scheduled(fixedRate = 30000L)
    public void tick() {
        service.randomizeSample();
    }
}
