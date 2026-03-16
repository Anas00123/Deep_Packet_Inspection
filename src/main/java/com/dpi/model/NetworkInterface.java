package com.dpi.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Represents a physical or virtual network interface being monitored.
 */
@Entity
@Table(name = "network_interfaces")
public class NetworkInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;               // e.g. eth0, wlan0, lo

    private String displayName;
    private String macAddress;
    private String ipAddress;
    private String ipv6Address;
    private int mtu;                   // maximum transmission unit in bytes

    @Enumerated(EnumType.STRING)
    private InterfaceStatus status;    // UP, DOWN, MONITORING

    private boolean captureEnabled;
    private long bytesReceived;
    private long bytesSent;
    private long packetsReceived;
    private long packetsSent;
    private long errorCount;

    private Instant lastSeen;

    public enum InterfaceStatus { UP, DOWN, MONITORING }

    public NetworkInterface() {
        this.status         = InterfaceStatus.DOWN;
        this.captureEnabled = false;
        this.lastSeen       = Instant.now();
    }

    public NetworkInterface(String name, String ipAddress) {
        this();
        this.name      = name;
        this.ipAddress = ipAddress;
        this.status    = InterfaceStatus.UP;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Long getId()                          { return id; }
    public String getName()                      { return name; }
    public void setName(String v)                { this.name = v; }
    public String getDisplayName()               { return displayName; }
    public void setDisplayName(String v)         { this.displayName = v; }
    public String getMacAddress()                { return macAddress; }
    public void setMacAddress(String v)          { this.macAddress = v; }
    public String getIpAddress()                 { return ipAddress; }
    public void setIpAddress(String v)           { this.ipAddress = v; }
    public String getIpv6Address()               { return ipv6Address; }
    public void setIpv6Address(String v)         { this.ipv6Address = v; }
    public int getMtu()                          { return mtu; }
    public void setMtu(int v)                    { this.mtu = v; }
    public InterfaceStatus getStatus()           { return status; }
    public void setStatus(InterfaceStatus v)     { this.status = v; }
    public boolean isCaptureEnabled()            { return captureEnabled; }
    public void setCaptureEnabled(boolean v)     { this.captureEnabled = v; }
    public long getBytesReceived()               { return bytesReceived; }
    public void setBytesReceived(long v)         { this.bytesReceived = v; }
    public long getBytesSent()                   { return bytesSent; }
    public void setBytesSent(long v)             { this.bytesSent = v; }
    public long getPacketsReceived()             { return packetsReceived; }
    public void setPacketsReceived(long v)       { this.packetsReceived = v; }
    public long getPacketsSent()                 { return packetsSent; }
    public void setPacketsSent(long v)           { this.packetsSent = v; }
    public long getErrorCount()                  { return errorCount; }
    public void setErrorCount(long v)            { this.errorCount = v; }
    public Instant getLastSeen()                 { return lastSeen; }
    public void setLastSeen(Instant v)           { this.lastSeen = v; }
}
