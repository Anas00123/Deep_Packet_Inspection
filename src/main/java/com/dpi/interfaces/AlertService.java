package com.dpi.interfaces;

import com.dpi.model.Alert;
import com.dpi.model.PacketAnalysisResult;

import java.util.List;

/**
 * Service interface for generating and dispatching DPI alerts.
 */
public interface AlertService {

    /**
     * Evaluate an analysis result and generate alerts if rules are triggered.
     *
     * @param result packet analysis result
     * @return list of alerts generated (empty if none)
     */
    List<Alert> evaluate(PacketAnalysisResult result);

    /**
     * Dispatch an alert to configured outputs (DB, webhook, email, etc.).
     *
     * @param alert the alert to dispatch
     */
    void dispatch(Alert alert);

    /**
     * Retrieve all active (unresolved) alerts.
     *
     * @return list of active alerts
     */
    List<Alert> getActiveAlerts();

    /**
     * Acknowledge/resolve an alert by ID.
     *
     * @param alertId the alert to resolve
     */
    void resolveAlert(Long alertId);
}
