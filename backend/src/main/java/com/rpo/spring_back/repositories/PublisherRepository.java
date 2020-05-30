package com.rpo.spring_back.repositories;

import com.rpo.spring_back.models.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

// PublisherRepository - интерфейс, который по молчанию уже реализует
// базовый набор функций, позволяющий извлекать, модифицировать и удалять записи из
// таблицы publishers. Надо лишь указать тип Publisher в шаблоне JpaRepository. Когда нам
// понадобятся какие то особенные запросы к базе банных, мы сможем их добавить сюда.
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
}
