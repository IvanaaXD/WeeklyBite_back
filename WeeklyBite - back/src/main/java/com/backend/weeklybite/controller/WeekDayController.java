package com.backend.weeklybite.controller;

import com.backend.weeklybite.dto.week_day.GetWeekDayDTO;
import com.backend.weeklybite.dto.week_day.UpdateWeekDayDTO;
import com.backend.weeklybite.service.AuthService;
import com.backend.weeklybite.service.WeekDayService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/week-days")
@CrossOrigin(origins="*")
public class WeekDayController {

    @Autowired
    private WeekDayService weekDayService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ModelMapper modelMapper;

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<GetWeekDayDTO> update(@RequestBody UpdateWeekDayDTO weekDay,
                                                @PathVariable Long id) throws Exception {
        GetWeekDayDTO updatedWeekDay = weekDayService.update(id, weekDay);
        return new ResponseEntity<GetWeekDayDTO>(updatedWeekDay, HttpStatus.OK);
    }
}
