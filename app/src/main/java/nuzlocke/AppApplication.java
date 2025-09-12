package nuzlocke;

import java.util.ArrayList;
import java.util.List;

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
import nuzlocke.domain.Nature;
import nuzlocke.domain.Pokemon;
import nuzlocke.domain.PokemonMoveSet;
import nuzlocke.domain.Region;
import nuzlocke.domain.Route;
import nuzlocke.domain.Trainer;
import nuzlocke.domain.TrainerTeam;
import nuzlocke.domain.Type;
import nuzlocke.repository.AppUserRepository;
import nuzlocke.repository.GameRepository;
import nuzlocke.repository.PokemonMoveSetRepository;
import nuzlocke.repository.PokemonRepository;
import nuzlocke.repository.RegionRepository;
import nuzlocke.repository.RouteRepository;
import nuzlocke.repository.TrainerRepository;
import nuzlocke.repository.TrainerTeamRepository;

@SuppressWarnings("unused")
@SpringBootApplication
public class AppApplication {

	private static final Logger log = LoggerFactory.getLogger(AppApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Profile("dev")
	@Bean
	public CommandLineRunner appTesting(AppUserRepository appUserRepo,
			GameRepository gameRepo, RegionRepository regionRepo, RouteRepository routeRepo,
			TrainerRepository trainerRepo, TrainerTeamRepository ttRepo, PokemonRepository pokemonRepo,
			PokemonMoveSetRepository moveSetRepo) {
		return (_args) -> {
			log.info("Save some test data");

			AppUser user = new AppUser("user", "$2a$12$/2Z3T6HYbtIoyVgtouPFMO8BMo5EpASfWRrWBQCQhOv63r5vyMW8O",
					"user@testing.com",
					AppUserRole.ROLE_USER, "saltyy");
			AppUser admin = new AppUser("admin", "$2a$12$YzwjWastVXpIc7vDg211cuMamujIedfZ0DqEULDTu35rYUgXLW1SS",
					"admin@testing.com",
					AppUserRole.ROLE_ADMIN, "salt");

			appUserRepo.save(user);
			appUserRepo.save(admin);

			Game renplat = new Game("Renegade Platinum", "Drayano",
					"Gen 4 romhack of pokemon platinum version with fairy typing added", 4);

			gameRepo.save(renplat);

			Region sinnoh = new Region("Sinnoh", renplat);

			regionRepo.save(sinnoh);

			Route route201 = new Route("Route 201", sinnoh);
			Route route202 = new Route("Route 202", sinnoh);

			routeRepo.save(route201);
			routeRepo.save(route202);

			Trainer barry = new Trainer("PKMN Trainer Barry", route201);
			trainerRepo.save(barry);

			Trainer dawn = new Trainer("PKMN Trainer Dawn", route202);
			trainerRepo.save(dawn);

			Trainer lucas = new Trainer("PKMN Trainer Lucas", route202);
			trainerRepo.save(lucas);

			Trainer natalie = new Trainer("Lass Natalie", route202);
			trainerRepo.save(natalie);

			Trainer tristan = new Trainer("Youngster Tristan", route202);
			trainerRepo.save(tristan);

			Trainer logan = new Trainer("Youngster Logan", route202);
			trainerRepo.save(logan);

			TrainerTeam barry1 = new TrainerTeam(barry);
			ttRepo.save(barry1);

			TrainerTeam dawn1 = new TrainerTeam(dawn);
			ttRepo.save(dawn1);

			TrainerTeam lucas1 = new TrainerTeam(lucas);
			ttRepo.save(lucas1);

			TrainerTeam lassNatalie = new TrainerTeam(natalie);
			ttRepo.save(lassNatalie);

			TrainerTeam youngsterTristan = new TrainerTeam(tristan);
			ttRepo.save(youngsterTristan);

			TrainerTeam youngsterLogan = new TrainerTeam(logan);
			ttRepo.save(youngsterLogan);

			Pokemon turtwig = new Pokemon("Turtwig", List.of("Overgrow", "Shell Armor"), Type.GRASS, null, dawn1);
			pokemonRepo.save(turtwig);

			Pokemon piplup = new Pokemon("Piplup", List.of("Torrent", "Defiant"), Type.WATER, null, barry1);
			pokemonRepo.save(piplup);

			Pokemon chimchar = new Pokemon("Chimchar", List.of("Blaze", "Iron Fist"), Type.FIRE, null, lucas1);
			pokemonRepo.save(chimchar);

			Pokemon sentret = new Pokemon("Sentret", List.of("Run Away", "Keen Eye", "Frisk"), Type.NORMAL, null,
					lassNatalie);
			pokemonRepo.save(sentret);

			Pokemon bidoof = new Pokemon("Bidoof", List.of("Simple", "Unaware"), Type.NORMAL, null, lassNatalie);
			pokemonRepo.save(bidoof);

			Pokemon hoothoot = new Pokemon("Hoothoot", List.of("Tinted Lens", "Insomnia"), Type.NORMAL, Type.FLYING,
					youngsterTristan);
			pokemonRepo.save(hoothoot);

			Pokemon starly = new Pokemon("Starly", List.of("Keen Eye", "Reckless"), Type.NORMAL, Type.FLYING,
					youngsterTristan);
			pokemonRepo.save(starly);

			Pokemon growlithe = new Pokemon("Growlithe", List.of("Intimidate", "Flash Fire"), Type.FIRE, null,
					youngsterLogan);
			pokemonRepo.save(growlithe);

			Pokemon burmy = new Pokemon("Turtwig", List.of("Overgrow", "Shell Armor"), Type.BUG, null, youngsterLogan);
			pokemonRepo.save(burmy);

			Pokemon zigzagoon = new Pokemon("Zigzagoon", List.of("Pickup", "Gluttony"), Type.NORMAL, null,
					youngsterLogan);
			pokemonRepo.save(zigzagoon);

			PokemonMoveSet piplu = new PokemonMoveSet(5, List.of("Pound", "Growl", "Bubble"), null, piplup,
					Nature.Naive);
			moveSetRepo.save(piplu);

			PokemonMoveSet chimch = new PokemonMoveSet(9, List.of("Scratch", "Leer", "Ember", "Taunt"), null, chimchar,
					Nature.Adamant);
			moveSetRepo.save(chimch);

			PokemonMoveSet turt = new PokemonMoveSet(9, List.of("Tackle", "Withdraw", "Absorb", "Razor Leaf"), null,
					turtwig, Nature.Careful);
			moveSetRepo.save(turt);

			PokemonMoveSet sent = new PokemonMoveSet(7,
					List.of("Foresight", "Defense Curl", "Quick Attack", "Fury Swipes"), null, sentret, Nature.Hasty);
			moveSetRepo.save(sent);

			PokemonMoveSet bido = new PokemonMoveSet(7, List.of("Tackle", "Growl", "Defense Curl"), null, bidoof,
					Nature.Quiet);
			moveSetRepo.save(bido);

			PokemonMoveSet hoot = new PokemonMoveSet(7, List.of("Growl", "Foresight", "Peck", "Hypnosis"), null,
					hoothoot, Nature.Jolly);
			moveSetRepo.save(hoot);

			PokemonMoveSet star = new PokemonMoveSet(7, List.of("Quick Attack", "Growl", "Tackle"), null, starly,
					Nature.Lax);
			moveSetRepo.save(star);

			PokemonMoveSet growl = new PokemonMoveSet(7, List.of("Leer", "Roar", "Howl", "Ember"), null, growlithe,
					Nature.Docile);
			moveSetRepo.save(growl);

			PokemonMoveSet burm = new PokemonMoveSet(7, List.of("Protect", "Tackle", "Big Bite", "Hidden Power"), null,
					burmy, Nature.Jolly);
			moveSetRepo.save(burm);

			PokemonMoveSet zigzag = new PokemonMoveSet(7, List.of("Growl", "Tail Whip", "Sand Attack", "Covet"), null,
					zigzagoon, Nature.Adamant);
			moveSetRepo.save(zigzag);

			log.info("Fetch all users");
			for (AppUser users : appUserRepo.findAll()) {
				log.info(users.toString());
			}

			log.info("fetch games");
			for (Game games : gameRepo.findAll()) {
				log.info(games.toString());
			}

			log.info("Fetch trainer teams");
			for (TrainerTeam teams : ttRepo.findAll()) {
				log.info(teams.toString());
			}

			log.info("fetch pokemon");
			for (Pokemon pokemons : pokemonRepo.findAll()) {
				log.info(pokemons.toString());
			}

			log.info("fetch movesets");
			for (PokemonMoveSet movesets : moveSetRepo.findAll()) {
				log.info(movesets.toString());
			}

		};
	}

}
