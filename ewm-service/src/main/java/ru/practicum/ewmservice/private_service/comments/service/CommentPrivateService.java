package ru.practicum.ewmservice.private_service.comments.service;

import ru.practicum.ewmservice.dto.comments.CommentUserDto;
import ru.practicum.ewmservice.dto.comments.NewCommentDto;
import ru.practicum.ewmservice.dto.comments.UpdateUserCommentDto;

import java.util.List;

public interface CommentPrivateService {
    CommentUserDto add(Long userId, NewCommentDto newCommentDto);

    CommentUserDto edit(Long userId, Long comId, UpdateUserCommentDto commentDto);

    List<CommentUserDto> findByUser(Long userId);

    CommentUserDto findById(Long userId, Long comId);

    void delete(Long userId, Long comId);
}
