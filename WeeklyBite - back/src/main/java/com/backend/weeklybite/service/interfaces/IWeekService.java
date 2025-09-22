package com.backend.weeklybite.service.interfaces;

import com.backend.weeklybite.dto.week.GetWeekDTO;

public interface IWeekService {
    GetWeekDTO getWeekById(Long id);

    GetWeekDTO getWeekByUserId(Long id);

    GetWeekDTO create();

    void checkWeeks();

    GetWeekDTO getCurrentWeekByUserId(Long id);

    GetWeekDTO getNextWeekByUserId(Long id);
}
