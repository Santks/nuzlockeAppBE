package nuzlocke.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import nuzlocke.config.SaltGenerator;
import nuzlocke.domain.AppUser;
import nuzlocke.repository.AppUserRepository;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public AppUser createUser(AppUser newUser) {
        String pwdSalt = SaltGenerator.generateSalt();
        newUser.setSalt(pwdSalt);
        newUser.setPasswordHash(passwordEncoder.encode(newUser.getPasswordHash() + pwdSalt));
        return appUserRepository.save(newUser);
    }

}
