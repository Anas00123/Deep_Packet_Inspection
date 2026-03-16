package com.dpi.repository;

import com.dpi.model.PacketAnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisResultRepository extends JpaRepository<PacketAnalysisResult, Long> {
    List<PacketAnalysisResult> findByDetectedProtocol(String protocol);
    List<PacketAnalysisResult> findBySuspiciousTrue();
    List<PacketAnalysisResult> findByRiskLevel(PacketAnalysisResult.RiskLevel level);
}
