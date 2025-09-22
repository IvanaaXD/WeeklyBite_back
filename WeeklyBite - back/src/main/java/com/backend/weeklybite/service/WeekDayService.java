package com.backend.weeklybite.service;

import com.backend.weeklybite.domain.Recipe;
import com.backend.weeklybite.domain.Week;
import com.backend.weeklybite.domain.WeekDay;
import com.backend.weeklybite.domain.enums.Day;
import com.backend.weeklybite.dto.recipe.GetRecipeDTO;
import com.backend.weeklybite.dto.week_day.GetWeekDayDTO;
import com.backend.weeklybite.dto.week_day.UpdateWeekDayDTO;
import com.backend.weeklybite.repository.WeekDayRepository;
import com.backend.weeklybite.service.interfaces.IWeekDayService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeekDayService implements IWeekDayService {

    @Autowired
    private WeekDayRepository allWeekDays;

    @Autowired
    private RecipeService allRecipes;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EntityManager entityManager;

    // --- Kreiranje dummy recepta ---
    private Recipe createDummyRecipe() {
        Recipe dummy = new Recipe();
        dummy.setId(0L);                       // ID = 0 znači da je dummy
        dummy.setComments(new ArrayList<>());   // prazne liste
        dummy.setProducts(new ArrayList<>());
        dummy.setPictures(new ArrayList<>());
        return dummy;
    }

    @Override
    public List<WeekDay> createWeekDaysForWeek(Week week) {
        List<WeekDay> weekDays = new ArrayList<>();

        for (Day day : Day.values()) {
            WeekDay weekDay = new WeekDay();
            weekDay.setDay(day);
            weekDay.setWeek(week);

            List<Recipe> recipes = new ArrayList<>();
            weekDay.setRecipes(recipes);

            weekDays.add(weekDay);
        }
        return weekDays;
    }

    @Override
    public GetWeekDayDTO update(Long id, UpdateWeekDayDTO weekDayDTO) {
        WeekDay weekDay = allWeekDays.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Week day with id " + id + " not found"));

        List<Recipe> currentRecipes = weekDay.getRecipes();
        GetRecipeDTO newRecipeDTO = allRecipes.getRecipeById(weekDayDTO.getRecipeId());
        Recipe newRecipe = modelMapper.map(newRecipeDTO, Recipe.class);

        if (newRecipe.getPictures() != null) {
            List<String> trimmedPictures = newRecipe.getPictures().stream()
                    .map(path -> path.substring(path.lastIndexOf('/') + 1))
                    .toList();
            newRecipe.setPictures(trimmedPictures);
        }

        currentRecipes.add(newRecipe);
        weekDay.setRecipes(currentRecipes);

        WeekDay updatedWeekDay = allWeekDays.save(weekDay);
        return modelMapper.map(updatedWeekDay, GetWeekDayDTO.class);
    }
}
