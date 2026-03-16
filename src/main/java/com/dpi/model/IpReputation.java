package com.dpi.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores threat-intelligence reputation data for an IP address.
 * Used during DPI to cross-reference source/destination IPs.
 */
@Entity
@Table(name = "ip_reputation")
public class IpReputation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    private ReputationScore score;         // CLEAN, SUSPICIOUS, MALICIOUS, KNOWN_BAD

    private String countryCode;            // ISO-3166 e.g. "IN", "US"
    private String asnNumber;             // Autonomous System Number
    private String asnOrg;                // ASN organisation name

    private boolean isTorExitNode;
    private boolean isVpn;
    private boolean isProxy;
    private boolean isDatacenter;

    @ElementCollection
    @CollectionTable(name = "ip_threat_categories",
                     joinColumns = @JoinColumn(name = "ip_rep_id"))
    @Column(name = "category")
    private List<String> threatCategories = new ArrayList<>();
    // e.g. ["BOTNET_CC", "SPAM_SOURCE", "BRUTE_FORCE"]

    private int abuseConfidenceScore;      // 0–100 from abuse DB
    private int reportCount;               // number of abuse reports

    private Instant firstSeen;
    private Instant lastUpdated;

    public enum ReputationScore { CLEAN, SUSPICIOUS, MALICIOUS, KNOWN_BAD }

    public IpReputation() {
        this.firstSeen   = Instant.now();
        this.lastUpdated = Instant.now();
        this.score       = ReputationScore.CLEAN;
    }

    public IpReputation(String ipAddress, ReputationScore score) {
        this();
        this.ipAddress = ipAddress;
        this.score     = score;
    }

    public boolean isThreat() {
        return score == ReputationScore.MALICIOUS || score == ReputationScore.KNOWN_BAD;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Long getId()                              { return id; }
    public String getIpAddress()                     { return ipAddress; }
    public void setIpAddress(String v)               { this.ipAddress = v; }
    public ReputationScore getScore()                { return score; }
    public void setScore(ReputationScore v)          { this.score = v; }
    public String getCountryCode()                   { return countryCode; }
    public void setCountryCode(String v)             { this.countryCode = v; }
    public String getAsnNumber()                     { return asnNumber; }
    public void setAsnNumber(String v)               { this.asnNumber = v; }
    public String getAsnOrg()                        { return asnOrg; }
    public void setAsnOrg(String v)                  { this.asnOrg = v; }
    public boolean isTorExitNode()                   { return isTorExitNode; }
    public void setTorExitNode(boolean v)            { this.isTorExitNode = v; }
    public boolean isVpn()                           { return isVpn; }
    public void setVpn(boolean v)                    { this.isVpn = v; }
    public boolean isProxy()                         { return isProxy; }
    public void setProxy(boolean v)                  { this.isProxy = v; }
    public boolean isDatacenter()                    { return isDatacenter; }
    public void setDatacenter(boolean v)             { this.isDatacenter = v; }
    public List<String> getThreatCategories()        { return threatCategories; }
    public void setThreatCategories(List<String> v)  { this.threatCategories = v; }
    public int getAbuseConfidenceScore()             { return abuseConfidenceScore; }
    public void setAbuseConfidenceScore(int v)       { this.abuseConfidenceScore = v; }
    public int getReportCount()                      { return reportCount; }
    public void setReportCount(int v)                { this.reportCount = v; }
    public Instant getFirstSeen()                    { return firstSeen; }
    public Instant getLastUpdated()                  { return lastUpdated; }
    public void setLastUpdated(Instant v)            { this.lastUpdated = v; }
}
