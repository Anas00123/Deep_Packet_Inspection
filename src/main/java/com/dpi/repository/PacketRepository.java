package com.dpi.repository;

import com.dpi.model.Packet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PacketRepository extends JpaRepository<Packet, Long> {
    List<Packet> findBySourceIp(String sourceIp);
    List<Packet> findByCapturedAtAfter(Instant since);
    List<Packet> findByProtocol(String protocol);
}
