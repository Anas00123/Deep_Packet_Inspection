package com.dpi.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Represents a raw captured network packet.
 */
@Entity
@Table(name = "packets")
public class Packet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sourceIp;

    @Column(nullable = false)
    private String destinationIp;

    private int sourcePort;
    private int destinationPort;

    @Column(nullable = false)
    private String protocol;       // TCP, UDP, ICMP

    private int length;            // total packet length in bytes

    @Lob
    private byte[] payload;        // raw payload bytes

    @Column(nullable = false)
    private Instant capturedAt;

    private String interfaceName;  // capture interface e.g. eth0

    // Constructors
    public Packet() {}

    public Packet(String sourceIp, String destinationIp, int sourcePort,
                  int destinationPort, String protocol, int length,
                  byte[] payload, Instant capturedAt) {
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.protocol = protocol;
        this.length = length;
        this.payload = payload;
        this.capturedAt = capturedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getSourceIp() { return sourceIp; }
    public void setSourceIp(String sourceIp) { this.sourceIp = sourceIp; }
    public String getDestinationIp() { return destinationIp; }
    public void setDestinationIp(String destinationIp) { this.destinationIp = destinationIp; }
    public int getSourcePort() { return sourcePort; }
    public void setSourcePort(int sourcePort) { this.sourcePort = sourcePort; }
    public int getDestinationPort() { return destinationPort; }
    public void setDestinationPort(int destinationPort) { this.destinationPort = destinationPort; }
    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }
    public byte[] getPayload() { return payload; }
    public void setPayload(byte[] payload) { this.payload = payload; }
    public Instant getCapturedAt() { return capturedAt; }
    public void setCapturedAt(Instant capturedAt) { this.capturedAt = capturedAt; }
    public String getInterfaceName() { return interfaceName; }
    public void setInterfaceName(String interfaceName) { this.interfaceName = interfaceName; }
}
