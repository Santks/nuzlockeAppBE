package nuzlocke.repository;

import org.springframework.data.repository.CrudRepository;

import nuzlocke.domain.AppUserRole;

public interface AppUserRoleRepository extends CrudRepository<AppUserRole, Long> {

}
