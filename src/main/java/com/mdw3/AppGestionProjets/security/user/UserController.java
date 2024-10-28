package com.mdw3.AppGestionProjets.security.user;

import com.mdw3.AppGestionProjets.entities.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    @PostMapping("/register")
    public MyUser createUser(@RequestBody MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return myUserRepository.save(user);
    }

    @GetMapping("/login")
    public UserProfile getProfile(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            // Créer et retourner un objet UserProfile
            return new UserProfile(
                    userDetails.getUsername(),
                    userDetails.getAuthorities()
            );
        }

        // Si l'utilisateur n'est pas authentifié, vous pouvez retourner null ou gérer cela autrement
        return null;
    }


}
