package ru.practicum.ewmservice.private_service.events.service;

import ru.practicum.ewmservice.dto.events.*;
import ru.practicum.ewmservice.dto.requests.EventRequestStatusUpdateRequest;
import ru.practicum.ewmservice.dto.requests.EventRequestStatusUpdateResult;
import ru.practicum.ewmservice.dto.requests.ParticipationRequestDto;

import java.util.List;

public interface EventsPrivateService {

    List<EventShortDto> findByUser(Long userId, int page, int size);

    EventDto add(Long userId, NewEventDto newEventDto);

    EventFullDto findById(Long userId, Long eventId);

    EventFullDto edit(Long userId, Long eventId, UpdateEventUserRequest updateEvent);

    List<ParticipationRequestDto> findRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult editRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest request);
}