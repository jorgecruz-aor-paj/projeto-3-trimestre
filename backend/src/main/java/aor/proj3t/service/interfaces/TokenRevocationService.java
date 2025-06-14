package aor.proj3t.service.interfaces;

import java.time.Instant;

import aor.proj3t.enums.TokenType;

public interface TokenRevocationService {

    void storeTokenId(String tokenId, String subject, TokenType type, Instant expiry);

    void revokeToken(String tokenId, String reason);

    boolean isTokenRevoked(String tokenId);

}
