package com.bilvantis.serviceImpl;

import com.bilvantis.model.UserInformation;
import com.bilvantis.model.UserInformationDTO;
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

import static com.bilvantis.util.AppModernizationAPIConstants.*;

@Component
public class JwtTokenService {


    private final UserInformationService<UserInformation, UserInformationDTO> userInformationService;

    @Autowired
    public JwtTokenService(UserInformationService<UserInformation, UserInformationDTO> userInformationService) {
        this.userInformationService = userInformationService;
    }

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
        if (ADMIN.equalsIgnoreCase(userRole)) {
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
        claims.put(ROLE, ADMIN);
        return createToken(claims, userId);
    }

    /**
     * Create token
     *
     * @param claims Claims
     * @param userId UserId
     * @return Token
     */

    private String createToken(Map<String, Object> claims, String userId) {
        return Jwts.builder().setClaims(claims).setSubject(userId).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)).signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
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
