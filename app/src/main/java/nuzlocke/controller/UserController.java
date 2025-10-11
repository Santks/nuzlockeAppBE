package nuzlocke.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nuzlocke.domain.AppUser;
import nuzlocke.dto.LoginDTO;
import nuzlocke.dto.LoginRespDTO;
import nuzlocke.service.AppUserService;

@RestController
@RequestMapping("/auth")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final AppUserService appUserService;

    @Autowired
    public UserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<AppUser> createNewUser(@RequestBody AppUser newUser) {
        log.info("Registering new user with username and email: " + newUser.getUsername() + " "
                + newUser.getEmailString());
        return ResponseEntity.status(HttpStatus.CREATED).body(appUserService.createUser(newUser));
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginRespDTO> authenticate(@RequestBody LoginDTO loginUserDto) {
        var loginResponse = appUserService.authenticate(loginUserDto);
        return ResponseEntity.ok(loginResponse);
    }

}
