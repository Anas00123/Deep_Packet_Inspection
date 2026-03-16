package com.dpi.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Aggregated traffic statistics snapshot — persisted periodically.
 */
@Entity
@Table(name = "traffic_stats")
public class TrafficStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long totalPackets;
    private long totalBytes;
    private long droppedPackets;
    private double throughputMbps;
    private int packetsPerSecond;
    private int activeFlows;
    private int activeAlerts;

    @Column(name = "snapshot_time", nullable = false)
    private Instant snapshotTime;

    private String interfaceName;   // e.g. eth0, wlan0

    public TrafficStats() {
        this.snapshotTime = Instant.now();
    }

    public TrafficStats(long totalPackets, long totalBytes, double throughputMbps,
                        int packetsPerSecond, int activeFlows) {
        this.totalPackets     = totalPackets;
        this.totalBytes       = totalBytes;
        this.throughputMbps   = throughputMbps;
        this.packetsPerSecond = packetsPerSecond;
        this.activeFlows      = activeFlows;
        this.snapshotTime     = Instant.now();
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Long getId()                      { return id; }
    public long getTotalPackets()            { return totalPackets; }
    public void setTotalPackets(long v)      { this.totalPackets = v; }
    public long getTotalBytes()              { return totalBytes; }
    public void setTotalBytes(long v)        { this.totalBytes = v; }
    public long getDroppedPackets()          { return droppedPackets; }
    public void setDroppedPackets(long v)    { this.droppedPackets = v; }
    public double getThroughputMbps()        { return throughputMbps; }
    public void setThroughputMbps(double v)  { this.throughputMbps = v; }
    public int getPacketsPerSecond()         { return packetsPerSecond; }
    public void setPacketsPerSecond(int v)   { this.packetsPerSecond = v; }
    public int getActiveFlows()              { return activeFlows; }
    public void setActiveFlows(int v)        { this.activeFlows = v; }
    public int getActiveAlerts()             { return activeAlerts; }
    public void setActiveAlerts(int v)       { this.activeAlerts = v; }
    public Instant getSnapshotTime()         { return snapshotTime; }
    public void setSnapshotTime(Instant v)   { this.snapshotTime = v; }
    public String getInterfaceName()         { return interfaceName; }
    public void setInterfaceName(String v)   { this.interfaceName = v; }
}
