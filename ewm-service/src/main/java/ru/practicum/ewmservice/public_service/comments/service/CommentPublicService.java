package ru.practicum.ewmservice.public_service.comments.service;

import ru.practicum.ewmservice.dto.comments.CommentDto;

import java.util.List;

public interface CommentPublicService {
    List<CommentDto> findByEvent(Long eventId, Integer page, Integer size);
}
