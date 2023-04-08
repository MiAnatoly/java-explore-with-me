package ru.practicum.ewmservice.private_service.requests.service;

import ru.practicum.ewmservice.dto.requests.ParticipationRequestDto;

import java.util.List;

public interface RequestsPrivateService {

    List<ParticipationRequestDto> findByUser(Long userId);

    ParticipationRequestDto add(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}