package nuzlocke.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appUserId;

    @NotBlank
    private String username;

    @NotBlank
    private String passwordHash;

    @NotBlank
    @Email
    private String emailString;

    @ManyToOne
    private AppUserRole appUserRole;

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

    public AppUserRole getAppUserRole() {
        return appUserRole;
    }

    public void setAppUserRole(AppUserRole appUserRole) {
        this.appUserRole = appUserRole;
    }

    public AppUser() {

    }

    public AppUser(Long appUserId, String username, String emailString, AppUserRole appUserRole) {
        this.appUserId = appUserId;
        this.username = username;
        this.emailString = emailString;
        this.appUserRole = appUserRole;
    }

    @Override
    public String toString() {
        return "AppUser [appUserId=" + appUserId + ", username=" + username + ", emailString=" + emailString
                + ", appUserRole=" + appUserRole + "]";
    }

}
