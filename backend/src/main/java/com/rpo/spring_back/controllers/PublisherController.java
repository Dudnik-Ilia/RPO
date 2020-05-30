package com.rpo.spring_back.controllers;

import com.rpo.spring_back.models.Publisher;
import com.rpo.spring_back.repositories.CountryRepository;
import com.rpo.spring_back.repositories.PublisherRepository;
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
public class PublisherController {
    @Autowired
    PublisherRepository publisherRepository;

    @Autowired
    CountryRepository countryRepository;

    // GET запрос, метод возвращает список издателей, который будет автоматически преобразован в JSON
    @GetMapping("/publishers")
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    // POST запрос, сохраняем в таблице publishers нового издателя
    @PostMapping("/publishers")
    public ResponseEntity<Object> createPublisher(@Valid @RequestBody Publisher publisher) {
        Publisher nc = publisherRepository.save(publisher);
        return new ResponseEntity<Object>(nc, HttpStatus.OK);
    }

    // PUT запрос, обновляет запись в таблице publishers
    // Здесь обработка ошибки сводится к тому, что мы возвращаем код 404 на запрос в случае,
    // если издатель с указанным ключом отсутствует
    @PutMapping("/publishers/{id}")
    public ResponseEntity<Publisher> updatePublisher(@PathVariable(value = "id") Long publisherId,
                                                @Valid @RequestBody Publisher publisherDetails) {
        Publisher publisher = null;
        Optional<Publisher> mm = publisherRepository.findById(publisherId);
        if (mm.isPresent())
        {
            publisher = mm.get();
            publisher.name = publisherDetails.name;
            publisher.location = publisherDetails.location;
            publisherRepository.save(publisher);
        }
        else
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "publisher_not_found"
            );
        }
        return ResponseEntity.ok(publisher);
    }

    // DELETE запрос, метод удаления записи из таблицы publishers
    @DeleteMapping("/publishers/{id}")
    public Map<String, Boolean> deletePublisher(@PathVariable(value = "id") Long publisherId) {
        Optional<Publisher> publisher = publisherRepository.findById(publisherId);
        Map<String, Boolean> response = new HashMap<>();
        if (publisher.isPresent())
        {
            publisherRepository.delete(publisher.get());
            response.put("deleted", Boolean.TRUE);
        }
        else
        {
            response.put("deleted", Boolean.FALSE);
        }
        return response;
    }

}

