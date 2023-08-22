package com.softserve.itacademy.security;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final PasswordEncoder encoder;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
//        String encodedPassword = encoder.encode(password);
        String savedPassword = userService.findByEmail(username).getPassword();
//            if (encoder.matches(encodedPassword, savedPassword)) {
        Object principal = authentication.getPrincipal();
        User user = new User();
        if (principal instanceof User) user = (User) principal;
        Long userId = user.getId();

        if (password.equals(savedPassword)) {
            if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                // Действия для администратора
                System.out.println("Entered with role ADMIN");
                response.sendRedirect("/todos/all/users/" + userId);
            } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                System.out.println("Entered with role USER");
                response.sendRedirect("/todos/all/users" + userId);
            } else {
                response.sendRedirect("/loging-form");
            }
        } else {
            response.sendRedirect("/login-form?error=true");
        }
    }
}
