package nuzlocke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import nuzlocke.domain.AppUser;
import nuzlocke.domain.AppUserRole;
import nuzlocke.domain.Game;
import nuzlocke.repository.AppUserRepository;
import nuzlocke.repository.AppUserRoleRepository;
import nuzlocke.repository.GameRepository;

@SuppressWarnings("unused")
@SpringBootApplication
public class AppApplication {

	private static final Logger log = LoggerFactory.getLogger(AppApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Profile("dev")
	@Bean
	public CommandLineRunner appTesting(AppUserRepository appUserRepo, AppUserRoleRepository appUserRoleRepository,
			GameRepository gameRepo) {
		return (_args) -> {
			log.info("Save some test data");

			AppUserRole userRole = new AppUserRole("user", "Basic permissions");
			AppUserRole adminRole = new AppUserRole("admin", "Admin permissions ");

			appUserRoleRepository.save(userRole);
			appUserRoleRepository.save(adminRole);

			AppUser user = new AppUser("user", "ee11cbb19052e40b07aac0ca060c23ee", "user@testing.com", userRole);
			AppUser admin = new AppUser("admin", "21232f297a57a5a743894a0e4a801fc3", "admin@testing.com", userRole);

			appUserRepo.save(user);
			appUserRepo.save(admin);

			Game renplat = new Game("Renegade Platinum", "Drayano",
					"Gen 4 romhack of pokemon platinum version with fairy typing added", 4);

			gameRepo.save(renplat);

			log.info("Fetch all users");
			for (AppUser users : appUserRepo.findAll()) {
				log.info(users.toString());

				log.info("fetch games");
				for (Game games : gameRepo.findAll()) {
					log.info(games.toString());
				}

			}
		};
	}

}
