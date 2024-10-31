package com.mdw3.AppGestionProjets.security.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
    public class MyUserDetailsService implements UserDetailsService {

        @Autowired
        private MyUserRepository myUserRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            MyUser user = myUserRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return new MyUserDetails(user); // Conversion en MyUserDetails
        }
        }
