package com.rpo.spring_back.controllers;

import com.rpo.spring_back.models.Developer;
import com.rpo.spring_back.models.Country;
import com.rpo.spring_back.repositories.DeveloperRepository;
import com.rpo.spring_back.repositories.CountryRepository;
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
public class DeveloperController {
    @Autowired
    DeveloperRepository developerRepository;
    @Autowired
    CountryRepository countryRepository;

    // GET запрос, метод возвращает список разработчиков, который будет автоматически преобразован в JSON
    @GetMapping("/developers")
    public List<Developer> getAllDevelopers() {
        return developerRepository.findAll();
    }

    // POST запрос, сохраняем в таблице developers нового разраба
    // В теле метода проверяем, что существует country с заданным id,
    // Если да, то developer.country = <Название_страны>
    // Без проверки было бы null
    @PostMapping("/developers")
    public ResponseEntity<Object> createDeveloper(@Valid @RequestBody Developer developer) {
        Optional<Country> cc = countryRepository.findById(developer.country.id);
        if(cc.isPresent()) {
            developer.country = cc.get();
        }
        Developer nc = developerRepository.save(developer);
        return new ResponseEntity<Object>(nc, HttpStatus.OK);
    }

    // PUT запрос, обновляет запись в таблице developers
    // Здесь обработка ошибки сводится к тому, что мы возвращаем код 404 на запрос в случае,
    // если разраб с указанным ключом отсутствует
    @PutMapping("/developers/{id}")
    public ResponseEntity<Developer> updateDeveloper(@PathVariable(value = "id") Long developerId,
                                                  @Valid @RequestBody Developer developerDetails) {
        Developer developer = null;
        Optional<Developer> aa = developerRepository.findById(developerId);
        if (aa.isPresent())
        {
            developer = aa.get();
            developer.name = developerDetails.name;
            developer.country = developerDetails.country;
            developerRepository.save(developer);
        }
        else
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "developer_not_found"
            );
        }
        return ResponseEntity.ok(developer);
    }

    // DELETE запрос, метод удаления записи из таблицы developers
    @DeleteMapping("/developers/{id}")
    public Map<String, Boolean> deleteDeveloper(@PathVariable(value = "id") Long developerId) {
        Optional<Developer> developer = developerRepository.findById(developerId);
        Map<String, Boolean> response = new HashMap<>();
        if (developer.isPresent())
        {
            developerRepository.delete(developer.get());
            response.put("deleted", Boolean.TRUE);
        }
        else
        {
            response.put("deleted", Boolean.FALSE);
        }
        return response;
    }
}
