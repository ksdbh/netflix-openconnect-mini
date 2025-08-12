package com.seanhayes.cdnmonitor;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DeviceService {
    private final DeviceRepository repo = new DeviceRepository();
    private final MeterRegistry registry;

    public DeviceService(MeterRegistry registry) {
        this.registry = registry;
        seed();
        registerMetrics();
    }

    private void seed() {
        String[] regions = {"NA", "EU", "APAC"};
        String[] isps = {"ISP-A", "ISP-B", "ISP-C"};
        for (int i = 1; i <= 20; i++) {
            var d = new Device(
                "edge-" + i,
                regions[i % regions.length],
                isps[i % isps.length],
                ThreadLocalRandom.current().nextBoolean(),
                ThreadLocalRandom.current().nextDouble(5.0, 85.0),
                ThreadLocalRandom.current().nextDouble(50.0, 950.0)
            );
            repo.save(d);
        }
    }

    private void registerMetrics() {
        // Gauges for summary metrics
        Gauge.builder("cdn.devices.total", repo, r -> r.findAll().size())
                .description("Total number of edge devices").register(registry);

        Gauge.builder("cdn.devices.online", repo, r -> r.findAll().stream().filter(Device::isOnline).count())
                .description("Online devices").register(registry);

        Gauge.builder("cdn.devices.offline", repo, r -> r.findAll().stream().filter(d -> !d.isOnline()).count())
                .description("Offline devices").register(registry);

        Gauge.builder("cdn.devices.avg_cpu", repo,
                r -> r.findAll().stream().mapToDouble(Device::getCpu).average().orElse(0.0))
                .description("Average CPU across devices").register(registry);

        Gauge.builder("cdn.devices.avg_throughput_mbps", repo,
                r -> r.findAll().stream().mapToDouble(Device::getThroughputMbps).average().orElse(0.0))
                .description("Average throughput Mbps").register(registry);
    }

    public Collection<Device> all() { return repo.findAll(); }
    public Optional<Device> one(String id) { return repo.findById(id); }

    public Device upsert(Device d) {
        return repo.save(d);
    }

    public void randomizeSample() {
        // Randomly flip statuses to simulate a distributed, changing fleet
        repo.findAll().forEach(d -> {
            if (ThreadLocalRandom.current().nextDouble() < 0.2) {
                d.setOnline(!d.isOnline());
            }
            d.setCpu(Math.max(1.0, Math.min(99.0, d.getCpu() + ThreadLocalRandom.current().nextDouble(-10, 10))));
            d.setThroughputMbps(Math.max(10.0, Math.min(2000.0, d.getThroughputMbps() + ThreadLocalRandom.current().nextDouble(-200, 200))));
        });
    }
}
