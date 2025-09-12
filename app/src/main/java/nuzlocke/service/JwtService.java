package nuzlocke.service;

import java.time.Instant;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import nuzlocke.config.JwtConfig;
import nuzlocke.repository.AppUserRepository;

@Service
public class JwtService {

    private final JwtConfig jwtConfig;
    private final AppUserRepository appUserRepository;

    @Autowired
    public JwtService(JwtConfig jwtConfig, AppUserRepository appUserRepository) {
        this.jwtConfig = jwtConfig;
        this.appUserRepository = appUserRepository;
    }

    public String generateToken(Authentication authentication) {
        var header = new JWSHeader.Builder(jwtConfig.getAlgorithm()).type(JOSEObjectType.JWT).build();
        Instant now = Instant.now();
        var roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        var builder = new JWTClaimsSet.Builder()
                .issuer("Admin")
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plus(1, java.time.temporal.ChronoUnit.HOURS)));
        builder.claim("roles", roles);
        var user = (User) authentication.getPrincipal();
        var appUser = appUserRepository.findByUsername(user.getUsername());
        builder.claim("username", appUser.getUsername());
        builder.claim("email", appUser.getEmailString());
        builder.claim("userId", appUser.getAppUserId());
        var claims = builder.build();

        var key = jwtConfig.getSecretKey();
        var jwt = new SignedJWT(header, claims);

        try {
            var signer = new MACSigner(key);
            jwt.sign(signer);
        } catch (JOSEException ex) {
            throw new RuntimeException("Error while generating jwt: " + ex);
        }
        return jwt.serialize();
    }

}
