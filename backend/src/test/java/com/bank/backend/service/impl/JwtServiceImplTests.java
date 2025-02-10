package com.bank.backend.service.impl;

import com.bank.backend.model.Token;
import com.bank.backend.model.User;
import com.bank.backend.model.UserRole;
import com.bank.backend.repository.TokenRepository;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@TestPropertySource("classpath:application.properties")
public class JwtServiceImplTests {

    @Value("${application.security.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${application.security.jwt.access-token-expiration}")
    private long ACCESS_TOKEN_EXPIRATION;

    @Value("${application.security.jwt.refresh-token-expiration}")
    private long REFRESH_TOKEN_EXPIRATION;

    private final Date EXPIRATION_DATE = new Date(1767139200000L);

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private Claims claims;

    @Mock
    private Jws<Claims> jwsClaims;

    @Mock
    private JwtParserBuilder jwtParserBuilder;

    @Mock
    private JwtParser jwtParser;

    @Mock
    private JwtBuilder jwtBuilder;

    @InjectMocks
    private JwtServiceImpl jwtService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "ACCESS_TOKEN_EXPIRATION", ACCESS_TOKEN_EXPIRATION);
        ReflectionTestUtils.setField(jwtService, "REFRESH_TOKEN_EXPIRATION", REFRESH_TOKEN_EXPIRATION);
    }

    @Test
    public void testExtractUsername() {
        User user = createUser();

        try (MockedStatic<Jwts> mockedJwts = Mockito.mockStatic(Jwts.class)) {
            mockedJwts.when(Jwts::parserBuilder).thenReturn(jwtParserBuilder);
            Mockito.when(jwtParserBuilder.setSigningKey(Mockito.any(Key.class))).thenReturn(jwtParserBuilder);
            Mockito.when(jwtParserBuilder.build()).thenReturn(jwtParser);
            Mockito.when(jwtParser.parseClaimsJws(Mockito.anyString())).thenReturn(jwsClaims);
            Mockito.when(jwsClaims.getBody()).thenReturn(claims);
            Mockito.when(claims.getSubject()).thenReturn(user.getUsername());

            String username = jwtService.extractUsername("accessToken");

            Assertions.assertNotNull(username);
            Assertions.assertEquals(user.getUsername(), username);

            Mockito.verify(jwtParserBuilder, Mockito.times(1)).setSigningKey(Mockito.any(Key.class));
            Mockito.verify(jwtParserBuilder, Mockito.times(1)).build();
            Mockito.verify(jwtParser, Mockito.times(1)).parseClaimsJws(Mockito.anyString());
            Mockito.verify(jwsClaims, Mockito.times(1)).getBody();
            Mockito.verify(claims, Mockito.times(1)).getSubject();
        }
    }

    @Test
    public void testisValid() {
        UserDetails user = createUser();
        Token token = createUser().getTokenList().get(0);

        try (MockedStatic<Jwts> mockedJwts = Mockito.mockStatic(Jwts.class)) {
            mockedJwts.when(Jwts::parserBuilder).thenReturn(jwtParserBuilder);
            Mockito.when(jwtParserBuilder.setSigningKey(Mockito.any(Key.class))).thenReturn(jwtParserBuilder);
            Mockito.when(jwtParserBuilder.build()).thenReturn(jwtParser);
            Mockito.when(jwtParser.parseClaimsJws(Mockito.anyString())).thenReturn(jwsClaims);
            Mockito.when(jwsClaims.getBody()).thenReturn(claims);
            Mockito.when(claims.getSubject()).thenReturn(user.getUsername());
            Mockito.when(claims.getExpiration()).thenReturn(EXPIRATION_DATE);
            Mockito.when(tokenRepository.findByAccessToken(Mockito.anyString())).thenReturn(Optional.of(token));

            boolean isValid = jwtService.isValid("accessToken", user);

            Assertions.assertTrue(isValid);

            Mockito.verify(jwtParserBuilder, Mockito.times(2)).setSigningKey(Mockito.any(Key.class));
            Mockito.verify(jwtParserBuilder, Mockito.times(2)).build();
            Mockito.verify(jwtParser, Mockito.times(2)).parseClaimsJws(Mockito.anyString());
            Mockito.verify(jwsClaims, Mockito.times(2)).getBody();
            Mockito.verify(claims, Mockito.times(1)).getSubject();
            Mockito.verify(claims, Mockito.times(1)).getExpiration();
            Mockito.verify(tokenRepository, Mockito.times(1)).findByAccessToken(Mockito.anyString());
        }
    }

    @Test
    public void testisValidLoggedOut() {
        UserDetails user = createUser();
        Token token = createUser().getTokenList().get(0);
        token.setLoggedOut(true);

        try (MockedStatic<Jwts> mockedJwts = Mockito.mockStatic(Jwts.class)) {
            mockedJwts.when(Jwts::parserBuilder).thenReturn(jwtParserBuilder);
            Mockito.when(jwtParserBuilder.setSigningKey(Mockito.any(Key.class))).thenReturn(jwtParserBuilder);
            Mockito.when(jwtParserBuilder.build()).thenReturn(jwtParser);
            Mockito.when(jwtParser.parseClaimsJws(Mockito.anyString())).thenReturn(jwsClaims);
            Mockito.when(jwsClaims.getBody()).thenReturn(claims);
            Mockito.when(claims.getSubject()).thenReturn(user.getUsername());
            Mockito.when(claims.getExpiration()).thenReturn(EXPIRATION_DATE);
            Mockito.when(tokenRepository.findByAccessToken(Mockito.anyString())).thenReturn(Optional.of(token));

            boolean isValid = jwtService.isValid("accessToken", user);

            Assertions.assertFalse(isValid);

            Mockito.verify(jwtParserBuilder, Mockito.times(2)).setSigningKey(Mockito.any(Key.class));
            Mockito.verify(jwtParserBuilder, Mockito.times(2)).build();
            Mockito.verify(jwtParser, Mockito.times(2)).parseClaimsJws(Mockito.anyString());
            Mockito.verify(jwsClaims, Mockito.times(2)).getBody();
            Mockito.verify(claims, Mockito.times(1)).getSubject();
            Mockito.verify(claims, Mockito.times(1)).getExpiration();
            Mockito.verify(tokenRepository, Mockito.times(1)).findByAccessToken(Mockito.anyString());
        }
    }

    @Test
    public void testisValidExpiredToken() {
        UserDetails user = createUser();
        Token token = createUser().getTokenList().get(0);

        try (MockedStatic<Jwts> mockedJwts = Mockito.mockStatic(Jwts.class)) {
            mockedJwts.when(Jwts::parserBuilder).thenReturn(jwtParserBuilder);
            Mockito.when(jwtParserBuilder.setSigningKey(Mockito.any(Key.class))).thenReturn(jwtParserBuilder);
            Mockito.when(jwtParserBuilder.build()).thenReturn(jwtParser);
            Mockito.when(jwtParser.parseClaimsJws(Mockito.anyString())).thenReturn(jwsClaims);
            Mockito.when(jwsClaims.getBody()).thenReturn(claims);
            Mockito.when(claims.getSubject()).thenReturn(user.getUsername());
            Mockito.when(claims.getExpiration()).thenReturn(new Date(939945600000L));
            Mockito.when(tokenRepository.findByAccessToken(Mockito.anyString())).thenReturn(Optional.of(token));

            boolean isValid = jwtService.isValid("accessToken", user);

            Assertions.assertFalse(isValid);

            Mockito.verify(jwtParserBuilder, Mockito.times(2)).setSigningKey(Mockito.any(Key.class));
            Mockito.verify(jwtParserBuilder, Mockito.times(2)).build();
            Mockito.verify(jwtParser, Mockito.times(2)).parseClaimsJws(Mockito.anyString());
            Mockito.verify(jwsClaims, Mockito.times(2)).getBody();
            Mockito.verify(claims, Mockito.times(1)).getSubject();
            Mockito.verify(claims, Mockito.times(1)).getExpiration();
            Mockito.verify(tokenRepository, Mockito.times(1)).findByAccessToken(Mockito.anyString());
        }
    }

    @Test
    public void testisValidInvalidName() {
        UserDetails user = createUser();
        Token token = createUser().getTokenList().get(0);

        try (MockedStatic<Jwts> mockedJwts = Mockito.mockStatic(Jwts.class)) {
            mockedJwts.when(Jwts::parserBuilder).thenReturn(jwtParserBuilder);
            Mockito.when(jwtParserBuilder.setSigningKey(Mockito.any(Key.class))).thenReturn(jwtParserBuilder);
            Mockito.when(jwtParserBuilder.build()).thenReturn(jwtParser);
            Mockito.when(jwtParser.parseClaimsJws(Mockito.anyString())).thenReturn(jwsClaims);
            Mockito.when(jwsClaims.getBody()).thenReturn(claims);
            Mockito.when(claims.getSubject()).thenReturn("username");
            Mockito.when(tokenRepository.findByAccessToken(Mockito.anyString())).thenReturn(Optional.of(token));

            boolean isValid = jwtService.isValid("accessToken", user);

            Assertions.assertFalse(isValid);

            Mockito.verify(jwtParserBuilder, Mockito.times(1)).setSigningKey(Mockito.any(Key.class));
            Mockito.verify(jwtParserBuilder, Mockito.times(1)).build();
            Mockito.verify(jwtParser, Mockito.times(1)).parseClaimsJws(Mockito.anyString());
            Mockito.verify(jwsClaims, Mockito.times(1)).getBody();
            Mockito.verify(claims, Mockito.times(1)).getSubject();
            Mockito.verify(tokenRepository, Mockito.times(1)).findByAccessToken(Mockito.anyString());
        }
    }

    @Test
    public void testisValidRefreshToken() {
        User user = createUser();
        Token token = user.getTokenList().get(0);

        try (MockedStatic<Jwts> mockedJwts = Mockito.mockStatic(Jwts.class)) {
            mockedJwts.when(Jwts::parserBuilder).thenReturn(jwtParserBuilder);
            Mockito.when(jwtParserBuilder.setSigningKey(Mockito.any(Key.class))).thenReturn(jwtParserBuilder);
            Mockito.when(jwtParserBuilder.build()).thenReturn(jwtParser);
            Mockito.when(jwtParser.parseClaimsJws(Mockito.anyString())).thenReturn(jwsClaims);
            Mockito.when(jwsClaims.getBody()).thenReturn(claims);
            Mockito.when(claims.getSubject()).thenReturn(user.getUsername());
            Mockito.when(claims.getExpiration()).thenReturn(EXPIRATION_DATE);
            Mockito.when(tokenRepository.findByRefreshToken(Mockito.anyString())).thenReturn(Optional.of(token));

            boolean isValid = jwtService.isValidRefreshToken("refreshToken", user);

            Assertions.assertTrue(isValid);

            Mockito.verify(jwtParserBuilder, Mockito.times(2)).setSigningKey(Mockito.any(Key.class));
            Mockito.verify(jwtParserBuilder, Mockito.times(2)).build();
            Mockito.verify(jwtParser, Mockito.times(2)).parseClaimsJws(Mockito.anyString());
            Mockito.verify(jwsClaims, Mockito.times(2)).getBody();
            Mockito.verify(claims, Mockito.times(1)).getSubject();
            Mockito.verify(claims, Mockito.times(1)).getExpiration();
            Mockito.verify(tokenRepository, Mockito.times(1)).findByRefreshToken(Mockito.anyString());
        }
    }

    @Test
    public void testisValidRefreshTokenLoggedOut() {
        User user = createUser();
        Token token = user.getTokenList().get(0);
        token.setLoggedOut(true);

        try (MockedStatic<Jwts> mockedJwts = Mockito.mockStatic(Jwts.class)) {
            mockedJwts.when(Jwts::parserBuilder).thenReturn(jwtParserBuilder);
            Mockito.when(jwtParserBuilder.setSigningKey(Mockito.any(Key.class))).thenReturn(jwtParserBuilder);
            Mockito.when(jwtParserBuilder.build()).thenReturn(jwtParser);
            Mockito.when(jwtParser.parseClaimsJws(Mockito.anyString())).thenReturn(jwsClaims);
            Mockito.when(jwsClaims.getBody()).thenReturn(claims);
            Mockito.when(claims.getSubject()).thenReturn(user.getUsername());
            Mockito.when(claims.getExpiration()).thenReturn(EXPIRATION_DATE);
            Mockito.when(tokenRepository.findByRefreshToken(Mockito.anyString())).thenReturn(Optional.of(token));

            boolean isValid = jwtService.isValidRefreshToken("refreshToken", user);

            Assertions.assertFalse(isValid);

            Mockito.verify(jwtParserBuilder, Mockito.times(2)).setSigningKey(Mockito.any(Key.class));
            Mockito.verify(jwtParserBuilder, Mockito.times(2)).build();
            Mockito.verify(jwtParser, Mockito.times(2)).parseClaimsJws(Mockito.anyString());
            Mockito.verify(jwsClaims, Mockito.times(2)).getBody();
            Mockito.verify(claims, Mockito.times(1)).getSubject();
            Mockito.verify(claims, Mockito.times(1)).getExpiration();
            Mockito.verify(tokenRepository, Mockito.times(1)).findByRefreshToken(Mockito.anyString());
        }
    }

    @Test
    public void testisValidRefreshTokenExpiredToken() {
        User user = createUser();
        Token token = user.getTokenList().get(0);

        try (MockedStatic<Jwts> mockedJwts = Mockito.mockStatic(Jwts.class)) {
            mockedJwts.when(Jwts::parserBuilder).thenReturn(jwtParserBuilder);
            Mockito.when(jwtParserBuilder.setSigningKey(Mockito.any(Key.class))).thenReturn(jwtParserBuilder);
            Mockito.when(jwtParserBuilder.build()).thenReturn(jwtParser);
            Mockito.when(jwtParser.parseClaimsJws(Mockito.anyString())).thenReturn(jwsClaims);
            Mockito.when(jwsClaims.getBody()).thenReturn(claims);
            Mockito.when(claims.getSubject()).thenReturn(user.getUsername());
            Mockito.when(claims.getExpiration()).thenReturn(new Date(939945600000L));
            Mockito.when(tokenRepository.findByRefreshToken(Mockito.anyString())).thenReturn(Optional.of(token));

            boolean isValid = jwtService.isValidRefreshToken("accessToken", user);

            Assertions.assertFalse(isValid);

            Mockito.verify(jwtParserBuilder, Mockito.times(2)).setSigningKey(Mockito.any(Key.class));
            Mockito.verify(jwtParserBuilder, Mockito.times(2)).build();
            Mockito.verify(jwtParser, Mockito.times(2)).parseClaimsJws(Mockito.anyString());
            Mockito.verify(jwsClaims, Mockito.times(2)).getBody();
            Mockito.verify(claims, Mockito.times(1)).getSubject();
            Mockito.verify(claims, Mockito.times(1)).getExpiration();
            Mockito.verify(tokenRepository, Mockito.times(1)).findByRefreshToken(Mockito.anyString());
        }
    }

    @Test
    public void testisValidRefreshTokenInvalidName() {
        User user = createUser();
        Token token = user.getTokenList().get(0);

        try (MockedStatic<Jwts> mockedJwts = Mockito.mockStatic(Jwts.class)) {
            mockedJwts.when(Jwts::parserBuilder).thenReturn(jwtParserBuilder);
            Mockito.when(jwtParserBuilder.setSigningKey(Mockito.any(Key.class))).thenReturn(jwtParserBuilder);
            Mockito.when(jwtParserBuilder.build()).thenReturn(jwtParser);
            Mockito.when(jwtParser.parseClaimsJws(Mockito.anyString())).thenReturn(jwsClaims);
            Mockito.when(jwsClaims.getBody()).thenReturn(claims);
            Mockito.when(claims.getSubject()).thenReturn("username");
            Mockito.when(tokenRepository.findByRefreshToken(Mockito.anyString())).thenReturn(Optional.of(token));

            boolean isValid = jwtService.isValidRefreshToken("refreshToken", user);

            Assertions.assertFalse(isValid);

            Mockito.verify(jwtParserBuilder, Mockito.times(1)).setSigningKey(Mockito.any(Key.class));
            Mockito.verify(jwtParserBuilder, Mockito.times(1)).build();
            Mockito.verify(jwtParser, Mockito.times(1)).parseClaimsJws(Mockito.anyString());
            Mockito.verify(jwsClaims, Mockito.times(1)).getBody();
            Mockito.verify(claims, Mockito.times(1)).getSubject();
            Mockito.verify(tokenRepository, Mockito.times(1)).findByRefreshToken(Mockito.anyString());
        }
    }

    @Test
    public void testGenerateAccessToken() {
        User user = createUser();

        try (MockedStatic<Jwts> mockedJwts = mockStatic(Jwts.class)) {
            mockedJwts.when(Jwts::builder).thenReturn(jwtBuilder);
            Mockito.when(jwtBuilder.setSubject(Mockito.anyString())).thenReturn(jwtBuilder);
            Mockito.when(jwtBuilder.setIssuedAt(Mockito.any(Date.class))).thenReturn(jwtBuilder);
            Mockito.when(jwtBuilder.setExpiration(Mockito.any(Date.class))).thenReturn(jwtBuilder);
            Mockito.when(jwtBuilder.signWith(Mockito.any(Key.class))).thenReturn(jwtBuilder);
            Mockito.when(jwtBuilder.compact()).thenReturn("accessToken");

            String accessToken = jwtService.generateAccessToken(user);

            Assertions.assertNotNull(accessToken);
            Mockito.verify(jwtBuilder, Mockito.times(1)).setSubject(Mockito.anyString());
            Mockito.verify(jwtBuilder, Mockito.times(1)).setIssuedAt(Mockito.any(Date.class));
            Mockito.verify(jwtBuilder, Mockito.times(1)).setExpiration(Mockito.any(Date.class));
            Mockito.verify(jwtBuilder, Mockito.times(1)).signWith(Mockito.any(Key.class));
            Mockito.verify(jwtBuilder, Mockito.times(1)).compact();
        }
    }

    @Test
    public void testGenerateRefreshToken() {
        User user = createUser();

        try (MockedStatic<Jwts> mockedJwts = mockStatic(Jwts.class)) {
            mockedJwts.when(Jwts::builder).thenReturn(jwtBuilder);
            Mockito.when(jwtBuilder.setSubject(Mockito.anyString())).thenReturn(jwtBuilder);
            Mockito.when(jwtBuilder.setIssuedAt(Mockito.any(Date.class))).thenReturn(jwtBuilder);
            Mockito.when(jwtBuilder.setExpiration(Mockito.any(Date.class))).thenReturn(jwtBuilder);
            Mockito.when(jwtBuilder.signWith(Mockito.any(Key.class))).thenReturn(jwtBuilder);
            Mockito.when(jwtBuilder.compact()).thenReturn("refreshToken");

            String refreshToken = jwtService.generateRefreshToken(user);

            Assertions.assertNotNull(refreshToken);
            Mockito.verify(jwtBuilder, Mockito.times(1)).setSubject(Mockito.anyString());
            Mockito.verify(jwtBuilder, Mockito.times(1)).setIssuedAt(Mockito.any(Date.class));
            Mockito.verify(jwtBuilder, Mockito.times(1)).setExpiration(Mockito.any(Date.class));
            Mockito.verify(jwtBuilder, Mockito.times(1)).signWith(Mockito.any(Key.class));
            Mockito.verify(jwtBuilder, Mockito.times(1)).compact();
        }
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