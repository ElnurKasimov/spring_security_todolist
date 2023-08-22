package com.softserve.itacademy.security;

import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.plaf.PanelUI;

@RequiredArgsConstructor
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
     private final UserService userService;

     @Override
     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
          final User user = userService.findByEmail(email);
          if (user == null) {
               throw new UsernameNotFoundException(email);
          }
          UserDetails userDetails = org.springframework.security.core.userdetails.User.
              withUsername(user.getEmail()).password(user.getPassword()).authorities("ROLE_" + user.getRole().getName()).build();
          return userDetails;
     }
}