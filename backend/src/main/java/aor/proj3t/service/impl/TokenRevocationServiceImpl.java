package aor.proj3t.service.impl;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.enums.TokenType;
import aor.proj3t.service.interfaces.TokenRevocationService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * In-memory implementation of TokenRevocationService.
 * 
 * This service manages JWT token revocation state, allowing explicit revocation
 * of tokens before their expiration time and checking if tokens have been
 * revoked.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 */
@ApplicationScoped
public class TokenRevocationServiceImpl implements TokenRevocationService {

    private static final Logger logger = LogManager.getLogger(TokenRevocationServiceImpl.class);

    // In-memory storage for revoked tokens FIXME - switch to database persistence
    private final Map<String, RevocationInfo> tokenStore = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        logger.info("TokenRevocationService initialized with scheduled cleanup");
    }

    @PreDestroy
    public void shutdown() {
        logger.info("TokenRevocationService shutting down");
    }

    @Override
    public void storeTokenId(String tokenId, String subject, TokenType type, Instant expiry) {
        if (tokenId == null || subject == null || type == null || expiry == null) {
            logger.warn("Attempted to store token with null parameters: tokenId={}, subject={}, type={}, expiry={}",
                    tokenId, subject, type, expiry);
            return;
        }

        logger.debug("Storing token ID: {}, subject: {}, type: {}, expiry: {}",
                tokenId, subject, type, expiry);

        // For session tokens, we might want different behavior than for other token
        // types
        if (type == TokenType.SESSION) {
            // For session tokens, store with active state
            tokenStore.put(tokenId, new RevocationInfo(subject, false, null, expiry));
        } else {
            // For other types like password reset or email confirmation, also store
            tokenStore.put(tokenId, new RevocationInfo(subject, false, null, expiry));
        }
    }

    @Override
    public void revokeToken(String tokenId, String reason) {
        if (tokenId == null) {
            logger.warn("Attempted to revoke null token ID");
            return;
        }

        logger.debug("Revoking token ID: {}, reason: {}", tokenId, reason);

        RevocationInfo info = tokenStore.get(tokenId);
        if (info == null) {
            // Token wasn't stored previously, create a new revocation entry
            tokenStore.put(tokenId, new RevocationInfo(null, true, reason, null));
            logger.debug("Created new revocation entry for previously unknown token ID: {}", tokenId);
        } else {
            // Update existing entry to mark as revoked
            info.setRevoked(true);
            info.setRevocationReason(reason);
            logger.debug("Updated existing token entry as revoked, token ID: {}", tokenId);
        }
    }

    @Override
    public boolean isTokenRevoked(String tokenId) {
        if (tokenId == null) {
            logger.warn("Checked revocation status for null token ID");
            return true; // Treat null tokens as revoked
        }

        RevocationInfo info = tokenStore.get(tokenId);
        boolean revoked = info != null && info.isRevoked();

        logger.debug("Checked revocation status for token ID: {}, revoked: {}", tokenId, revoked);
        return revoked;
    }

    /**
     * Internal class to store token revocation information.
     */
    private static class RevocationInfo {
        private final String subject;
        private boolean revoked;
        private String revocationReason;
        private final Instant expiry;

        public RevocationInfo(String subject, boolean revoked, String revocationReason, Instant expiry) {
            this.subject = subject;
            this.revoked = revoked;
            this.revocationReason = revocationReason;
            this.expiry = expiry;
        }

        public boolean isRevoked() {
            return revoked;
        }

        public void setRevoked(boolean revoked) {
            this.revoked = revoked;
        }

        public String getRevocationReason() {
            return revocationReason;
        }

        public void setRevocationReason(String revocationReason) {
            this.revocationReason = revocationReason;
        }

        public Instant getExpiry() {
            return expiry;
        }

        public String getSubject() {
            return subject;
        }
    }
}