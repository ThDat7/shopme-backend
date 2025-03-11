package com.shopme.admin.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.shopme.admin.dto.request.AuthenticationRequest;
import com.shopme.admin.dto.request.IntrospectRequest;
import com.shopme.admin.dto.response.AuthenticationResponse;
import com.shopme.admin.dto.response.IntrospectResponse;
import com.shopme.admin.repository.UserRepository;
import com.shopme.admin.service.impl.AuthenticationServiceImpl;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Authentication Service Tests")
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private String signerKey;

    private User mockUser;

    @BeforeEach
    void setUp() {
        signerKey = "5Fym73Ubz5u3Q22mI-cfC4yQhFXcRx_SBHozaHnTHrOh2KNKYKzKYMnsob8HrJcDPpISnKLItHlgxWv-ISj-lw";
        ReflectionTestUtils.setField(authenticationService, "SIGNER_KEY", signerKey);

        mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("passwordEncoded");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");

        Role role = new Role();
        role.setName("USER");
        mockUser.setRoles(Set.of(role));
    }

    @Nested
    @DisplayName("Authentication")
    class AuthenticationTests {
        @Test
        @DisplayName("Should authenticate with valid credentials")
        void authenticate_Success() {
            // Arrange
            AuthenticationRequest request = new AuthenticationRequest("test@example.com", "password");

            when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(mockUser));
            when(passwordEncoder.matches(request.getPassword(), mockUser.getPassword())).thenReturn(true);

            // Act
            AuthenticationResponse response = authenticationService.authenticate(request);

            // Assert
            assertNotNull(response);
            assertTrue(response.isAuthenticated());
            assertEquals("John Doe", response.getUserFullName());
            assertNotNull(response.getToken());

            verify(userRepository).findByEmail(request.getEmail());
            verify(passwordEncoder).matches(request.getPassword(), mockUser.getPassword());
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void authenticate_UserNotFound_ThrowsException() {
            // Arrange
            AuthenticationRequest request = new AuthenticationRequest("wrong@example.com", "password");

            when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> authenticationService.authenticate(request));
            assertEquals("User not found", exception.getMessage());

            verify(userRepository).findByEmail(request.getEmail());
        }

        @Test
        @DisplayName("Should throw exception when invalid password")
        void authenticate_InvalidPassword_ThrowsException() {
            // Arrange
            AuthenticationRequest request = new AuthenticationRequest("test@example.com", "wrong-password");

            when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(mockUser));
            when(passwordEncoder.matches(request.getPassword(), mockUser.getPassword())).thenReturn(false);

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> authenticationService.authenticate(request));
            assertEquals("Unauthenticated", exception.getMessage());

            verify(userRepository).findByEmail(request.getEmail());
            verify(passwordEncoder).matches(request.getPassword(), mockUser.getPassword());
        }
    }

    @Nested
    @DisplayName("Token Introspection")
    class TokenIntrospectionTests {

        @Test
        @DisplayName("Should return valid response for valid token")
        void introspect_ValidToken_ReturnsValidResponse() throws JOSEException, ParseException {
            // Arrange
            String token = generateValidToken();
            IntrospectRequest introspectRequest = new IntrospectRequest(token);

            // Act
            IntrospectResponse response = authenticationService.introspect(introspectRequest);

            // Assert
            assertNotNull(response);
            assertTrue(response.isValid());
        }

        @Test
        @DisplayName("Should return invalid response for expired token")
        void introspect_ExpiredToken_ReturnsInvalidResponse() throws JOSEException, ParseException {
            // Arrange
            Date expireTime = Date.from(Instant.now().minus(1, ChronoUnit.HOURS));
            String expiredToken = generateToken(expireTime);
            IntrospectRequest introspectRequest = new IntrospectRequest(expiredToken);

            // Act
            IntrospectResponse response = authenticationService.introspect(introspectRequest);

            // Assert
            assertNotNull(response);
            assertFalse(response.isValid());
        }

        @Test
        @DisplayName("Should throw exception when invalid token")
        void introspect_InvalidToken_ReturnsInvalidResponse() {
            // Arrange
            IntrospectRequest introspectRequest = new IntrospectRequest("invalid-token");

            // Act & Assert
            assertThrows(ParseException.class, () -> authenticationService.introspect(introspectRequest));
        }

        private String generateToken(Date expireTime) throws JOSEException {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(mockUser.getEmail())
                    .expirationTime(expireTime)
                    .build();

            JWSObject jwsObject = new JWSObject(header, new Payload(jwtClaimsSet.toJSONObject()));
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        }

        private String generateValidToken() throws JOSEException {
            return generateToken(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)));
        }
    }

}
