package com.backend.weeklybite.controller;

import com.backend.weeklybite.domain.PagedResponse;
import com.backend.weeklybite.domain.Recipe;
import com.backend.weeklybite.domain.UserAccount;
import com.backend.weeklybite.dto.recipe.*;
import com.backend.weeklybite.exception.UserNotFoundException;
import com.backend.weeklybite.service.AuthService;
import com.backend.weeklybite.service.FileStorageService;
import com.backend.weeklybite.service.RecipeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins="*")
@RequestMapping(path = "api/recipe")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private AuthService authService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetRecipeDTO> getRecipeById(@PathVariable("id") Long id) {
        GetRecipeDTO recipeDTO = recipeService.getRecipeById(id);
        return new ResponseEntity<GetRecipeDTO>(recipeDTO, HttpStatus.OK);
    }

    @GetMapping(path = "/top-five", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GetRecipeDTO>> getTopEvents() {
        UserAccount getUser = authService.getAuthenticatedUserAccount();
        List<GetRecipeDTO> topEvents = recipeService.getTopFiveRecipes(getUser);
        return new ResponseEntity<>(topEvents, HttpStatus.OK);
    }

    @GetMapping(value = "/paged", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<GetRecipeDTO>> getPagedResponseRecipes(Pageable pageable) {

        long count = recipeService.getRecipeCount();
        Page<GetRecipeDTO> recipes = recipeService.getAllRecipes(pageable);
        PagedResponse<GetRecipeDTO> pagedCategoriesDTO = new PagedResponse<>(recipes.getContent(), (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<>(pagedCategoriesDTO, HttpStatus.OK);
    }

    @GetMapping(path = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<GetRecipeDTO>> filterServices(
            @ModelAttribute RecipeFilterDTO filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        Sort sort = Sort.unsorted();
        Pageable pageable = PageRequest.of(page, size, sort);
        Long currentUserId = -1L;
        List<Long> blockedUserIds = null;

        try {
            UserAccount userAccount = authService.getAuthenticatedUserAccount();
            currentUserId = userAccount.getId();
        } catch (UsernameNotFoundException | UserNotFoundException e) {
            // blockedUserIds is null
        }

        Page<Recipe> services = recipeService.filterRecipes(filter, pageable);

        // Page<Service> services = serviceService.filterServices(filter, pageable);
        Page<GetRecipeDTO> dtoPage = services.map(service -> {
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

        return ResponseEntity.ok(dtoPage);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CreatedRecipeDTO> create( @RequestPart("recipe") CreateRecipeDTO recipe,
                                                   @RequestPart(value = "pictures", required = false) MultipartFile[] pictureFiles) throws IOException {
        CreatedRecipeDTO savedRecipe = recipeService.create(recipe, pictureFiles);
        return new ResponseEntity<CreatedRecipeDTO>(savedRecipe, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UpdatedRecipeDTO> update( @RequestPart("recipe") UpdateRecipeDTO recipe,
                                                    @RequestPart(value = "pictures", required = false) MultipartFile[] pictureFiles, @PathVariable Long id) throws Exception {
        UpdatedRecipeDTO updatedRecipe = recipeService.update(id, recipe, pictureFiles);
        return new ResponseEntity<UpdatedRecipeDTO>(updatedRecipe, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        recipeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
