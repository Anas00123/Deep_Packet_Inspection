package com.dpi.repository;

import com.dpi.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByStatus(Alert.AlertStatus status);
    List<Alert> findBySeverity(Alert.Severity severity);
    List<Alert> findBySourceIp(String sourceIp);
}
