package com.softserve.itacademy.sequrity;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        if (user == null) return null;
        else {
           return new UserDetails() {
              @Override
              public Collection<? extends GrantedAuthority> getAuthorities() {
                  GrantedAuthority authority = (GrantedAuthority) user.getRole();
                  return List.of(authority);
              }
               @Override
              public String getPassword() {return user.getPassword();}
               @Override
              public String getUsername() {return user.getEmail();}
               @Override
              public boolean isAccountNonExpired() {return true;}
               @Override
              public boolean isAccountNonLocked() {return true;}
               @Override
              public boolean isCredentialsNonExpired() {return true;}
               @Override
              public boolean isEnabled() {return true;}
           };
        }
    }
}
