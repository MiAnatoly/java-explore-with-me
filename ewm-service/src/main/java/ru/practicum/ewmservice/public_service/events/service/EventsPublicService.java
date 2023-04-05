package ru.practicum.ewmservice.public_service.events.service;

import ru.practicum.ewmservice.dto.events.EventFullDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventsPublicService {

    List<EventFullDto> search(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                              LocalDateTime rangeEnd, Boolean onlyAvailable, String sort,
                              Integer page, Integer size, HttpServletRequest request);

    EventFullDto findById(Long id, HttpServletRequest request);
}
