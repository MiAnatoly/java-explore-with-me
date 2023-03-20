package ru.practicum.service.hit.service;

import dto.HitDto;
import dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    void add(HitDto hitDto);

    List<ViewStats> find(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
