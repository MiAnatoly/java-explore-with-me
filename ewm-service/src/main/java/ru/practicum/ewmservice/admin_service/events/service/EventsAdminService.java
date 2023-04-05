package ru.practicum.ewmservice.admin_service.events.service;

import ru.practicum.ewmservice.dto.events.EventFullDto;
import ru.practicum.ewmservice.dto.events.UpdateEventAdminRequest;
import ru.practicum.ewmservice.status.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsAdminService {

    List<EventFullDto> findAll(List<Long> users, List<State> eventStates, List<Long> categories,
                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer page, Integer size);

    EventFullDto edit(Long eventId, UpdateEventAdminRequest updateEvent);
}
