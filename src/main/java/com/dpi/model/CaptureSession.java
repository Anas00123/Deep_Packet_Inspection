package com.dpi.model;

import jakarta.persistence.*;
import java.time.Duration;
import java.time.Instant;

/**
 * Represents a single packet capture session on a network interface.
 * Tracks start/stop time, filter applied, and session-level stats.
 */
@Entity
@Table(name = "capture_sessions")
public class CaptureSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interface_id")
    private NetworkInterface networkInterface;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;          // STARTING, ACTIVE, STOPPED, ERROR

    private String bpfFilter;             // BPF filter expression e.g. "tcp port 80"
    private int snapLen;                  // snapshot length (bytes per packet)
    private boolean promiscuousMode;

    private long packetsCaptured;
    private long packetsDropped;
    private long bytesProcessed;

    private Instant startedAt;
    private Instant stoppedAt;

    private String stopReason;            // e.g. "USER_REQUEST", "ERROR", "TIMEOUT"
    private String errorMessage;

    public enum SessionStatus { STARTING, ACTIVE, STOPPED, ERROR }

    public CaptureSession() {
        this.status          = SessionStatus.STARTING;
        this.startedAt       = Instant.now();
        this.snapLen         = 65535;
        this.promiscuousMode = true;
    }

    public CaptureSession(NetworkInterface iface, String bpfFilter) {
        this();
        this.networkInterface = iface;
        this.bpfFilter        = bpfFilter;
    }

    /** Duration of this session in seconds. Returns 0 if still active. */
    public long getDurationSeconds() {
        Instant end = stoppedAt != null ? stoppedAt : Instant.now();
        return Duration.between(startedAt, end).getSeconds();
    }

    public void stop(String reason) {
        this.status     = SessionStatus.STOPPED;
        this.stoppedAt  = Instant.now();
        this.stopReason = reason;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Long getId()                              { return id; }
    public NetworkInterface getNetworkInterface()    { return networkInterface; }
    public void setNetworkInterface(NetworkInterface v) { this.networkInterface = v; }
    public SessionStatus getStatus()                 { return status; }
    public void setStatus(SessionStatus v)           { this.status = v; }
    public String getBpfFilter()                     { return bpfFilter; }
    public void setBpfFilter(String v)               { this.bpfFilter = v; }
    public int getSnapLen()                          { return snapLen; }
    public void setSnapLen(int v)                    { this.snapLen = v; }
    public boolean isPromiscuousMode()               { return promiscuousMode; }
    public void setPromiscuousMode(boolean v)        { this.promiscuousMode = v; }
    public long getPacketsCaptured()                 { return packetsCaptured; }
    public void setPacketsCaptured(long v)           { this.packetsCaptured = v; }
    public long getPacketsDropped()                  { return packetsDropped; }
    public void setPacketsDropped(long v)            { this.packetsDropped = v; }
    public long getBytesProcessed()                  { return bytesProcessed; }
    public void setBytesProcessed(long v)            { this.bytesProcessed = v; }
    public Instant getStartedAt()                    { return startedAt; }
    public Instant getStoppedAt()                    { return stoppedAt; }
    public String getStopReason()                    { return stopReason; }
    public String getErrorMessage()                  { return errorMessage; }
    public void setErrorMessage(String v)            { this.errorMessage = v; this.status = SessionStatus.ERROR; }
}
