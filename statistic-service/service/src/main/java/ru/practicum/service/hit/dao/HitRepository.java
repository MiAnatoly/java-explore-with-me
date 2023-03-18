package ru.practicum.service.hit.dao;

import dto.ViewStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.service.hit.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new dto.ViewStats(b.app, b.uri, count(b.ip)) from EndpointHit b " +
            "where b.timestamp between ?1 and ?2 and b.uri in ?3" +
            " group by b.uri, b.app order by count(b.ip) DESC")
    List<ViewStats> findUniqueFalse(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new dto.ViewStats(b.app, b.uri, count(DISTINCT b.ip)) " +
            "from EndpointHit b where b.timestamp between ?1 and ?2 and b.uri in ?3" +
            " group by b.uri, b.app order by count(b.ip) DESC")
    List<ViewStats> findUniqueTrue(LocalDateTime start, LocalDateTime end, List<String> uris);
}
