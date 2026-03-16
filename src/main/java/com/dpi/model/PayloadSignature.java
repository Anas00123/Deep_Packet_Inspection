package com.dpi.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Stores payload byte-level signatures for known malware, exploits, or patterns.
 * Matched against raw packet payloads during DPI.
 */
@Entity
@Table(name = "payload_signatures")
public class PayloadSignature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String signatureId;           // e.g. "SIG-2024-0042"

    @Column(nullable = false)
    private String name;                  // human-readable name

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private SignatureType type;           // HEX_PATTERN, REGEX, YARA

    @Column(length = 4000)
    private String pattern;               // the actual signature data

    @Enumerated(EnumType.STRING)
    private ThreatClass threatClass;      // MALWARE, EXPLOIT, C2, RANSOMWARE, etc.

    @Enumerated(EnumType.STRING)
    private Alert.Severity severity;

    private String cveName;               // CVE reference if applicable
    private boolean enabled;
    private int hitCount;                 // number of matches in current session
    private Instant addedAt;

    public enum SignatureType { HEX_PATTERN, REGEX, YARA }

    public enum ThreatClass {
        MALWARE, EXPLOIT, COMMAND_AND_CONTROL, RANSOMWARE,
        PHISHING, DATA_EXFIL, LATERAL_MOVEMENT, RECONNAISSANCE
    }

    public PayloadSignature() {
        this.enabled = true;
        this.addedAt = Instant.now();
    }

    public PayloadSignature(String signatureId, String name,
                             SignatureType type, String pattern,
                             ThreatClass threatClass, Alert.Severity severity) {
        this();
        this.signatureId = signatureId;
        this.name        = name;
        this.type        = type;
        this.pattern     = pattern;
        this.threatClass = threatClass;
        this.severity    = severity;
    }

    public void recordHit() { this.hitCount++; }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Long getId()                          { return id; }
    public String getSignatureId()               { return signatureId; }
    public void setSignatureId(String v)         { this.signatureId = v; }
    public String getName()                      { return name; }
    public void setName(String v)                { this.name = v; }
    public String getDescription()               { return description; }
    public void setDescription(String v)         { this.description = v; }
    public SignatureType getType()               { return type; }
    public void setType(SignatureType v)         { this.type = v; }
    public String getPattern()                   { return pattern; }
    public void setPattern(String v)             { this.pattern = v; }
    public ThreatClass getThreatClass()          { return threatClass; }
    public void setThreatClass(ThreatClass v)    { this.threatClass = v; }
    public Alert.Severity getSeverity()          { return severity; }
    public void setSeverity(Alert.Severity v)    { this.severity = v; }
    public String getCveName()                   { return cveName; }
    public void setCveName(String v)             { this.cveName = v; }
    public boolean isEnabled()                   { return enabled; }
    public void setEnabled(boolean v)            { this.enabled = v; }
    public int getHitCount()                     { return hitCount; }
    public Instant getAddedAt()                  { return addedAt; }
}
