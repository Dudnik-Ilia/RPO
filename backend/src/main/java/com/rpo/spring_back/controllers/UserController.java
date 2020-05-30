package com.rpo.spring_back.controllers;

import com.rpo.spring_back.models.Publisher;
import com.rpo.spring_back.models.User;
import com.rpo.spring_back.repositories.PublisherRepository;
import com.rpo.spring_back.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PublisherRepository publisherRepository;

    // GET запрос, метод возвращает список пользователей, который будет автоматически преобразован в JSON.
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // POST запрос, сохраняем в таблице users нового пользователя.
    // Здесь обработка ошибки сводится к тому, что мы возвращаем код 404 на запрос в случае,
    // если такое имя пользователя уже существует.
    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        try {
            User nc = userRepository.save(user);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        }
        catch(Exception ex)
        {
            String error;
            if (ex.getMessage().contains("users.name_UNIQUE"))
                error = "user_already_exists";
            else
                error = "undefined_error";
            Map<String, String> map = new HashMap<>();
            map.put("error", error);
            return ResponseEntity.ok(map);
        }
    }

    // POST запрос, метод добавляет список издателей пользователю
    @PostMapping("/users/{id}/addpublishers")
    public ResponseEntity<Object> addPublishers(@PathVariable(value = "id") Long userId,
                                           @Valid @RequestBody Set<Publisher> publishers) {
        Optional<User> uu = userRepository.findById(userId);
        int cnt = 0;
        if (uu.isPresent()) {
            User u = uu.get();
            for (Publisher m : publishers) {
                // извлекаем каждый издатель из базы прежде чем его
                // добавить к списку, т.к. в параметрах запроса передаются только ключи издателей
                Optional<Publisher> mm = publisherRepository.findById(m.id);
                if (mm.isPresent()) {
                    u.addPublisher(mm.get());
                    cnt++;
                }
            }
            userRepository.save(u);
        }
        Map<String, String> response = new HashMap<>();
        response.put("count", String.valueOf(cnt));
        return ResponseEntity.ok(response);
    }

    // POST запрос, метод удаляет список издателей пользователя
    @PostMapping("/users/{id}/removepublishers")
    public ResponseEntity<Object> removePublishers(@PathVariable(value = "id") Long userId,
                                              @Valid @RequestBody Set<Publisher> publishers) {
        Optional<User> uu = userRepository.findById(userId);
        int cnt = 0;
        if (uu.isPresent()) {
            User u = uu.get();
            for (Publisher m : u.publishers) {
                u.removePublisher(m);
                cnt++;
            }
            userRepository.save(u);
        }
        Map<String, String> response = new HashMap<>();
        response.put("count", String.valueOf(cnt));
        return ResponseEntity.ok(response);
    }

    // PUT запрос, обновляет запись в таблице users.
    // Здесь обработка ошибки сводится к тому, что мы возвращаем код 404 на запрос в случае,
    // если пользователь с указанным ключом отсутствует.
    // Копируются только два поля. С остальными, разберемся потом. //TODO
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId,
                                           @Valid @RequestBody User userDetails) {
        User user = null;
        Optional<User> uu = userRepository.findById(userId);
        if (uu.isPresent())
        {
            user = uu.get();
            user.login = userDetails.login;
            user.email = userDetails.email;
            userRepository.save(user);
        }
        else
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user_not_found"
            );
        }
        return ResponseEntity.ok(user);
    }

    // DELETE запрос, метод удаления записи из таблицы users
    @DeleteMapping("/users/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Map<String, Boolean> response = new HashMap<>();
        if (user.isPresent())
        {
            userRepository.delete(user.get());
            response.put("deleted", Boolean.TRUE);
        }
        else
        {
            response.put("deleted", Boolean.FALSE);
        }
        return response;
    }
}

