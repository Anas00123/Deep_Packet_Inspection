package com.dpi.interfaces;

import com.dpi.model.Packet;
import com.dpi.model.ParsedProtocol;

/**
 * Interface for parsing specific network protocols from raw packet data.
 */
public interface ProtocolParser {

    /**
     * Parse the given packet and extract protocol-specific fields.
     *
     * @param packet raw network packet
     * @return parsed protocol object with headers and payload
     */
    ParsedProtocol parse(Packet packet);

    /**
     * Return the protocol name this parser handles.
     *
     * @return protocol name e.g. "HTTP", "DNS", "FTP"
     */
    String getProtocolName();

    /**
     * Return the default port(s) associated with this protocol.
     *
     * @return array of port numbers
     */
    int[] getDefaultPorts();

    /**
     * Detect if this parser can handle the given raw bytes.
     *
     * @param rawBytes raw packet payload bytes
     * @return confidence score 0.0 - 1.0
     */
    double detect(byte[] rawBytes);
}
