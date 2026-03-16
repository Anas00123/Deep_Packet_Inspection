package com.dpi.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents metadata from a TLS/SSL handshake extracted via passive DPI.
 * Enables JA3 fingerprinting, certificate inspection, and cipher analysis.
 */
@Entity
@Table(name = "tls_handshakes")
public class TlsHandshake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packet_id")
    private Packet packet;

    private String tlsVersion;            // TLS 1.0, 1.1, 1.2, 1.3
    private String serverName;            // SNI (Server Name Indication)

    // JA3 fingerprint — MD5 of Client Hello fields
    private String ja3Hash;
    private String ja3sHash;              // server-side JA3S

    private String selectedCipherSuite;   // negotiated cipher

    @ElementCollection
    @CollectionTable(name = "tls_cipher_suites",
                     joinColumns = @JoinColumn(name = "tls_id"))
    @Column(name = "cipher_suite")
    private List<String> offeredCipherSuites = new ArrayList<>();

    // Certificate fields (from server cert)
    private String certSubject;
    private String certIssuer;
    private String certSerialNumber;
    private Instant certNotBefore;
    private Instant certNotAfter;
    private boolean certSelfSigned;
    private boolean certExpired;

    @Enumerated(EnumType.STRING)
    private TlsRisk tlsRisk;

    private boolean weakCipherUsed;
    private boolean deprecatedVersion;   // TLS < 1.2

    private Instant observedAt;

    public enum TlsRisk { SECURE, WEAK, DEPRECATED, SUSPICIOUS }

    public TlsHandshake() {
        this.observedAt = Instant.now();
    }

    public TlsHandshake(Packet packet, String tlsVersion, String serverName) {
        this();
        this.packet     = packet;
        this.tlsVersion = tlsVersion;
        this.serverName = serverName;
    }

    public boolean isCertificateValid() {
        if (certExpired || certSelfSigned) return false;
        Instant now = Instant.now();
        return certNotBefore != null && certNotAfter != null
                && now.isAfter(certNotBefore) && now.isBefore(certNotAfter);
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Long getId()                                   { return id; }
    public Packet getPacket()                             { return packet; }
    public void setPacket(Packet v)                       { this.packet = v; }
    public String getTlsVersion()                         { return tlsVersion; }
    public void setTlsVersion(String v)                   { this.tlsVersion = v; }
    public String getServerName()                         { return serverName; }
    public void setServerName(String v)                   { this.serverName = v; }
    public String getJa3Hash()                            { return ja3Hash; }
    public void setJa3Hash(String v)                      { this.ja3Hash = v; }
    public String getJa3sHash()                           { return ja3sHash; }
    public void setJa3sHash(String v)                     { this.ja3sHash = v; }
    public String getSelectedCipherSuite()                { return selectedCipherSuite; }
    public void setSelectedCipherSuite(String v)          { this.selectedCipherSuite = v; }
    public List<String> getOfferedCipherSuites()          { return offeredCipherSuites; }
    public void setOfferedCipherSuites(List<String> v)    { this.offeredCipherSuites = v; }
    public String getCertSubject()                        { return certSubject; }
    public void setCertSubject(String v)                  { this.certSubject = v; }
    public String getCertIssuer()                         { return certIssuer; }
    public void setCertIssuer(String v)                   { this.certIssuer = v; }
    public String getCertSerialNumber()                   { return certSerialNumber; }
    public void setCertSerialNumber(String v)             { this.certSerialNumber = v; }
    public Instant getCertNotBefore()                     { return certNotBefore; }
    public void setCertNotBefore(Instant v)               { this.certNotBefore = v; }
    public Instant getCertNotAfter()                      { return certNotAfter; }
    public void setCertNotAfter(Instant v)                { this.certNotAfter = v; }
    public boolean isCertSelfSigned()                     { return certSelfSigned; }
    public void setCertSelfSigned(boolean v)              { this.certSelfSigned = v; }
    public boolean isCertExpired()                        { return certExpired; }
    public void setCertExpired(boolean v)                 { this.certExpired = v; }
    public TlsRisk getTlsRisk()                           { return tlsRisk; }
    public void setTlsRisk(TlsRisk v)                     { this.tlsRisk = v; }
    public boolean isWeakCipherUsed()                     { return weakCipherUsed; }
    public void setWeakCipherUsed(boolean v)              { this.weakCipherUsed = v; }
    public boolean isDeprecatedVersion()                  { return deprecatedVersion; }
    public void setDeprecatedVersion(boolean v)           { this.deprecatedVersion = v; }
    public Instant getObservedAt()                        { return observedAt; }
}
