package ru.practicum.ewmservice.dao.events;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.events.QEvent;
import ru.practicum.ewmservice.model.user.User;
import ru.practicum.ewmservice.status.State;

import java.util.List;
import java.util.Optional;

public interface EventsRepository extends EventCustomRepository<Event, QEvent, Long> {

    Boolean existsByCategory_Id(Long id);

    Page<Event> findByInitiator(User initiator, Pageable pageable);

    List<Event> findByIdInAndState(List<Long> eventsId, State state);

    Optional<Event> findByIdAndState(Long eventsId, State state);

    List<Event> findByIdIn(List<Long> eventsId);

    Optional<Event> findByInitiatorAndId(User initiator, Long eventId);

}
