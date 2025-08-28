package nuzlocke.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nuzlocke.domain.Game;
import nuzlocke.service.GameService;

@RestController
@RequestMapping("/games")
public class RestGameController {

    private static final Logger log = LoggerFactory.getLogger(RestGameController.class);

    @Autowired
    private GameService gameService;

    @GetMapping
    public Iterable<Game> getAllGames() {
        log.info("Fetch all games");
        return gameService.getAllGames();
    }

}
