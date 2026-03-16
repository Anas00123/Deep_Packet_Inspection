package com.dpi.service;

import com.dpi.interfaces.PacketAnalyzer;
import com.dpi.interfaces.AlertService;
import com.dpi.model.*;
import com.dpi.repository.PacketRepository;
import com.dpi.repository.AnalysisResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Core DPI service — coordinates capture simulation, analysis, and alerting.
 */
@Service
public class DpiService implements PacketAnalyzer {

    private final PacketRepository packetRepo;
    private final AnalysisResultRepository resultRepo;
    private final AlertService alertService;

    // Simulated counters for live stats
    private final AtomicLong totalPackets = new AtomicLong(0);
    private final AtomicLong totalBytes   = new AtomicLong(0);
    private final Map<String, AtomicLong> protocolCounts = new LinkedHashMap<>();

    private static final String[] PROTOCOLS  = {"HTTP", "HTTPS/TLS", "DNS", "FTP", "SSH", "SMTP", "UDP-Other"};
    private static final String[] SAMPLE_IPS = {"192.168.1.", "10.0.0.", "172.16.0.", "203.0.113."};
    private static final Random RNG = new Random();

    @Autowired
    public DpiService(PacketRepository packetRepo,
                      AnalysisResultRepository resultRepo,
                      AlertService alertService) {
        this.packetRepo  = packetRepo;
        this.resultRepo  = resultRepo;
        this.alertService = alertService;
        for (String p : PROTOCOLS) protocolCounts.put(p, new AtomicLong(0));
    }

    // ─── PacketAnalyzer implementation ───────────────────────────────────────

    @Override
    public PacketAnalysisResult analyze(Packet packet) {
        PacketAnalysisResult result = new PacketAnalysisResult();
        result.setPacket(packet);
        result.setDetectedProtocol(detectProtocol(packet));
        result.setEncrypted(isEncrypted(packet.getDestinationPort()));
        result.setSuspicious(RNG.nextDouble() < 0.05); // 5% chance flagged
        result.setRiskLevel(assignRisk(result));

        packetRepo.save(packet);
        resultRepo.save(result);

        alertService.evaluate(result).forEach(alertService::dispatch);
        return result;
    }

    @Override
    public List<PacketAnalysisResult> analyzeBatch(List<Packet> packets) {
        List<PacketAnalysisResult> results = new ArrayList<>();
        for (Packet p : packets) results.add(analyze(p));
        return results;
    }

    @Override
    public boolean supports(String protocol) {
        return Arrays.asList(PROTOCOLS).contains(protocol.toUpperCase());
    }

    @Override
    public void reset() {
        totalPackets.set(0);
        totalBytes.set(0);
        protocolCounts.values().forEach(c -> c.set(0));
    }

    // ─── Live stats simulation ────────────────────────────────────────────────

    /**
     * Simulate capturing a burst of packets and return summary stats.
     * Called by the REST controller for live dashboard updates.
     */
    public Map<String, Object> simulateLiveBurst() {
        int burstSize = 5 + RNG.nextInt(20);
        for (int i = 0; i < burstSize; i++) {
            String proto = PROTOCOLS[RNG.nextInt(PROTOCOLS.length)];
            int pktLen = 64 + RNG.nextInt(1460);
            totalPackets.incrementAndGet();
            totalBytes.addAndGet(pktLen);
            protocolCounts.get(proto).incrementAndGet();
        }

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalPackets", totalPackets.get());
        stats.put("totalBytes", totalBytes.get());
        stats.put("packetsPerSecond", 50 + RNG.nextInt(450));
        stats.put("throughputMbps", Math.round((1 + RNG.nextDouble() * 99) * 100.0) / 100.0);
        stats.put("activeFlows", 10 + RNG.nextInt(200));
        stats.put("alertCount", alertService.getActiveAlerts().size());

        Map<String, Long> protoMap = new LinkedHashMap<>();
        protocolCounts.forEach((k, v) -> protoMap.put(k, v.get()));
        stats.put("protocolDistribution", protoMap);

        // Latest 5 synthetic packets for the feed
        List<Map<String, Object>> feed = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> pkt = new LinkedHashMap<>();
            pkt.put("time", Instant.now().toString());
            pkt.put("src", SAMPLE_IPS[RNG.nextInt(SAMPLE_IPS.length)] + (1 + RNG.nextInt(254)));
            pkt.put("dst", SAMPLE_IPS[RNG.nextInt(SAMPLE_IPS.length)] + (1 + RNG.nextInt(254)));
            pkt.put("protocol", PROTOCOLS[RNG.nextInt(PROTOCOLS.length)]);
            pkt.put("length", 64 + RNG.nextInt(1460));
            pkt.put("risk", new String[]{"LOW", "LOW", "LOW", "MEDIUM", "HIGH"}[RNG.nextInt(5)]);
            feed.add(pkt);
        }
        stats.put("recentPackets", feed);
        return stats;
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private String detectProtocol(Packet packet) {
        int port = packet.getDestinationPort();
        if (port == 80)  return "HTTP";
        if (port == 443) return "HTTPS/TLS";
        if (port == 53)  return "DNS";
        if (port == 21)  return "FTP";
        if (port == 22)  return "SSH";
        if (port == 25 || port == 587) return "SMTP";
        return "UDP-Other";
    }

    private boolean isEncrypted(int port) {
        return port == 443 || port == 22 || port == 993 || port == 995;
    }

    private PacketAnalysisResult.RiskLevel assignRisk(PacketAnalysisResult r) {
        if (r.isSuspicious())  return PacketAnalysisResult.RiskLevel.HIGH;
        if (!r.isEncrypted())  return PacketAnalysisResult.RiskLevel.MEDIUM;
        return PacketAnalysisResult.RiskLevel.LOW;
    }
}
