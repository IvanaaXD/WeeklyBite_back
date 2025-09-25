package com.backend.weeklybite.service;

import com.backend.weeklybite.domain.UserAccount;
import com.backend.weeklybite.domain.Week;
import com.backend.weeklybite.domain.WeekDay;
import com.backend.weeklybite.dto.recipe.GetRecipeDTO;
import com.backend.weeklybite.dto.week.GetWeekDTO;
import com.backend.weeklybite.dto.week_day.GetWeekDayDTO;
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
import java.util.*;

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

//        if (dayOfWeekToday != DayOfWeek.SATURDAY && dayOfWeekTomorrow != DayOfWeek.MONDAY) {
//            throw new IllegalStateException("Week can only be created on Sunday before Monday");
//        }

        Week week = new Week();

        UserAccount userAccount = authService.getAuthenticatedUserAccount();
        week.setUser(userAccount);


        LocalDate nextMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        //week.setStartDate(tomorrow);
        week.setStartDate(nextMonday);

        //LocalDate endOfWeek = tomorrow.plusDays(6);
        LocalDate endOfWeek = nextMonday.plusDays(6);
        week.setEndDate(endOfWeek);

        List<WeekDay> weekDays = allWeekDays.createWeekDaysForWeek(week);
        week.setWeekDays(weekDays);

        Week createdWeek = allWeeks.save(week);

        userAccount.setNextWeek(createdWeek);
        allAccounts.save(userAccount);

        return modelMapper.map(createdWeek, GetWeekDTO.class);
    }
    //            if (today.getDayOfWeek() == DayOfWeek.SATURDAY ||today.getDayOfWeek() == DayOfWeek.SUNDAY) {
//                if (today.isAfter(currentWeek.getStartDate())) {
//                    nextWeekDTO = create();
//                }
//            } else {
//                isWeekend = false;
//            }
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

            if (today.isAfter(currentWeek.getStartDate())) {
                nextWeek = create();
            }
        }

        if (!isWeekend){
            return;
        }

        LocalDate nextWeekStart = nextWeek.getStartDate();

        if (!today.isBefore(nextWeekStart)) {

            Week next = modelMapper.map(nextWeek, Week.class);
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
        System.out.println("Next Monday: " + nextMonday);

        Week week = allWeeks.findNextWeekByUserId(id, nextMonday)
                .orElseThrow(() -> new EntityNotFoundException("Next week not found"));

        return modelMapper.map(week, GetWeekDTO.class);
    }

    @Override
    public Collection<GetRecipeDTO> getCurrentWeekRecipes(Long id) {
        GetWeekDTO weekDTO = getCurrentWeekByUserId(id);

        Set<GetRecipeDTO> uniqueRecipes = new HashSet<>();

        for (GetWeekDayDTO weekDayDTO : weekDTO.getWeekDays()) {
            if (weekDayDTO.getRecipes() != null) {
                uniqueRecipes.addAll(weekDayDTO.getRecipes());
            }
        }

        return new ArrayList<>(uniqueRecipes);
    }

    @Override
    public Collection<GetWeekDTO> getPastWeeksByUserId(Long userId) {

        GetWeekDTO currentWeekDTO = getCurrentWeekByUserId(userId);
        GetWeekDTO nextWeekDTO = getNextWeekByUserId(userId);
        List<Week> allUserWeeks = allWeeks.findAllByUserId(userId);

        List<Week> pastWeeks = allUserWeeks.stream()
                .filter(week -> !week.getId().equals(currentWeekDTO.getId()) && !week.getId().equals(nextWeekDTO.getId()))
                .sorted(Comparator.comparing(Week::getStartDate).reversed())
                .toList();

        return pastWeeks.stream()
                .map(week -> modelMapper.map(week, GetWeekDTO.class))
                .toList();
    }

    public Map<Long, Long> getRecipeUsageCounts(Long userId) {
        Collection<GetWeekDTO> pastWeeks = getPastWeeksByUserId(userId);

        Map<Long, Long> recipeCounts = new HashMap<>();

        for (GetWeekDTO week : pastWeeks) {
            for (GetWeekDayDTO weekDay : week.getWeekDays()) {
                if (weekDay.getRecipes() != null) {
                    for (GetRecipeDTO recipe : weekDay.getRecipes()) {
                        recipeCounts.put(
                                recipe.getId(),
                                recipeCounts.getOrDefault(recipe.getId(), 0L) + 1
                        );
                    }
                }
            }
        }

        return recipeCounts;
    }
}
