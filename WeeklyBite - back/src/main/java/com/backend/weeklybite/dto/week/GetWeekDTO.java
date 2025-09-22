package com.backend.weeklybite.dto.week;

import com.backend.weeklybite.dto.week_day.GetWeekDayDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetWeekDTO {

    private Long id;
    private List<GetWeekDayDTO> weekDays;
    private LocalDate startDate;
    private LocalDate endDate;
}
