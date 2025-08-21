package com.backend.weeklybite.service;

import com.backend.weeklybite.service.interfaces.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired private AuthService authService;

    @Value("${spring.mail.username}") private String sender;
    @Value("${app.base-url}") private String appBaseUrl;
    @Value("${frontend.base-url}") private String frontendBaseUrl;
    @Value("${mobile.deep-link.scheme}") private String scheme;

//    public ResponseEntity<String> sendEmailToEventOrganizer(EmailDetails details) {
//
//        try {
//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//
//            mailMessage.setFrom(sender);
//            //mailMessage.setTo(details.getRecipient());
//            mailMessage.setTo("diirrektorr@gmail.com");
//
//            String body = "Dear " + details.getRecipient() + ",\n\n"
//                    + "You have successfully reserved the service for your event: " + details.getEventName() + ".\n"
//                    + "Reservation details:\n"
//                    + "Service: " + details.getServiceName() + "\n"
//                    + "Date: " + details.getReservationDate() + "\n"
//                    + "Start time: " + details.getStartTime() + "\n"
//                    + "End time: " + details.getEndTime() + "\n\n"
//                    + "Thank you for using our service!\n\n"
//                    + "Best regards,\nYour team.";
//
//            mailMessage.setText(body);
//            mailMessage.setSubject("EventaApp: Service Reservation Confirmation");
//
//            javaMailSender.send(mailMessage);
//            return ResponseEntity.ok("{\"message\": \"Mail Sent Successfully to Event Organizer\"}");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("{\"message\": \"Error while Sending Mail to Event Organizer: " + e.getMessage() + "\"}");
//        }
//    }

    public void sendActivationEmail(String recipientEmail, String recipientName, String activationToken) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo(sender);
        mailMessage.setSubject("WeeklyBite: Activate Your Account");

        // Construct the activation link
        // This URL should point to your backend endpoint that handles activation
        String activationLink = appBaseUrl + "/api/accounts/activate?token=" + activationToken;

        String body = "Hello " + recipientName + ",\n\n"
                + "Thank you for registering with WeeklyBite! Please click the link below to activate your account:\n\n"
                + activationLink + "\n\n"
                + "This link will expire in 24 hours. If you did not register for an account, please ignore this email.\n\n"
                + "Best regards,\n"
                + "The WeeklyBite Team";

        mailMessage.setText(body);

        try {
            javaMailSender.send(mailMessage);
            System.out.println("Activation email sent successfully to: " + recipientEmail);
        } catch (Exception e) {
            System.err.println("Error sending activation email to " + recipientEmail + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
