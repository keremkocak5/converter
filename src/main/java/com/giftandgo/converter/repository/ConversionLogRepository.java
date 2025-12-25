package com.giftandgo.converter.repository;

import com.giftandgo.converter.model.ConversionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversionLogRepository extends JpaRepository<ConversionLog, Long> {
}
