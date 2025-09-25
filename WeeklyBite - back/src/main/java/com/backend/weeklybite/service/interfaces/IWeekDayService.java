package com.backend.weeklybite.service.interfaces;

import com.backend.weeklybite.domain.Week;
import com.backend.weeklybite.domain.WeekDay;
import com.backend.weeklybite.dto.week_day.GetWeekDayDTO;
import com.backend.weeklybite.dto.week_day.UpdateWeekDayDTO;

import java.util.List;

public interface IWeekDayService {
    List<WeekDay> createWeekDaysForWeek(Week week);

    GetWeekDayDTO update(Long id, UpdateWeekDayDTO weekDay);
}
