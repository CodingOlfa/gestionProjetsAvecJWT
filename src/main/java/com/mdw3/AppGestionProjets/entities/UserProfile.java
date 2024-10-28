package com.mdw3.AppGestionProjets.entities;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
@Data
public class UserProfile {
    private String username;
    private Collection<? extends GrantedAuthority> authorities;

    public UserProfile(String username, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }


}
