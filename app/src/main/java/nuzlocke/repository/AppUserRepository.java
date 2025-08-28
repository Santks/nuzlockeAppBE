package nuzlocke.repository;

import org.springframework.data.repository.CrudRepository;

import nuzlocke.domain.AppUser;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    AppUser findByUsername(String username);
}
