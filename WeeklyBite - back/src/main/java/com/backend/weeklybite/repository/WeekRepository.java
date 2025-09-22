package com.backend.weeklybite.repository;

import com.backend.weeklybite.domain.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface WeekRepository extends JpaRepository<Week, Long>, JpaSpecificationExecutor<Week> {

    @Query("SELECT w FROM Week w JOIN FETCH w.weekDays WHERE w.user.id = :userId")
    Optional<Week> findByUserId(@Param("userId") Long userId);

    @Query("SELECT w FROM Week w JOIN FETCH w.weekDays " +
            "WHERE w.user.id = :userId AND w.startDate = :nextMonday")
    Optional<Week> findNextWeekByUserId(@Param("userId") Long userId, @Param("nextMonday") LocalDate nextMonday);

    @Query("SELECT w FROM Week w JOIN FETCH w.weekDays " +
            "WHERE w.user.id = :userId AND :today BETWEEN w.startDate AND w.endDate")
    Optional<Week> findCurrentWeekByUserId(@Param("userId") Long userId, @Param("today") LocalDate today);

}
