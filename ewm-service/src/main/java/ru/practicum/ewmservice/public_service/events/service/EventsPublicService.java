package ru.practicum.ewmservice.public_service.events.service;

import ru.practicum.ewmservice.dto.events.EventSearch;
import ru.practicum.ewmservice.dto.events.EventFullDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventsPublicService {

    List<EventFullDto> search(EventSearch filter, Integer page, Integer size, HttpServletRequest request);

    EventFullDto findById(Long id, HttpServletRequest request);
}
