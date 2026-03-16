package com.dpi.model;

import jakarta.persistence.*;
import java.time.Instant;

// ─── Flow ─────────────────────────────────────────────────────────────────────

@Entity
@Table(name = "flows")
public class Flow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sourceIp;
    private String destinationIp;
    private int sourcePort;
    private int destinationPort;
    private String transportProtocol;   // TCP / UDP
    private String applicationProtocol; // HTTP, TLS, DNS ...

    private long totalBytes;
    private long packetCount;

    private Instant startTime;
    private Instant lastSeen;

    @Enumerated(EnumType.STRING)
    private FlowState state;            // ACTIVE, CLOSED, TIMEOUT

    public enum FlowState { ACTIVE, CLOSED, TIMEOUT }

    public Flow() { this.startTime = Instant.now(); this.lastSeen = Instant.now(); }

    // Getters / Setters
    public Long getId() { return id; }
    public String getSourceIp() { return sourceIp; } public void setSourceIp(String v) { this.sourceIp = v; }
    public String getDestinationIp() { return destinationIp; } public void setDestinationIp(String v) { this.destinationIp = v; }
    public int getSourcePort() { return sourcePort; } public void setSourcePort(int v) { this.sourcePort = v; }
    public int getDestinationPort() { return destinationPort; } public void setDestinationPort(int v) { this.destinationPort = v; }
    public String getTransportProtocol() { return transportProtocol; } public void setTransportProtocol(String v) { this.transportProtocol = v; }
    public String getApplicationProtocol() { return applicationProtocol; } public void setApplicationProtocol(String v) { this.applicationProtocol = v; }
    public long getTotalBytes() { return totalBytes; } public void setTotalBytes(long v) { this.totalBytes = v; }
    public long getPacketCount() { return packetCount; } public void setPacketCount(long v) { this.packetCount = v; }
    public Instant getStartTime() { return startTime; }
    public Instant getLastSeen() { return lastSeen; } public void setLastSeen(Instant v) { this.lastSeen = v; }
    public FlowState getState() { return state; } public void setState(FlowState v) { this.state = v; }
}
