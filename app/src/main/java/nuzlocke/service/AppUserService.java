package nuzlocke.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import nuzlocke.config.SaltGenerator;
import nuzlocke.domain.AppUser;
import nuzlocke.domain.AppUserRole;
import nuzlocke.dto.LoginDTO;
import nuzlocke.dto.LoginRespDTO;
import nuzlocke.repository.AppUserRepository;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authManager, JwtService jwtService) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public AppUser createUser(AppUser newUser) {
        AppUser existingUser = appUserRepository.findByUsername(newUser.getUsername());
        if (existingUser != null && newUser.getUsername().equals(existingUser.getUsername())) {
            throw new IllegalArgumentException("Username or email already taken");
        }
        String pwdSalt = SaltGenerator.generateSalt();
        newUser.setSalt(pwdSalt);
        newUser.setUserRole(AppUserRole.ROLE_USER);
        newUser.setPasswordHash(passwordEncoder.encode(newUser.getPasswordHash() + pwdSalt));
        return appUserRepository.save(newUser);
    }

    public LoginRespDTO authenticate(LoginDTO credentials) {
        var auth = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password()));
        var userDetails = (UserDetails) auth.getPrincipal();
        var appUser = appUserRepository.findByUsername(userDetails.getUsername());
        String token = jwtService.generateToken(auth);
        return new LoginRespDTO(token, appUser.getUsername(), appUser.getEmailString());
    }

}
