package com.rpo.spring_back.controllers;

import com.rpo.spring_back.models.Game;
import com.rpo.spring_back.repositories.CountryRepository;
import com.rpo.spring_back.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class GameController {
    @Autowired
    GameRepository gameRepository;

    @Autowired
    CountryRepository countryRepository;

    @GetMapping("/games")
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @PostMapping("/games")
    public ResponseEntity<Object> createGame(@Valid @RequestBody Game game) {
        Game nc = gameRepository.save(game);
        return new ResponseEntity<Object>(nc, HttpStatus.OK);
    }

    @PutMapping("/games/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable(value = "id") Long gameId,
                                           @Valid @RequestBody Game gameDetails) {
        Game game = null;
        Optional<Game> mm = gameRepository.findById(gameId);
        if (mm.isPresent())
        {
            game = mm.get();
            game.name = gameDetails.name;
            game.year = gameDetails.year;
            gameRepository.save(game);
        }
        else
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "game_not_found"
            );
        }
        return ResponseEntity.ok(game);
    }

    @DeleteMapping("/games/{id}")
    public Map<String, Boolean> deleteGame(@PathVariable(value = "id") Long gameId) {
        Optional<Game> game = gameRepository.findById(gameId);
        Map<String, Boolean> response = new HashMap<>();
        if (game.isPresent())
        {
            gameRepository.delete(game.get());
            response.put("deleted", Boolean.TRUE);
        }
        else
        {
            response.put("deleted", Boolean.FALSE);
        }
        return response;
    }
}
