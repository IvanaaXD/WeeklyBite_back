package com.backend.weeklybite.controller;

import com.backend.weeklybite.domain.enums.AccountStatus;
import com.backend.weeklybite.dto.account.GetAccountDTO;
import com.backend.weeklybite.dto.account.LoginDTO;
import com.backend.weeklybite.dto.account.LoginResponseDTO;
import com.backend.weeklybite.security.jwt.JwtTokenUtil;
import com.backend.weeklybite.service.AccountService;
import com.backend.weeklybite.service.WeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccountService accountService;

    @Autowired
    private WeekService weekService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping
    public LoginResponseDTO login(@RequestBody LoginDTO user) {
        GetAccountDTO userAccount = accountService.getUserAccountByEmail(user.getEmail());
        if (userAccount.getAccountStatus() == AccountStatus.PENDING) {
            throw new org.springframework.security.authentication.DisabledException("Account is not activated. Please check your email.");
        }

        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword());
        Authentication auth = authenticationManager.authenticate(authReq);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);

        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = jwtTokenUtil.generateToken(user.getEmail(), roles);

        LoginResponseDTO response = new LoginResponseDTO();
        response.setJwt(token);
        response.setEmail(user.getEmail());
        response.setRole(roles.isEmpty() ? null : roles.get(0));

        weekService.checkWeeks();

        return response;
    }
}
