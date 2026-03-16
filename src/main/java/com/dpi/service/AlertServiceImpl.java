package com.dpi.service;

import com.dpi.interfaces.AlertService;
import com.dpi.model.Alert;
import com.dpi.model.PacketAnalysisResult;
import com.dpi.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of AlertService.
 * Evaluates DPI results against rules and persists generated alerts.
 */
@Service
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;

    @Autowired
    public AlertServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public List<Alert> evaluate(PacketAnalysisResult result) {
        List<Alert> triggered = new ArrayList<>();

        // Rule 1: High risk suspicious packet
        if (result.isSuspicious() &&
                result.getRiskLevel() == PacketAnalysisResult.RiskLevel.HIGH) {
            Alert a = new Alert();
            a.setTitle("Suspicious HIGH-risk packet detected");
            a.setDescription("Packet from " + result.getPacket().getSourceIp()
                    + " flagged as suspicious with protocol: " + result.getDetectedProtocol());
            a.setSeverity(Alert.Severity.CRITICAL);
            a.setSourceIp(result.getPacket().getSourceIp());
            a.setDestinationIp(result.getPacket().getDestinationIp());
            a.setDetectedProtocol(result.getDetectedProtocol());
            a.setRuleTriggered("RULE_SUSPICIOUS_HIGH");
            a.setRelatedPacket(result.getPacket());
            triggered.add(a);
        }

        // Rule 2: Unencrypted FTP detected
        if ("FTP".equalsIgnoreCase(result.getDetectedProtocol())) {
            Alert a = new Alert();
            a.setTitle("Unencrypted FTP transfer detected");
            a.setDescription("Cleartext FTP detected from " + result.getPacket().getSourceIp());
            a.setSeverity(Alert.Severity.WARNING);
            a.setSourceIp(result.getPacket().getSourceIp());
            a.setDetectedProtocol("FTP");
            a.setRuleTriggered("RULE_CLEARTEXT_FTP");
            a.setRelatedPacket(result.getPacket());
            triggered.add(a);
        }

        return triggered;
    }

    @Override
    public void dispatch(Alert alert) {
        alertRepository.save(alert);
        // Future: send webhook, email, SIEM integration
    }

    @Override
    public List<Alert> getActiveAlerts() {
        return alertRepository.findByStatus(Alert.AlertStatus.ACTIVE);
    }

    @Override
    public void resolveAlert(Long alertId) {
        alertRepository.findById(alertId).ifPresent(alert -> {
            alert.setStatus(Alert.AlertStatus.RESOLVED);
            alert.setResolvedAt(Instant.now());
            alertRepository.save(alert);
        });
    }
}
