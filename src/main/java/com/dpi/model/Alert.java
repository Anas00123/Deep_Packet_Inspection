package com.dpi.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Represents a security or anomaly alert triggered by DPI inspection.
 */
@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Severity severity;          // INFO, WARNING, CRITICAL

    @Enumerated(EnumType.STRING)
    private AlertStatus status;         // ACTIVE, ACKNOWLEDGED, RESOLVED

    private String sourceIp;
    private String destinationIp;
    private String detectedProtocol;
    private String ruleTriggered;       // name of DPI rule that fired

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packet_id")
    private Packet relatedPacket;

    private Instant createdAt;
    private Instant resolvedAt;

    public enum Severity { INFO, WARNING, CRITICAL }
    public enum AlertStatus { ACTIVE, ACKNOWLEDGED, RESOLVED }

    public Alert() {
        this.createdAt = Instant.now();
        this.status = AlertStatus.ACTIVE;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getTitle() { return title; } public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; } public void setDescription(String d) { this.description = d; }
    public Severity getSeverity() { return severity; } public void setSeverity(Severity severity) { this.severity = severity; }
    public AlertStatus getStatus() { return status; } public void setStatus(AlertStatus status) { this.status = status; }
    public String getSourceIp() { return sourceIp; } public void setSourceIp(String sourceIp) { this.sourceIp = sourceIp; }
    public String getDestinationIp() { return destinationIp; } public void setDestinationIp(String d) { this.destinationIp = d; }
    public String getDetectedProtocol() { return detectedProtocol; } public void setDetectedProtocol(String p) { this.detectedProtocol = p; }
    public String getRuleTriggered() { return ruleTriggered; } public void setRuleTriggered(String r) { this.ruleTriggered = r; }
    public Packet getRelatedPacket() { return relatedPacket; } public void setRelatedPacket(Packet p) { this.relatedPacket = p; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getResolvedAt() { return resolvedAt; } public void setResolvedAt(Instant t) { this.resolvedAt = t; }
}
