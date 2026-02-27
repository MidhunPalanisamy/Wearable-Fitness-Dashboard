package com.fitness.dashboard.repository;

import com.fitness.dashboard.entity.DailyMetrics;
import com.fitness.dashboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyMetricsRepository extends JpaRepository<DailyMetrics, Long> {
    Optional<DailyMetrics> findByUserAndDate(User user, LocalDate date);
    List<DailyMetrics> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDate start, LocalDate end);
}
