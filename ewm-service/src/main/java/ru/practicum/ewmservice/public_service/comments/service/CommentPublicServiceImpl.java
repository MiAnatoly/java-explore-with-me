package ru.practicum.ewmservice.public_service.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.dao.comments.CommentRepository;
import ru.practicum.ewmservice.dto.comments.CommentDto;
import ru.practicum.ewmservice.mapper.CommentsMapper;
import ru.practicum.ewmservice.model.comments.Comment;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentPublicServiceImpl implements CommentPublicService {
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findByEvent(Long eventId, Integer page, Integer size) {
        List<Comment> comments = commentRepository
                .findByEvent_idOrderByCreated(eventId, PageRequest.of(page, size)).getContent();
        log.info("Get comment eventId{} count {}", eventId, comments.size());
        return CommentsMapper.toCommentsDto(comments);
    }
}
