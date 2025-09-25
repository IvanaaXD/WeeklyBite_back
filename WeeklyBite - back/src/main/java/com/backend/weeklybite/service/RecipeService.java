package com.backend.weeklybite.service;

import com.backend.weeklybite.domain.Ingredient;
import com.backend.weeklybite.domain.Recipe;
import com.backend.weeklybite.domain.UserAccount;
import com.backend.weeklybite.dto.ingredient.GetIngredientDTO;
import com.backend.weeklybite.dto.recipe.*;
import com.backend.weeklybite.repository.IngredientRepository;
import com.backend.weeklybite.repository.RecipeRepository;
import com.backend.weeklybite.service.interfaces.IRecipeService;
import com.backend.weeklybite.specification.RecipeSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecipeService implements IRecipeService {

    @Autowired
    private RecipeRepository allRecipes;

    @Autowired
    private IngredientRepository allIngredients;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private AccountService userService;

    @Autowired
    @Lazy
    private WeekService weekService;


    @Autowired
    private FileStorageService fileStorageService;

    public GetRecipeDTO getRecipeById(Long id) {
        Recipe recipe = allRecipes.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recipe with id " + id + " not found"));

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
    }

    @Override
    public List<GetRecipeDTO> getTopFiveRecipes(UserAccount user) {

        List<Recipe> allRecipesList = allRecipes.findAll();

        Set<Long> favoriteIds = user.getFavoriteRecipes().stream()
                .map(Recipe::getId)
                .collect(Collectors.toSet());

        Map<Long, Long> usageCounts = weekService.getRecipeUsageCounts(user.getId());

        List<RecipeScore> scoredRecipes = allRecipesList.stream()
                .map(recipe -> {
                    long score = 0;
                    if (favoriteIds.contains(recipe.getId())) {
                        score += 10;
                    }
                    score += usageCounts.getOrDefault(recipe.getId(), 0L);
                    return new RecipeScore(recipe, score);
                })
                .sorted((r1, r2) -> Long.compare(r2.score, r1.score))
                .toList();

        return scoredRecipes.stream()
                .limit(5)
                .map(rs -> {
                    GetRecipeDTO dto = modelMapper.map(rs.recipe, GetRecipeDTO.class);
                    if (rs.recipe.getPictures() != null && !rs.recipe.getPictures().isEmpty()) {
                        List<String> urls = rs.recipe.getPictures().stream()
                                .map(fileStorageService::getFileUrl)
                                .collect(Collectors.toList());
                        dto.setPictures(urls);
                    } else {
                        dto.setPictures(List.of());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private static class RecipeScore {
        Recipe recipe;
        long score;

        public RecipeScore(Recipe recipe, long score) {
            this.recipe = recipe;
            this.score = score;
        }
    }

    public Page<GetRecipeDTO> getAllRecipes(Pageable pageable) {
        Page<Recipe> recipesPage = allRecipes.findAllLast(
                pageable
        );

        return recipesPage.map(service -> {
            GetRecipeDTO dto = modelMapper.map(service, GetRecipeDTO.class);

            if (service.getPictures() != null && !service.getPictures().isEmpty()) {
                List<String> urls = service.getPictures().stream()
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

    public Page<Recipe> filterRecipes(RecipeFilterDTO filter, Pageable pageable) {
        Specification<Recipe> specification = new RecipeSpecification(filter);
        return allRecipes.findAll(specification, pageable);
    }

    public CreatedRecipeDTO create( CreateRecipeDTO createRecipeDTO, MultipartFile[] pictureFiles) {

        Recipe recipe = new Recipe();

        recipe.setName(createRecipeDTO.getName());
        recipe.setDescription(createRecipeDTO.getDescription());
        recipe.setContent(createRecipeDTO.getContent());
        recipe.setCategory(createRecipeDTO.getCategory());
        recipe.setDuration(createRecipeDTO.getDuration());
        recipe.setNumberOfPeople(createRecipeDTO.getNumberOfPeople());

        recipe.setCreated(LocalDate.now());
        recipe.setIsDeleted(false);
        recipe.setUpdated(LocalDate.now());

        List<String> storedAgencyPictureNames = new ArrayList<>();
        if (pictureFiles != null && pictureFiles.length > 0) {
            for (MultipartFile file : pictureFiles) {
                if (!file.isEmpty()) {
                    String fileName = fileStorageService.storeFile(file);
                    storedAgencyPictureNames.add(fileName);
                }
            }
        }
        recipe.setPictures(storedAgencyPictureNames);

        UserAccount admin = authService.getAuthenticatedUserAccount();
        recipe.setAdmin(admin);

        List<GetIngredientDTO> ingredientIds = createRecipeDTO.getProducts();
        List<Ingredient> ingredients = new ArrayList<>();
        for (GetIngredientDTO dto : ingredientIds) {
            allIngredients.findById(dto.getId()).ifPresent(ingredients::add);
        }
        recipe.setProducts(ingredients);

        Recipe createdRecipe = allRecipes.save(recipe);

        return modelMapper.map(createdRecipe, CreatedRecipeDTO.class);
    }

    public UpdatedRecipeDTO update(Long id, UpdateRecipeDTO updateRecipeDTO, MultipartFile[] pictureFiles) {

        Recipe existingRecipe = allRecipes.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Recipe with id " + id + " not found"));

        existingRecipe.setIsDeleted(true);
        allRecipes.save(existingRecipe);

        Recipe updateRecipe = new Recipe();

        updateRecipe.setName(updateRecipeDTO.getName() != null
                ? updateRecipeDTO.getName()
                : existingRecipe.getName());
        updateRecipe.setDescription(updateRecipeDTO.getDescription() != null
                ? updateRecipeDTO.getDescription()
                : existingRecipe.getDescription());
        updateRecipe.setContent(updateRecipeDTO.getContent() != null
                ? updateRecipeDTO.getContent()
                : existingRecipe.getContent());
        updateRecipe.setDuration(updateRecipeDTO.getDuration() != null
                ? updateRecipeDTO.getDuration()
                : existingRecipe.getDuration());
        updateRecipe.setNumberOfPeople(updateRecipeDTO.getNumberOfPeople() != null
                ? updateRecipeDTO.getNumberOfPeople()
                : existingRecipe.getNumberOfPeople());

        updateRecipe.setIsDeleted(false);
        updateRecipe.setUpdated(LocalDate.now());
        updateRecipe.setCreated(LocalDate.now());
        updateRecipe.setCategory(updateRecipeDTO.getCategory());
        updateRecipe.setAdmin(existingRecipe.getAdmin());

        List<String> storedPictureNames = new ArrayList<>();
        boolean newPicturesProvided = (pictureFiles != null && pictureFiles.length > 0 && !pictureFiles[0].isEmpty());

        if (newPicturesProvided) {
            for (MultipartFile file : pictureFiles) {
                if (!file.isEmpty()) {
                    String fileName = fileStorageService.storeFile(file);
                    storedPictureNames.add(fileName);
                }
            }
        } else {
            storedPictureNames.addAll(existingRecipe.getPictures());
        }
        updateRecipe.setPictures(storedPictureNames);

        List<GetIngredientDTO> ingredientIds = updateRecipeDTO.getProducts();
        List<Ingredient> ingredients = new ArrayList<>();
        for (GetIngredientDTO dto : ingredientIds) {
            allIngredients.findById(dto.getId()).ifPresent(ingredients::add);
        }
        updateRecipe.setProducts(ingredients);

        Recipe updatedRecipe = allRecipes.save(updateRecipe);
        return modelMapper.map(updatedRecipe, UpdatedRecipeDTO.class);
    }

    public void delete(Long id) {
        Recipe recipe = allRecipes.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Recipe with id " + id + " not found"));

        recipe.setIsDeleted(true);
        allRecipes.save(recipe);
    }
}

