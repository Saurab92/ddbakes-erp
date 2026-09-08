package com.bakery.inventory.security;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory registry of access tokens that have been invalidated via
 * {@code /api/auth/logout} before their natural expiry.
 *
 * Since the JWT access token is stateless, logout is implemented by
 * denylisting the token's signature until it would have expired anyway;
 * after that point it is removed so the map does not grow unbounded.
 * This is single-instance only - a multi-node deployment would need a
 * shared store (e.g. Redis) instead.
 */
@Service
public class TokenBlacklistService {

    private final Map<String, Date> blacklist = new ConcurrentHashMap<>();

    public void blacklist(String token, Date expiresAt) {
        purgeExpired();
        blacklist.put(token, expiresAt);
    }

    public boolean isBlacklisted(String token) {
        Date expiresAt = blacklist.get(token);
        if (expiresAt == null) {
            return false;
        }
        if (expiresAt.before(new Date())) {
            blacklist.remove(token);
            return false;
        }
        return true;
    }

    private void purgeExpired() {
        Date now = new Date();
        blacklist.values().removeIf(expiresAt -> expiresAt.before(now));
    }
}
