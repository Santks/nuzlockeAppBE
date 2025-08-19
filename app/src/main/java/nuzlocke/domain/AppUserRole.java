package nuzlocke.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class AppUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRoleId;

    @NotBlank
    private String userRoleName;

    @NotNull
    private String roleDesc;

    @OneToMany(mappedBy = "appUserRole")
    private Set<AppUser> users = new HashSet<>();

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getUserRoleName() {
        return userRoleName;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public Set<AppUser> getUsers() {
        return users;
    }

    public void setUsers(Set<AppUser> users) {
        this.users = users;
    }

    public AppUserRole() {

    }

    public AppUserRole(String userRoleName, String roleDesc) {
        this.userRoleName = userRoleName;
        this.roleDesc = roleDesc;
    }

    @Override
    public String toString() {
        return "AppUserRole [userRoleId=" + userRoleId + ", userRoleName=" + userRoleName + ", roleDesc=" + roleDesc
                + "]";
    }

}
