package ru.practicum.ewmservice.private_service.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.dao.comments.CommentRepository;
import ru.practicum.ewmservice.dao.events.EventsRepository;
import ru.practicum.ewmservice.dao.users.UsersRepository;
import ru.practicum.ewmservice.dto.comments.CommentUserDto;
import ru.practicum.ewmservice.dto.comments.NewCommentDto;
import ru.practicum.ewmservice.dto.comments.UpdateUserCommentDto;
import ru.practicum.ewmservice.exception.NotObjectException;
import ru.practicum.ewmservice.mapper.CommentsMapper;
import ru.practicum.ewmservice.model.comments.Comment;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.user.User;
import ru.practicum.ewmservice.status.State;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommentPrivateServiceImpl implements CommentPrivateService {
    private final CommentRepository commentRepository;
    private final EventsRepository eventsRepository;
    private final UsersRepository usersRepository;

    @Override
    @Transactional
    public CommentUserDto add(Long userId, NewCommentDto newCommentDto) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotObjectException("нет пользователя"));
        Event event = eventsRepository.findByIdAndState(newCommentDto.getEventId(), State.PUBLISHED)
                .orElseThrow(() -> new NotObjectException("нет опубликованного события"));
        Comment comment = CommentsMapper.toComment(newCommentDto, user, event);
        comment = commentRepository.save(comment);
        log.info("Post comment {} /CommentPrivateServiceImpl", comment.getId());
        return CommentsMapper.toCommentUserDto(comment);
    }

    @Override
    @Transactional
    public CommentUserDto edit(Long userId, Long comId, UpdateUserCommentDto updateUserCommentDto) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotObjectException("нет пользователя"));
        Comment comment = commentRepository.findByIdAndRequester(comId, user)
                .orElseThrow(() -> new NotObjectException(
                        "комментарий отсутствует или не пренадлежит данному пользователю"));
        comment.setDescription(updateUserCommentDto.getDescription());
        return CommentsMapper.toCommentUserDto(comment);
    }

    @Override
    public List<CommentUserDto> findByUser(Long userId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotObjectException("нет пользователя"));
        List<Comment> comments = commentRepository.findByRequester(user);
        log.info("Get comments.size {} /CommentPrivateServiceImpl", comments.size());
        return CommentsMapper.toCommentsUserDto(comments);
    }

    @Override
    public CommentUserDto findById(Long userId, Long comId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotObjectException("нет пользователя"));
        Comment comment = commentRepository.findByIdAndRequester(comId, user)
                .orElseThrow(() -> new NotObjectException(
                        "комментарий отсутствует или не пренадлежит данному пользователю"));
        return CommentsMapper.toCommentUserDto(comment);
    }

    @Transactional
    @Override
    public void delete(Long userId, Long comId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotObjectException("нет пользователя"));
        Comment comment = commentRepository.findByIdAndRequester(comId, user)
                .orElseThrow(() -> new NotObjectException(
                        "комментарий отсутствует или не пренадлежит данному пользователю"));
        commentRepository.deleteById(comment.getId());
    }
}
