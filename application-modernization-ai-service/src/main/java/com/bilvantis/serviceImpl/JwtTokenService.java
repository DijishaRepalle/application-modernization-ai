package com.bilvantis.serviceImpl;

import com.bilvantis.repository.UserInformationRepository;
import com.bilvantis.service.UserInformationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenService {

    private static final String SECRET = "4027d080f9a3954189ebca5ddbbfdfc9cb824314c0d6c910eff8bec1c689eabc";
    private static final String ROLE = "ADMIN";
    @Autowired
    private UserInformationService userInformationService;
    @Autowired
    private UserInformationRepository userInformationRepository;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validate Token
     *
     * @param token String
     * @return Boolean
     */

    public Boolean validateToken(String token) {
        String userId = extractUsername(token);

        String userRole = String.valueOf(userInformationService.getRoleBasedOnUserId(userId));
        // Bypass token validation for admins
        if ("admin".equalsIgnoreCase(userRole)) {
            return true;
        }
        return !isTokenExpired(token);
    }


    /**
     * Generate Token
     *
     * @param userId WorkerId
     * @return Token
     */
    public String generateToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", ROLE);
        return createToken(claims, userId);
    }
    /**
     * Create token
     *
     * @param claims   Claims
     * @param userId UserId
     * @return Token
     */

    private String createToken(Map<String, Object> claims, String userId) {
        return Jwts.builder().setClaims(claims).setSubject(userId).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Get Sign Key
     *
     * @return Key
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
