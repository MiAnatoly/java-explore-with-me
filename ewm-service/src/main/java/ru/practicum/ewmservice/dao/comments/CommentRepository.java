package ru.practicum.ewmservice.dao.comments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmservice.model.comments.Comment;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.user.User;
import ru.practicum.ewmservice.status.State;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByRequesterAndEvent_State(User requester, State state);

    Optional<Comment> findByIdAndRequesterAndEvent_State(Long id, User requester, State state);

    Page<Comment> findByEvent_idOrderByCreated(Long eventId, Pageable pageable);

    @Query("select count(c.id) from Comment c where c.event = ?1")
    Long findCount(Event event);

    List<Comment> findByEventIn(List<Event> events);
}
