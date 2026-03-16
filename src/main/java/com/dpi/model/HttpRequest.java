package com.dpi.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a parsed HTTP/1.x request extracted from a packet payload.
 * Used for URL filtering, content inspection, and user-agent analysis.
 */
@Entity
@Table(name = "http_requests")
public class HttpRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packet_id")
    private Packet packet;

    private String method;             // GET, POST, PUT, DELETE, etc.
    private String url;
    private String host;
    private String path;
    private String queryString;
    private String httpVersion;        // HTTP/1.0, HTTP/1.1

    private String userAgent;
    private String referer;
    private String contentType;
    private int contentLength;

    @ElementCollection
    @CollectionTable(name = "http_headers", joinColumns = @JoinColumn(name = "http_req_id"))
    @MapKeyColumn(name = "header_name")
    @Column(name = "header_value")
    private Map<String, String> headers = new HashMap<>();

    @Column(length = 4000)
    private String requestBody;        // truncated body for inspection

    // DPI enrichment
    private boolean suspiciousUrl;
    private boolean maliciousPayload;
    private String urlCategory;        // MALWARE, PHISHING, ADULT, SOCIAL, etc.
    private int responseStatusCode;

    private Instant capturedAt;

    public HttpRequest() {
        this.capturedAt = Instant.now();
    }

    public HttpRequest(Packet packet, String method, String url, String host) {
        this();
        this.packet = packet;
        this.method = method;
        this.url    = url;
        this.host   = host;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Long getId()                          { return id; }
    public Packet getPacket()                    { return packet; }
    public void setPacket(Packet v)              { this.packet = v; }
    public String getMethod()                    { return method; }
    public void setMethod(String v)              { this.method = v; }
    public String getUrl()                       { return url; }
    public void setUrl(String v)                 { this.url = v; }
    public String getHost()                      { return host; }
    public void setHost(String v)                { this.host = v; }
    public String getPath()                      { return path; }
    public void setPath(String v)                { this.path = v; }
    public String getQueryString()               { return queryString; }
    public void setQueryString(String v)         { this.queryString = v; }
    public String getHttpVersion()               { return httpVersion; }
    public void setHttpVersion(String v)         { this.httpVersion = v; }
    public String getUserAgent()                 { return userAgent; }
    public void setUserAgent(String v)           { this.userAgent = v; }
    public String getReferer()                   { return referer; }
    public void setReferer(String v)             { this.referer = v; }
    public String getContentType()               { return contentType; }
    public void setContentType(String v)         { this.contentType = v; }
    public int getContentLength()                { return contentLength; }
    public void setContentLength(int v)          { this.contentLength = v; }
    public Map<String, String> getHeaders()      { return headers; }
    public void setHeaders(Map<String, String> v){ this.headers = v; }
    public String getRequestBody()               { return requestBody; }
    public void setRequestBody(String v)         { this.requestBody = v; }
    public boolean isSuspiciousUrl()             { return suspiciousUrl; }
    public void setSuspiciousUrl(boolean v)      { this.suspiciousUrl = v; }
    public boolean isMaliciousPayload()          { return maliciousPayload; }
    public void setMaliciousPayload(boolean v)   { this.maliciousPayload = v; }
    public String getUrlCategory()               { return urlCategory; }
    public void setUrlCategory(String v)         { this.urlCategory = v; }
    public int getResponseStatusCode()           { return responseStatusCode; }
    public void setResponseStatusCode(int v)     { this.responseStatusCode = v; }
    public Instant getCapturedAt()               { return capturedAt; }
}
