package com.backend.weeklybite.controller;

import com.backend.weeklybite.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emails")
@CrossOrigin(origins="*")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-pdf")
    public String sendWeeklyPdf() {
        String message = emailService.sendEmail();
        return message;
    }
}
