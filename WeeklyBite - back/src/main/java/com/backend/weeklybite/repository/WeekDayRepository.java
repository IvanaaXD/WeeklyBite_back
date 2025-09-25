package com.backend.weeklybite.repository;

import com.backend.weeklybite.domain.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WeekDayRepository  extends JpaRepository<WeekDay, Long>, JpaSpecificationExecutor<WeekDay> {
}
