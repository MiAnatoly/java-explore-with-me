package ru.practicum.ewmservice.dao.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.requests.Request;
import ru.practicum.ewmservice.model.user.User;
import ru.practicum.ewmservice.status.Status;

import java.util.List;
import java.util.Optional;

public interface RequestsRepository extends JpaRepository<Request, Long> {

    List<Request> findByRequester(User requester);

    List<Request> findByRequesterAndEvent(User requester, Event event);

    Optional<Request> findByRequesterAndId(User requester, Long id);

    @Query("select count(r.id) from Request r where r.event = ?1 and r.status = ?2")
    Long findCount(Event event, Status status);

    List<Request> findByEventAndStatus(Event event, Status status);

    List<Request> findByEventAndStatusIn(Event event, List<Status> status);

    List<Request> findByEventInAndStatus(List<Event> events, Status status);

    List<Request> findByEvent(Event events);

    List<Request> findByEventAndIdInAndStatus(Event events, List<Long> ids, Status status);
}
