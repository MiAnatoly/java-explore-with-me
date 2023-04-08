package ru.practicum.ewmservice.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewmservice.dto.comments.CommentDto;
import ru.practicum.ewmservice.dto.comments.CommentUserDto;
import ru.practicum.ewmservice.dto.comments.NewCommentDto;
import ru.practicum.ewmservice.dto.users.UserShortDto;
import ru.practicum.ewmservice.model.comments.Comment;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentsMapper {

    public static Comment toComment(NewCommentDto newCommentDto, User user, Event event) {
        return new Comment(
                null,
                newCommentDto.getDescription(),
                LocalDateTime.now(),
                event,
                user
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        UserShortDto userShortDto = UsersMapper.toUserShortDto(comment.getRequester());
        return new CommentDto(
                comment.getId(),
                comment.getDescription(),
                comment.getCreated(),
                comment.getEvent().getId(),
                userShortDto
        );
    }

    public static CommentUserDto toCommentUserDto(Comment comment) {
        return new CommentUserDto(
                comment.getId(),
                comment.getDescription(),
                comment.getCreated(),
                comment.getEvent().getId()
        );
    }

    public static List<CommentUserDto> toCommentsUserDto(List<Comment> comments) {
        return comments.stream().map(CommentsMapper::toCommentUserDto).collect(Collectors.toList());
    }

    public static List<CommentDto> toCommentsDto(List<Comment> comments) {
        return comments.stream().map(CommentsMapper::toCommentDto).collect(Collectors.toList());
    }
}
