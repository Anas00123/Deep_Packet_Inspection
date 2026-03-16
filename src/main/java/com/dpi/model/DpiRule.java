package com.dpi.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Represents a DPI inspection rule used to flag packets or flows.
 * Rules are loaded by the alert engine at runtime.
 */
@Entity
@Table(name = "dpi_rules")
public class DpiRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ruleName;           // e.g. "RULE_CLEARTEXT_FTP"

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private RuleType ruleType;         // SIGNATURE, ANOMALY, THRESHOLD, REGEX

    @Column(length = 2000)
    private String pattern;            // regex or signature string

    private String targetProtocol;     // protocol this rule applies to, null = all

    private int targetPort;            // 0 = any port

    @Enumerated(EnumType.STRING)
    private Alert.Severity alertSeverity;

    private boolean enabled;

    private int matchCount;            // how many times this rule has fired

    private Instant createdAt;
    private Instant updatedAt;

    public enum RuleType { SIGNATURE, ANOMALY, THRESHOLD, REGEX }

    public DpiRule() {
        this.enabled   = true;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public DpiRule(String ruleName, RuleType ruleType, String pattern,
                   String targetProtocol, Alert.Severity severity) {
        this();
        this.ruleName       = ruleName;
        this.ruleType       = ruleType;
        this.pattern        = pattern;
        this.targetProtocol = targetProtocol;
        this.alertSeverity  = severity;
    }

    public void incrementMatchCount() {
        this.matchCount++;
        this.updatedAt = Instant.now();
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Long getId()                         { return id; }
    public String getRuleName()                 { return ruleName; }
    public void setRuleName(String v)           { this.ruleName = v; }
    public String getDescription()              { return description; }
    public void setDescription(String v)        { this.description = v; }
    public RuleType getRuleType()               { return ruleType; }
    public void setRuleType(RuleType v)         { this.ruleType = v; }
    public String getPattern()                  { return pattern; }
    public void setPattern(String v)            { this.pattern = v; }
    public String getTargetProtocol()           { return targetProtocol; }
    public void setTargetProtocol(String v)     { this.targetProtocol = v; }
    public int getTargetPort()                  { return targetPort; }
    public void setTargetPort(int v)            { this.targetPort = v; }
    public Alert.Severity getAlertSeverity()    { return alertSeverity; }
    public void setAlertSeverity(Alert.Severity v) { this.alertSeverity = v; }
    public boolean isEnabled()                  { return enabled; }
    public void setEnabled(boolean v)           { this.enabled = v; }
    public int getMatchCount()                  { return matchCount; }
    public Instant getCreatedAt()               { return createdAt; }
    public Instant getUpdatedAt()               { return updatedAt; }
}
