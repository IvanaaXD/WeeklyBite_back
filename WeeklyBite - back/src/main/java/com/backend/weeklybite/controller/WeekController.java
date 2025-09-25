package com.backend.weeklybite.controller;

import com.backend.weeklybite.domain.UserAccount;
import com.backend.weeklybite.dto.week.GetWeekDTO;
import com.backend.weeklybite.service.AuthService;
import com.backend.weeklybite.service.FileStorageService;
import com.backend.weeklybite.service.WeekService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/weeks")
@CrossOrigin(origins="*")
public class WeekController {

    @Autowired
    private WeekService weekService;

    @Autowired
    private AuthService authService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "/current-week", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetWeekDTO> getCurrentWeek() {
        UserAccount currentUser = authService.getAuthenticatedUserAccount();
        GetWeekDTO weekDTO = weekService.getCurrentWeekByUserId(currentUser.getId());
        return new ResponseEntity<GetWeekDTO>(weekDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/next-week", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetWeekDTO> getNextWeek() {
        UserAccount currentUser = authService.getAuthenticatedUserAccount();
        GetWeekDTO weekDTO = weekService.getNextWeekByUserId(currentUser.getId());
        return new ResponseEntity<GetWeekDTO>(weekDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/past-weeks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<GetWeekDTO>> getPastWeeks() {
        UserAccount currentUser = authService.getAuthenticatedUserAccount();
        Collection<GetWeekDTO> weekDTOs = weekService.getPastWeeksByUserId(currentUser.getId());
        return new ResponseEntity<Collection<GetWeekDTO>>(weekDTOs, HttpStatus.OK);
    }

    @PostMapping("/check-weeks")
    public ResponseEntity<String> checkWeeksForCurrentUser() {
        UserAccount currentUser = authService.getAuthenticatedUserAccount(); // ovo sada radi jer request ima JWT
        weekService.checkWeeks(); // refaktorisana funkcija koja prima UserAccount
        return ResponseEntity.ok("Weeks checked for user: " + currentUser.getEmail());
    }


//    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasAuthority('ROLE_USER')")
//    public ResponseEntity<GetWeekDTO> create() throws IOException {
//        GetWeekDTO savedWeek = weekService.create();
//        return new ResponseEntity<GetWeekDTO>(savedWeek, HttpStatus.CREATED);
//    }
}
