package com.softserve.itacademy.sequrity;
import SpringBoot.App.role_user.RoleUser;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<RoleUser> userDataLines = jdbcTemplate.query(
                "SELECT last_name, first_name, email, password, name FROM role r " +
                        "JOIN role_user ru ON r.id = ru.role_id " +
                        "JOIN \"user\" u ON u.id = ru.user_id " +
                        "WHERE email = :email",
                Map.of("email", email),
                new RoleUserRowMapper()
        );

        Set<GrantedAuthority> roles = userDataLines.stream()
                .map(RoleUser::getRole)
                .map(it -> (GrantedAuthority) () -> it)
                .collect(Collectors.toSet());

        if (userDataLines.isEmpty()) return null;
        else return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                roles.forEach(System.out::println);
                return roles;
            }
            @Override
            public String getPassword() {
                return userDataLines.get(0).getPassword();
            }
            @Override
            public String getUsername() {
                return userDataLines.get(0).getEmail();
            }
            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };

    }

    private static class RoleUserRowMapper implements RowMapper<RoleUser> {
        @Override
        public RoleUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            String lastName = rs.getString("last_name");
            String firstName = rs.getString("first_name");
            String email = rs.getString("email");
            String password = rs.getString("password");
            String role = rs.getString("name");
            RoleUser roleUser = new RoleUser();
            roleUser.setLastName(lastName);
            roleUser.setFirstName(firstName);
            roleUser.setEmail(email);
            roleUser.setPassword(password);
            roleUser.setRole(role);
            return roleUser;
        }
    }

}
