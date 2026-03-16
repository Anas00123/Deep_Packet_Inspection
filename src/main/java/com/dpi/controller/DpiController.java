package com.dpi.controller;

import com.dpi.model.Alert;
import com.dpi.service.AlertServiceImpl;
import com.dpi.service.DpiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST API controller for the DPI dashboard.
 * All endpoints consumed by the HTML/JS frontend.
 */
@RestController
@RequestMapping("/api/dpi")
@CrossOrigin(origins = "*")
public class DpiController {

    private final DpiService dpiService;
    private final AlertServiceImpl alertService;

    @Autowired
    public DpiController(DpiService dpiService, AlertServiceImpl alertService) {
        this.dpiService   = dpiService;
        this.alertService = alertService;
    }

    /**
     * GET /api/dpi/stats
     * Returns live packet statistics for dashboard polling.
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getLiveStats() {
        return ResponseEntity.ok(dpiService.simulateLiveBurst());
    }

    /**
     * GET /api/dpi/alerts
     * Returns all active alerts.
     */
    @GetMapping("/alerts")
    public ResponseEntity<List<Alert>> getActiveAlerts() {
        return ResponseEntity.ok(alertService.getActiveAlerts());
    }

    /**
     * POST /api/dpi/alerts/{id}/resolve
     * Resolves (acknowledges) an alert by ID.
     */
    @PostMapping("/alerts/{id}/resolve")
    public ResponseEntity<Void> resolveAlert(@PathVariable Long id) {
        alertService.resolveAlert(id);
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/dpi/reset
     * Resets all counters (useful for testing).
     */
    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetCounters() {
        dpiService.reset();
        return ResponseEntity.ok(Map.of("status", "reset complete"));
    }
}
