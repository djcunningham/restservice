package com.example.restservice.security;

import com.example.business.IUserService;
import com.example.business.models.UserModel;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final IUserService userService;

    public CustomAuthenticationProvider(IUserService userService) {
        this.userService = userService;
    }


    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<UserModel> user = userService.findUserByUserName(name);
        if(user.isPresent()) {
            Collection<SimpleGrantedAuthority> authorities = user
                    .get()
                    .getRoles()
                    .stream()
                    .map(r -> new SimpleGrantedAuthority(String.format("ROLE_%s", r)))
                    .collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(name, password, authorities);
        }
        else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}