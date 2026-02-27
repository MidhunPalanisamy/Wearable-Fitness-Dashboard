package com.fitness.dashboard.repository;

import com.fitness.dashboard.entity.HeartRateLog;
import com.fitness.dashboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HeartRateLogRepository extends JpaRepository<HeartRateLog, Long> {
    List<HeartRateLog> findByUserAndTimestampBetweenOrderByTimestampAsc(User user, LocalDateTime start, LocalDateTime end);
}
