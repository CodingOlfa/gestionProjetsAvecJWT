package com.mdw3.AppGestionProjets.entities;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
@Data
public class UserProfile {
    private String username;
    private Collection<GrantedAuthority> authorities;

    public UserProfile(String username, Collection<GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }


}
