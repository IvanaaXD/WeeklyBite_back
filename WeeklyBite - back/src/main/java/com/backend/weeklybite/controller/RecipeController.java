package com.backend.weeklybite.controller;

import com.backend.weeklybite.domain.PagedResponse;
import com.backend.weeklybite.dto.recipe.GetRecipeDTO;
import com.backend.weeklybite.service.FileStorageService;
import com.backend.weeklybite.service.RecipeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="*")
@RequestMapping(path = "api/recipe")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(path = "/top-five", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GetRecipeDTO>> getTopEvents() {
        List<GetRecipeDTO> topEvents = recipeService.getTopFiveRecipes();
        return new ResponseEntity<>(topEvents, HttpStatus.OK);
    }

    @GetMapping(value = "/paged", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<GetRecipeDTO>> getPagedResponseRecipes(Pageable pageable) {

        long count = recipeService.getRecipeCount();
        Page<GetRecipeDTO> recipes = recipeService.getAllRecipes(pageable);
        PagedResponse<GetRecipeDTO> pagedCategoriesDTO = new PagedResponse<>(recipes.getContent(), (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<>(pagedCategoriesDTO, HttpStatus.OK);
    }
}
