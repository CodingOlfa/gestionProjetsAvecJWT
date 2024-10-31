package com.mdw3.AppGestionProjets.security.user;

import com.mdw3.AppGestionProjets.configuration.jwt.JwtUtil;
import com.mdw3.AppGestionProjets.entities.UserProfile;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public MyUser createUser(@RequestBody MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return myUserRepository.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            // Authentifier l'utilisateur
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            // Si les informations d'identification sont incorrectes, lancer une exception
            throw new RuntimeException("Bad credentials");
        }

        // Charger les détails de l'utilisateur
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        // Générer le token JWT
        return jwtUtil.generateToken(userDetails);
    }
}

@Data
class AuthRequest {
    private String username;
    private String password;
    // Getters et Setters
}


