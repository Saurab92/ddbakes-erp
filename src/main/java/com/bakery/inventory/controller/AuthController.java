package com.bakery.inventory.controller;

import com.bakery.inventory.dto.LoginRequest;
import com.bakery.inventory.dto.LoginResponse;
import com.bakery.inventory.entity.User;
import com.bakery.inventory.repository.UserRepository;
import com.bakery.inventory.security.JwtService;
import com.bakery.inventory.security.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Issues JWT access tokens for authenticated users. The token is valid for
 * 240 hours (see {@code jwt.access-token-expiration-ms}); there is no
 * refresh-token endpoint. Tokens can be invalidated early via
 * {@code /api/auth/logout}.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserRepository userRepository;
    private final long accessTokenExpirationMs;

    public AuthController(AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           TokenBlacklistService tokenBlacklistService,
                           UserRepository userRepository,
                           @Value("${jwt.access-token-expiration-ms}") long accessTokenExpirationMs) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.userRepository = userRepository;
        this.accessTokenExpirationMs = accessTokenExpirationMs;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");

        String accessToken = jwtService.generateAccessToken(userDetails, role);

        User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        String firstName = user != null ? user.getFirstName() : null;
        String lastName = user != null ? user.getLastName() : null;

        LoginResponse response = new LoginResponse(
                accessToken, accessTokenExpirationMs, userDetails.getUsername(), firstName, lastName, role);
        return ResponseEntity.ok(response);
    }

    /**
     * Invalidates the caller's access token immediately. The token is added
     * to the in-memory denylist (see {@link TokenBlacklistService}) until it
     * would have expired naturally, so subsequent requests using it are
     * treated as unauthenticated.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length());
            try {
                tokenBlacklistService.blacklist(token, jwtService.extractExpiration(token));
            } catch (RuntimeException e) {
                // Token is already malformed/expired - nothing to invalidate.
            }
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
