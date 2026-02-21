package com.pgmanagement.security;

import com.pgmanagement.entity.User;
import com.pgmanagement.enums.Role;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
public class CustomUserDetails implements UserDetails {

    
	private static final long serialVersionUID = 1L;
	private Long id;
    private String email;
    private String password;
    private Role role;
    private boolean enabled;
    private boolean accountNonLocked;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.enabled = user.isEnabled();
        this.accountNonLocked = user.isAccountNonLocked();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority(role.name())
        );
    }

    @Override
    public String getUsername() {
        return email; // we can still login via email/phone, but Spring Security uses username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // can implement expiry later
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // can implement password expiry later
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}