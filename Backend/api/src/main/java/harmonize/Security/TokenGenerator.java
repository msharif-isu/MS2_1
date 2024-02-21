package harmonize.Security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class TokenGenerator {
    public static final long EXPIRATION_DATE = 60000;
    private static final SecretKey SECRET_KEY = Jwts.SIG.HS512.key().build();

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + EXPIRATION_DATE);

        String token = Jwts.builder()
                            .subject(username)
                            .expiration(expireDate)
                            .signWith(SECRET_KEY)
                            .compact();

        return token;
    }

    public String getUsernameByToken(String token) {
        Claims claims = Jwts.parser()
                            .verifyWith(SECRET_KEY)
                            .build()
                            .parseSignedClaims(token)
                            .getPayload();

        return claims.getSubject();    
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                            .verifyWith(SECRET_KEY)
                            .build()
                            .parse(token);
            
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Token has expired");
        }
    }
}
