package com.softserve.itacademy.security;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;


@Slf4j
@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final PasswordEncoder encoder;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        User user = userService.findByEmail(authentication.getName());
        log.info("USER " + user);
        String password = request.getParameter("password");
        log.info("PASSWORD -  {}  for user {} ", password, user );
        String encodedPassword = encoder.encode(password);
        log.info("ENCODED PASSWORD  - " + encodedPassword);
        String savedPassword = user.getPassword();
        log.info("SAVED PASSWORD - " + savedPassword);
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                       log.info("Entered with role ADMIN");
                        response.sendRedirect("/todos/all/users/" + user.getId());
        } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                        log.info("Entered with role USER");
                        response.sendRedirect("/todos/all/users/" + user.getId());
        } else response.sendRedirect("/login-form?error=true");
    }

}
