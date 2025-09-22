package com.backend.weeklybite.service;

import com.backend.weeklybite.domain.UserAccount;
import com.backend.weeklybite.domain.Week;
import com.backend.weeklybite.domain.WeekDay;
import com.backend.weeklybite.dto.week.GetWeekDTO;
import com.backend.weeklybite.repository.AccountRepository;
import com.backend.weeklybite.repository.WeekRepository;
import com.backend.weeklybite.service.interfaces.IWeekService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class WeekService implements IWeekService {

    @Autowired
    private WeekRepository allWeeks;

    @Autowired
    private WeekDayService allWeekDays;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private AccountRepository allAccounts;

    @Override
    public GetWeekDTO getWeekById(Long id) {

        Week week = allWeeks.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Week with id " + id + " not found"));

        return modelMapper.map(week, GetWeekDTO.class);
    }

    @Override
    public GetWeekDTO getWeekByUserId(Long id) {

        Week week = allWeeks.findByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException("Week with id " + id + " not found"));

        return modelMapper.map(week, GetWeekDTO.class);
    }

    @Override
    public GetWeekDTO create() {

        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeekToday = today.getDayOfWeek();
        LocalDate tomorrow = today.plusDays(1);
        DayOfWeek dayOfWeekTomorrow = tomorrow.getDayOfWeek();

        if (dayOfWeekToday != DayOfWeek.SATURDAY && dayOfWeekTomorrow != DayOfWeek.MONDAY) {
            throw new IllegalStateException("Week can only be created on Sunday before Monday");
        }

        Week week = new Week();

        UserAccount userAccount = authService.getAuthenticatedUserAccount();
        week.setUser(userAccount);
        week.setStartDate(tomorrow);

        LocalDate endOfWeek = tomorrow.plusDays(6);
        week.setEndDate(endOfWeek);

        List<WeekDay> weekDays = allWeekDays.createWeekDaysForWeek(week);
        week.setWeekDays(weekDays);

        Week createdWeek = allWeeks.save(week);

        userAccount.setNextWeek(createdWeek);
        allAccounts.save(userAccount);

        return modelMapper.map(createdWeek, GetWeekDTO.class);
    }

    @Override
    public void checkWeeks() {

        UserAccount userAccount = authService.getAuthenticatedUserAccount();
        GetWeekDTO currentWeek = getCurrentWeekByUserId(userAccount.getId());

        GetWeekDTO nextWeek = null;
        try {
            nextWeek = getNextWeekByUserId(userAccount.getId());
        } catch (EntityNotFoundException ignored) {
        }
        LocalDate today = LocalDate.now();

        GetWeekDTO nextWeekDTO = null;
        boolean isWeekend = true;
        if (nextWeek == null) {
            if (today.getDayOfWeek() == DayOfWeek.SATURDAY ||today.getDayOfWeek() == DayOfWeek.SUNDAY) {
                if (today.isAfter(currentWeek.getStartDate())) {
                    nextWeekDTO = create();
                }
            } else {
                isWeekend = false;
            }
        }

        if (!isWeekend){
            return;
        }

        LocalDate nextWeekStart = nextWeekDTO.getStartDate();

        if (!today.isBefore(nextWeekStart)) {

            Week next = modelMapper.map(nextWeekDTO, Week.class);
            userAccount.setCurrentWeek(next);
            userAccount.setNextWeek(null);

            allAccounts.save(userAccount);
        }
    }

    @Override
    public GetWeekDTO getCurrentWeekByUserId(Long id) {

        LocalDate today = LocalDate.now();

        Week week = allWeeks.findCurrentWeekByUserId(id, today)
                .orElseThrow(() -> new EntityNotFoundException("Week with id " + id + " not found"));

        return modelMapper.map(week, GetWeekDTO.class);
    }

    @Override
    public GetWeekDTO getNextWeekByUserId(Long id) {

        LocalDate today = LocalDate.now();
        LocalDate nextMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        Week week = allWeeks.findNextWeekByUserId(id, nextMonday)
                .orElseThrow(() -> new EntityNotFoundException("Next week not found"));

        return modelMapper.map(week, GetWeekDTO.class);
    }
}
