package com.backend.weeklybite.controller;

import com.backend.weeklybite.dto.ingredient.CreateIngredientDTO;
import com.backend.weeklybite.dto.ingredient.GetIngredientDTO;
import com.backend.weeklybite.service.AuthService;
import com.backend.weeklybite.service.IngredientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins="*")
@RequestMapping(path = "api/ingredient")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<GetIngredientDTO> create( @RequestBody CreateIngredientDTO ingredient) throws IOException {
        GetIngredientDTO savedIngredient = ingredientService.create(ingredient);
        return new ResponseEntity<GetIngredientDTO>(savedIngredient, HttpStatus.CREATED);
    }


}
