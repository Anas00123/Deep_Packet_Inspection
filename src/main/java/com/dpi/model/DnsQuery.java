package com.dpi.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a parsed DNS query/response extracted from a packet.
 * Enables DNS-layer DPI for tunnelling and data exfiltration detection.
 */
@Entity
@Table(name = "dns_queries")
public class DnsQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packet_id")
    private Packet packet;

    private String queryName;             // e.g. "www.example.com"
    private String queryType;             // A, AAAA, MX, TXT, CNAME, etc.
    private int transactionId;
    private boolean isResponse;

    @ElementCollection
    @CollectionTable(name = "dns_answers", joinColumns = @JoinColumn(name = "dns_query_id"))
    @Column(name = "answer")
    private List<String> answers = new ArrayList<>();  // resolved IPs or values

    private int responseTtl;
    private int responseCode;            // 0=NOERROR, 2=SERVFAIL, 3=NXDOMAIN

    // DPI enrichment
    private boolean suspiciousDomain;
    private boolean dnsExfiltrationSuspected;  // long subdomain entropy check
    private double entropyScore;               // Shannon entropy of subdomain labels
    private String domainCategory;            // MALWARE, CDN, SOCIAL, etc.

    private Instant queriedAt;

    public DnsQuery() {
        this.queriedAt = Instant.now();
    }

    public DnsQuery(Packet packet, String queryName, String queryType) {
        this();
        this.packet    = packet;
        this.queryName = queryName;
        this.queryType = queryType;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Long getId()                                  { return id; }
    public Packet getPacket()                            { return packet; }
    public void setPacket(Packet v)                      { this.packet = v; }
    public String getQueryName()                         { return queryName; }
    public void setQueryName(String v)                   { this.queryName = v; }
    public String getQueryType()                         { return queryType; }
    public void setQueryType(String v)                   { this.queryType = v; }
    public int getTransactionId()                        { return transactionId; }
    public void setTransactionId(int v)                  { this.transactionId = v; }
    public boolean isResponse()                          { return isResponse; }
    public void setResponse(boolean v)                   { this.isResponse = v; }
    public List<String> getAnswers()                     { return answers; }
    public void setAnswers(List<String> v)               { this.answers = v; }
    public int getResponseTtl()                          { return responseTtl; }
    public void setResponseTtl(int v)                    { this.responseTtl = v; }
    public int getResponseCode()                         { return responseCode; }
    public void setResponseCode(int v)                   { this.responseCode = v; }
    public boolean isSuspiciousDomain()                  { return suspiciousDomain; }
    public void setSuspiciousDomain(boolean v)           { this.suspiciousDomain = v; }
    public boolean isDnsExfiltrationSuspected()          { return dnsExfiltrationSuspected; }
    public void setDnsExfiltrationSuspected(boolean v)   { this.dnsExfiltrationSuspected = v; }
    public double getEntropyScore()                      { return entropyScore; }
    public void setEntropyScore(double v)                { this.entropyScore = v; }
    public String getDomainCategory()                    { return domainCategory; }
    public void setDomainCategory(String v)              { this.domainCategory = v; }
    public Instant getQueriedAt()                        { return queriedAt; }
}
