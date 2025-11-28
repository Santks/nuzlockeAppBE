package nuzlocke.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_id", nullable = false)
    private Long appUserId;

    @NotBlank
    @Column(name = "Username", unique = true, nullable = false)
    private String username;

    @NotBlank
    @Column(name = "Password", nullable = false)
    @JsonIgnore
    private String passwordHash;

    @NotBlank
    @Email
    @Column(name = "Email", unique = true, nullable = false)
    private String emailString;

    @Enumerated(EnumType.STRING)
    @Column(name = "User_Role", nullable = false)
    @JsonIgnore
    private AppUserRole userRole;

    @NotBlank
    @Column(name = "Salt", unique = true, nullable = false)
    @JsonIgnore
    private String salt;

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmailString() {
        return emailString;
    }

    public void setEmailString(String emailString) {
        this.emailString = emailString;
    }

    public AppUserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(AppUserRole userRole) {
        this.userRole = userRole;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    protected AppUser() {

    }

    public AppUser(String username, String passwordHash, String emailString, AppUserRole appUserRole) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.emailString = emailString;
        this.userRole = appUserRole;
    }

    // ONLY FOR H2 DB + CommandLineRUnner TESTING
    public AppUser(String username, String passwordHash, String emailString, AppUserRole appUserRole, String salt) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.emailString = emailString;
        this.userRole = appUserRole;
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "AppUser [appUserId=" + appUserId + ", username=" + username + ", emailString=" + emailString
                + ", appUserRole=" + userRole + "salt" + salt + "]";
    }

}
