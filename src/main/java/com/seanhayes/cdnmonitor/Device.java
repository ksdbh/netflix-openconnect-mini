package com.seanhayes.cdnmonitor;

public class Device {
    private final String id;
    private String region;
    private String isp;
    private boolean online;
    private double cpu; // percent
    private double throughputMbps;

    public Device(String id, String region, String isp, boolean online, double cpu, double throughputMbps) {
        this.id = id;
        this.region = region;
        this.isp = isp;
        this.online = online;
        this.cpu = cpu;
        this.throughputMbps = throughputMbps;
    }

    public String getId() { return id; }
    public String getRegion() { return region; }
    public String getIsp() { return isp; }
    public boolean isOnline() { return online; }
    public double getCpu() { return cpu; }
    public double getThroughputMbps() { return throughputMbps; }

    public void setRegion(String region) { this.region = region; }
    public void setIsp(String isp) { this.isp = isp; }
    public void setOnline(boolean online) { this.online = online; }
    public void setCpu(double cpu) { this.cpu = cpu; }
    public void setThroughputMbps(double throughputMbps) { this.throughputMbps = throughputMbps; }
}
