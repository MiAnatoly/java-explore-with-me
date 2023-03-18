package ru.practicum.service.hit.service;

import dto.HitDto;
import dto.ViewStats;

import java.util.List;

public interface HitService {
    void add(HitDto hitDto);

    List<ViewStats> find(String start, String end, List<String> uris, Boolean unique);
}
