package com.backend.weeklybite.controller;

import com.backend.weeklybite.dto.account.*;
import com.backend.weeklybite.service.AccountService;
import com.backend.weeklybite.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins="*")
public class UserAccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthService authService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetAccountDTO> getAccount(@PathVariable("id") Long id) {
        GetAccountDTO account = accountService.getUserAccountById(id);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetAccountDTO> create(@RequestBody CreateAccountDTO account) throws Exception {
        GetAccountDTO createdAccount = accountService.create(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetAccountDTO> update(@PathVariable Long id, @RequestBody UpdateAccountDTO account) throws Exception {
        GetAccountDTO updatedAccount = accountService.update(id, account);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @DeleteMapping(path = "/deactivate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deactivateAccount() {
        accountService.deactivateAccount();
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/change-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDTO passwordRequest) {
        accountService.changePassword(passwordRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetAccountDTO> verificateAccount(@RequestBody VerificateAccountDTO verificateAccountDTO) throws Exception {
        GetAccountDTO account = accountService.verificateAccount(verificateAccountDTO);
        return new ResponseEntity<GetAccountDTO>(account, HttpStatus.OK);
    }

//    @GetMapping("/activate")
//    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
//        try {
//            accountService.verificateAccount(token);
//            return ResponseEntity.ok("Account successfully activated!");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Invalid or expired token.");
//        }
//    }


//    @PostMapping("/{accountId}/favorites/{recipeId}")
//    public ResponseEntity<GetRecipeDTO> addFavoriteRecipe(
//            @PathVariable Long accountId,
//            @PathVariable Long serviceId) {
//        accountService.addFavouriteRecipes(accountId, recipeId);
//        GetRecipeDTO service = recipeService.getRecipeById(serviceId);
//        return new ResponseEntity<GetRecipeDTO>(service, HttpStatus.OK);
//    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
        try {
            accountService.activateAccount(token);
            String successHtml = "<html><body>"
                    + "<h1>Account Activated Successfully!</h1>"
                    + "<p>Your WeeklyBiteApp account has been successfully activated. You can now close this page and log in to the app.</p>"
                    + "<p>Thank you!</p>"
                    + "</body></html>";
            return ResponseEntity.ok().body(successHtml);
        } catch (RuntimeException e) {
            String errorHtml = "<html><body>"
                    + "<h1>Account Activation Failed!</h1>"
                    + "<p>An error occurred during activation: " + e.getMessage() + "</p>"
                    + "<p>Please ensure you are using a valid and unexpired activation link. If the link has expired, you will need to re-register.</p>"
                    + "</body></html>";
            return ResponseEntity.badRequest().body(errorHtml);
        }
    }
}
