package com.dpi.interfaces;

import com.dpi.model.Packet;
import com.dpi.model.PacketAnalysisResult;

import java.util.List;

/**
 * Core interface for Deep Packet Inspection analysis.
 * Implementations perform protocol-level inspection of raw packets.
 */
public interface PacketAnalyzer {

    /**
     * Analyze a single packet and return inspection result.
     *
     * @param packet the raw packet to inspect
     * @return analysis result including protocol, payload metadata, and flags
     */
    PacketAnalysisResult analyze(Packet packet);

    /**
     * Batch analyze a list of packets.
     *
     * @param packets list of raw packets
     * @return list of analysis results in same order
     */
    List<PacketAnalysisResult> analyzeBatch(List<Packet> packets);

    /**
     * Returns true if this analyzer supports the given protocol.
     *
     * @param protocol protocol name (e.g., "HTTP", "DNS", "TLS")
     * @return true if supported
     */
    boolean supports(String protocol);

    /**
     * Reset internal state (e.g., flow tracking tables).
     */
    void reset();
}
