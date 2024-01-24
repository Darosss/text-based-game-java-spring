package com.example.users;

import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {
    private User user;
    private boolean isActive;
    public CustomUserDetails(User user) {
        super(user.getUsername(), user.getPassword(),
                user.getRoles().stream().map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));
        this.user = user;
        this.isActive = user.isActive();

    }

    public User getUser() {
        return user;
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; //TODO: later
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; //TODO: later
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; //TODO: later
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}