package com.bank.backend.service.impl;

import com.bank.backend.model.Token;
import com.bank.backend.model.User;
import com.bank.backend.model.UserRole;
import com.bank.backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.List;
import java.util.Optional;

public class UserDetailsServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsernameSuccess() {
        User user = createUser();

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserDetails actualUserDetails = userDetailsService.loadUserByUsername(user.getUsername());

        Assertions.assertNotNull(actualUserDetails);
        Assertions.assertEquals(user.getUsername(), actualUserDetails.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(user.getUsername());
    }

    @Test
    public void testLoadUserByUsernameFail() {
        User user = createUser();

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        UsernameNotFoundException exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(user.getUsername());
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("User Not Found.", exception.getMessage());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(user.getUsername());
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
