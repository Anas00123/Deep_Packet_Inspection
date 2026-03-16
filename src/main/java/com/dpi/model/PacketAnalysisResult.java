package com.dpi.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Map;
import java.util.HashMap;

/**
 * Result of deep packet inspection on a single packet.
 */
@Entity
@Table(name = "analysis_results")
public class PacketAnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packet_id")
    private Packet packet;

    private String detectedProtocol;       // Application layer protocol e.g. HTTP, TLS, DNS
    private String applicationName;        // e.g. "YouTube", "WhatsApp"
    private boolean suspicious;
    private boolean encrypted;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;           // LOW, MEDIUM, HIGH, CRITICAL

    private String riskReason;

    @ElementCollection
    @CollectionTable(name = "result_metadata", joinColumns = @JoinColumn(name = "result_id"))
    @MapKeyColumn(name = "meta_key")
    @Column(name = "meta_value")
    private Map<String, String> metadata = new HashMap<>();

    private Instant analyzedAt;

    public enum RiskLevel { LOW, MEDIUM, HIGH, CRITICAL }

    public PacketAnalysisResult() {
        this.analyzedAt = Instant.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public Packet getPacket() { return packet; }
    public void setPacket(Packet packet) { this.packet = packet; }
    public String getDetectedProtocol() { return detectedProtocol; }
    public void setDetectedProtocol(String detectedProtocol) { this.detectedProtocol = detectedProtocol; }
    public String getApplicationName() { return applicationName; }
    public void setApplicationName(String applicationName) { this.applicationName = applicationName; }
    public boolean isSuspicious() { return suspicious; }
    public void setSuspicious(boolean suspicious) { this.suspicious = suspicious; }
    public boolean isEncrypted() { return encrypted; }
    public void setEncrypted(boolean encrypted) { this.encrypted = encrypted; }
    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }
    public String getRiskReason() { return riskReason; }
    public void setRiskReason(String riskReason) { this.riskReason = riskReason; }
    public Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }
    public Instant getAnalyzedAt() { return analyzedAt; }
}
