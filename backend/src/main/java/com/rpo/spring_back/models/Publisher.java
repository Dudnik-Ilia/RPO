package com.rpo.spring_back.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity //  таблица в базе
@Table(name = "publishers") //  имя таблицы
@Access(AccessType.FIELD) //разрешаем доступ к полям класса
public class Publisher {

    public Publisher() { }
    public Publisher(Long id) {
        this.id = id;
    }

    @Id //Каждое поле класса связывается с полем в таблице а для поля ключа, дополнительно, указываются его свойства.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    public long id;

    @Column(name = "name", nullable = false, unique = true)
    public String name;

    @Column(name = "location", nullable = false)
    public String location;

    @JsonIgnore
    @OneToMany
    public List<Game> games = new ArrayList<>();

    // В настройках отношения @ManyToMany указывается что она сделана через таблицу
    // userspublishers по ее полям publisherid и userid. Со стороны таблицы publishers это
    // joinColumns, а со стороны users inverseJoinColums. В модели Users должно быть добавлено
    // такое же определение, но в обратном порядке. Однако без этого можно обойтись,
    // воспользовавшись атрибутом mappedBy. Тогда компилятор просто возьмет нужную
    // информацию из аннотации поля Publisher.users.
    // ***
    // В отношении многие-ко-многим есть та проблема циклических ссылок.
    // Разрываем цепочку с помощью аннотации @JsonIgnore для поля Publisher.users.
    // ***
    // Поля участвующие в отношении многие-ко-многим вместо
    // типа List (список) имеют тип Set (множество). Для типа List JPA
    // генерирует очень неэффективный код запросов к базе данных.
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "userspublishers",
            joinColumns = @JoinColumn(name = "publisherid"),
            inverseJoinColumns = @JoinColumn(name = "userid"))
    public Set<User> users = new HashSet<>();
}
