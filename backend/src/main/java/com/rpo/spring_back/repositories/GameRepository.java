package com.rpo.spring_back.repositories;

import com.rpo.spring_back.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;

// GameRepository - интерфейс, который по молчанию уже реализует
// базовый набор функций, позволяющий извлекать, модифицировать и удалять записи из
// таблицы games. Надо лишь указать тип Game в шаблоне JpaRepository. Когда нам
// понадобятся какие то особенные запросы к базе банных, мы сможем их добавить сюда.
public interface GameRepository extends JpaRepository<Game, Long> {
}
