package ru.practicum.ewmservice.dao.comments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmservice.model.comments.Comment;
import ru.practicum.ewmservice.model.user.User;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByRequester(User requester);

    Optional<Comment> findByIdAndRequester(Long id, User requester);

    Page<Comment> findByEvent_idOrderByCreated(Long eventId, Pageable pageable);
}
