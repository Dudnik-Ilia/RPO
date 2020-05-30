package com.rpo.spring_back.models;

import javax.persistence.*;

@Entity //  таблица в базе
@Table(name = "developers") //  имя таблицы
@Access(AccessType.FIELD) // разрешаем доступ к полям класса
public class Developer {

    public Developer() {}
    public Developer(Long id) {
        this.id = id;
    }

    @Id //Каждое поле класса связывается с полем в таблице а для поля ключа, дополнительно, указываются его свойства.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    public long id;

    @Column(name = "name", nullable = false, unique = true)
    public String name;

    // Отношение в модели Developer необходимо для того чтобы можно было задать, изменить или получить страну артиста
    @ManyToOne // - многие к одному, которая задает зависимость между таблицами developers и countries
    @JoinColumn(name = "countryid")
    public Country country;

}
