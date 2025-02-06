package com.bank.backend.filter;

import com.bank.backend.model.Token;
import com.bank.backend.model.User;
import com.bank.backend.model.UserRole;
import com.bank.backend.service.JwtService;
import com.bank.backend.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilterTests {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void doFilterInternalSuccess() throws ServletException, IOException {
        User user = createUser();
        Token token = user.getTokenList().get(0);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token.getRefreshToken());
        Mockito.when(jwtService.extractUsername(Mockito.anyString())).thenReturn(user.getUsername());
        Mockito.when(securityContext.getAuthentication()).thenReturn(null);
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.anyString())).thenReturn(userDetails);
        Mockito.when(jwtService.isValid(Mockito.anyString(), Mockito.any(UserDetails.class))).thenReturn(true);

        SecurityContextHolder.setContext(securityContext);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(request, Mockito.times(1)).getHeader("Authorization");
        Mockito.verify(jwtService, Mockito.times(1)).extractUsername(Mockito.anyString());
        Mockito.verify(userDetailsService, Mockito.times(1)).loadUserByUsername(Mockito.anyString());
        Mockito.verify(jwtService, Mockito.times(1)).isValid(Mockito.anyString(), Mockito.any(UserDetails.class));
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternalNullAuthHeader() throws ServletException, IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(request, Mockito.times(1)).getHeader("Authorization");
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternalWrongAuthHeader() throws ServletException, IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn("Token");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(request, Mockito.times(1)).getHeader("Authorization");
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternalInvalidToken() throws ServletException, IOException {
        User user = createUser();
        Token token = user.getTokenList().get(0);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token.getRefreshToken());
        Mockito.when(jwtService.extractUsername(Mockito.anyString())).thenReturn(user.getUsername());
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(null);
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.anyString())).thenReturn(userDetails);
        Mockito.when(jwtService.isValid(Mockito.anyString(), Mockito.any(UserDetails.class))).thenReturn(false);

        SecurityContextHolder.setContext(securityContext);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(request, Mockito.times(1)).getHeader("Authorization");
        Mockito.verify(jwtService, Mockito.times(1)).extractUsername(Mockito.anyString());
        Mockito.verify(userDetailsService, Mockito.times(1)).loadUserByUsername(Mockito.anyString());
        Mockito.verify(jwtService, Mockito.times(1)).isValid(Mockito.anyString(), Mockito.any(UserDetails.class));
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternalNullUsername() throws ServletException, IOException {
        User user = createUser();
        Token token = user.getTokenList().get(0);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token.getRefreshToken());
        Mockito.when(jwtService.extractUsername(Mockito.anyString())).thenReturn(null);
        Mockito.when(securityContext.getAuthentication()).thenReturn(null);

        SecurityContextHolder.setContext(securityContext);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(request, Mockito.times(1)).getHeader("Authorization");
        Mockito.verify(jwtService, Mockito.times(1)).extractUsername(Mockito.anyString());
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternalNotNullContext() throws ServletException, IOException {
        User user = createUser();
        Token token = user.getTokenList().get(0);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token.getRefreshToken());
        Mockito.when(jwtService.extractUsername(Mockito.anyString())).thenReturn(user.getUsername());
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(request, Mockito.times(1)).getHeader("Authorization");
        Mockito.verify(jwtService, Mockito.times(1)).extractUsername(Mockito.anyString());
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Name");
        user.setLastName("Name");
        user.setEmail("mail@mail.com");
        user.setPhoneNumber("123456");
        user.setUsername("Username");
        user.setPassword("Password");
        user.setUserRole(UserRole.USER);

        Token token = new Token();
        token.setId(1L);
        token.setAccessToken("accessToken");
        token.setRefreshToken("refreshToken");
        token.setLoggedOut(false);
        token.setUser(user);

        user.setTokenList(List.of(token));

        return user;
    }

}