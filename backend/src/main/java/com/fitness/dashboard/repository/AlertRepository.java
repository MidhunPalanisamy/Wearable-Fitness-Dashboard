package com.fitness.dashboard.repository;

import com.fitness.dashboard.entity.Alert;
import com.fitness.dashboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByUserOrderByTimestampDesc(User user);
}
