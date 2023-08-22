package com.softserve.itacademy.security;

import com.softserve.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String encodedPassword = passwordEncoder.encode(password);
        String savedPassword = userService.findByEmail(username).getPassword();
        if (passwordEncoder.matches(encodedPassword, savedPassword)) {
            if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                // Действия для администратора



                response.sendRedirect("/home");
            } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {

                response.sendRedirect("/user-home");
            } else {

                response.sendRedirect("/user-home");
            }
        } else {
            response.sendRedirect("/login-form?error=true");
        }
    }



//
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
//            throws IOException, ServletException {
//
//        // Получение ролей пользователя
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
//        // Проверка ролей и выполнение действий в зависимости от них
//        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//            // Действия для администратора
//            response.sendRedirect("/home");
//        } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
//            // Действия для обычного пользователя
//            response.sendRedirect("/user-home");
//        } else {
//            // Действия по умолчанию
//            response.sendRedirect("/default-home");
//        }
//    }


}
