package com.softserve.itacademy.security;

import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
//@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final RoleService roleService;

     Logger logger = LoggerFactory.getLogger("SampleLogger");



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        if (user == null) return null;
        else {
           return new UserDetails() {
              @Override
              public Collection<? extends GrantedAuthority> getAuthorities() {
                   Collection<Role> roles  = roleService.getAll();
                   Set<GrantedAuthority> authorities
                       = new HashSet<>();
                   for (Role role: roles) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
                   }
                   return authorities;
              }

               @Override
              public String getPassword() {return user.getPassword();}
               @Override
              public String getUsername() {return user.getEmail();}

                public long getId() {return user.getId();}
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
