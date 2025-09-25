package com.backend.weeklybite.service;

import com.backend.weeklybite.domain.UserAccount;
import com.backend.weeklybite.dto.ingredient.IngredientWithQuantityDTO;
import com.backend.weeklybite.dto.recipe.GetRecipeDTO;
import com.backend.weeklybite.service.interfaces.IEmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private WeekService weekService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private AuthService authService;

    @Value("${spring.mail.username}") private String sender;
    @Value("${app.base-url}") private String appBaseUrl;
    @Value("${frontend.base-url}") private String frontendBaseUrl;
    @Value("${mobile.deep-link.scheme}") private String scheme;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public String sendEmail() {
        UserAccount currentUser = authService.getAuthenticatedUserAccount();
        Collection<GetRecipeDTO> recipes = weekService.getCurrentWeekRecipes(currentUser.getId());
        Collection<IngredientWithQuantityDTO> ingredients =
                ingredientService.getIngredientsByWeekId(weekService.getCurrentWeekByUserId(currentUser.getId()).getId());

        byte[] pdfRecipeBytes = pdfService.generateWeeklyRecipesPdf(recipes);
        byte[] pdfIngredientBytes = pdfService.generateWeeklyIngredientsPdf(ingredients);

        if (pdfRecipeBytes != null && pdfIngredientBytes != null) {
            Map<String, byte[]> attachments = new HashMap<>();
            attachments.put("weekly-recipes.pdf", pdfRecipeBytes);
            attachments.put("weekly-ingredients.pdf", pdfIngredientBytes);

            sendEmailWithAttachments(
                    "Your weekly recipe plan",
                    "Attached you will find your weekly recipe plan and ingredients list in PDF format.",
                    attachments
            );

            return "The PDFs have been successfully sent to your email.";
        } else {
            return "An error occurred while generating the PDFs.";
        }
    }

    public void sendEmailWithAttachments(String subject, String body, Map<String, byte[]> attachments) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(sender);
            helper.setSubject(subject);
            helper.setText(body);

            for (Map.Entry<String, byte[]> entry : attachments.entrySet()) {
                String filename = entry.getKey();
                byte[] fileBytes = entry.getValue();
                helper.addAttachment(filename, new ByteArrayResource(fileBytes));
            }

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendActivationEmail(String recipientEmail, String recipientName, String activationToken) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo(sender);
        mailMessage.setSubject("WeeklyBite: Activate Your Account");

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
