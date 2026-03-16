package com.dpi.model;

import java.util.Map;
import java.util.HashMap;

/**
 * Represents a parsed application-layer protocol extracted from a packet.
 */
public class ParsedProtocol {

    private String protocolName;
    private Map<String, String> headers = new HashMap<>();
    private byte[] payload;
    private String payloadText;         // decoded text if applicable
    private boolean fullyParsed;
    private String parseError;

    public ParsedProtocol() {}

    public ParsedProtocol(String protocolName) {
        this.protocolName = protocolName;
    }

    // Getters and Setters
    public String getProtocolName() { return protocolName; }
    public void setProtocolName(String protocolName) { this.protocolName = protocolName; }
    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }
    public void addHeader(String key, String value) { this.headers.put(key, value); }
    public byte[] getPayload() { return payload; }
    public void setPayload(byte[] payload) { this.payload = payload; }
    public String getPayloadText() { return payloadText; }
    public void setPayloadText(String payloadText) { this.payloadText = payloadText; }
    public boolean isFullyParsed() { return fullyParsed; }
    public void setFullyParsed(boolean fullyParsed) { this.fullyParsed = fullyParsed; }
    public String getParseError() { return parseError; }
    public void setParseError(String parseError) { this.parseError = parseError; }
}
