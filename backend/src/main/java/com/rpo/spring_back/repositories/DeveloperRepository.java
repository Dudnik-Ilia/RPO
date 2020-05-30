package com.rpo.spring_back.repositories;

import com.rpo.spring_back.models.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

// DeveloperRepository - интерфейс, который по молчанию уже реализует
// базовый набор функций, позволяющий извлекать, модифицировать и удалять записи из
// таблицы developers. Надо лишь указать тип Developer в шаблоне JpaRepository. Когда нам
// понадобятся какие то особенные запросы к базе банных, мы сможем их добавить сюда.
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
}
