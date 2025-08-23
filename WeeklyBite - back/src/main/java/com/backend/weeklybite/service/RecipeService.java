package com.backend.weeklybite.service;

import com.backend.weeklybite.domain.Recipe;
import com.backend.weeklybite.dto.recipe.GetRecipeDTO;
import com.backend.weeklybite.repository.RecipeRepository;
import com.backend.weeklybite.service.interfaces.IRecipeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService implements IRecipeService {

    @Autowired
    private RecipeRepository allRecipes;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileStorageService fileStorageService;

    public List<GetRecipeDTO> getTopFiveRecipes() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Recipe> recipes = allRecipes.findAllLast(pageable);

        return recipes.stream().map(recipe -> {
            GetRecipeDTO dto = modelMapper.map(recipe, GetRecipeDTO.class);

            if (recipe.getPictures() != null && !recipe.getPictures().isEmpty()) {
                List<String> urls = recipe.getPictures().stream()
                        .map(fileStorageService::getFileUrl)
                        .collect(Collectors.toList());
                dto.setPictures(urls);
            } else {
                dto.setPictures(List.of());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    public Page<GetRecipeDTO> getAllRecipes(Pageable pageable) {

        Page<Recipe> servicesPage = allRecipes.findAllByIsDeletedOrderByUpdatedAsc(
                pageable,
                false
        );

        return servicesPage.map(recipe -> {
            GetRecipeDTO dto = modelMapper.map(recipe, GetRecipeDTO.class);

            if (recipe.getPictures() != null && !recipe.getPictures().isEmpty()) {
                List<String> urls = recipe.getPictures().stream()
                        .map(fileStorageService::getFileUrl)
                        .collect(Collectors.toList());
                dto.setPictures(urls);
            } else {
                dto.setPictures(null);
            }

            return dto;
        });
    }

    public long getRecipeCount() {
        return allRecipes.count();
    }
}

