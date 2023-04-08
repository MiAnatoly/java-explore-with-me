package ru.practicum.ewmservice.admin_service.events.service;

import com.querydsl.core.types.Predicate;
import dto.ViewStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.dao.events.EventsRepository;
import ru.practicum.ewmservice.dao.requests.RequestsRepository;
import ru.practicum.ewmservice.dto.events.EventFullDto;
import ru.practicum.ewmservice.dto.events.UpdateEventAdminRequest;
import ru.practicum.ewmservice.exception.ConflictObjectException;
import ru.practicum.ewmservice.exception.NotObjectException;
import ru.practicum.ewmservice.joint.events.JointEvents;
import ru.practicum.ewmservice.model.QPredicates;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.events.QEvent;
import ru.practicum.ewmservice.model.requests.Request;
import ru.practicum.ewmservice.status.State;
import ru.practicum.ewmservice.status.StateActionAdmin;
import ru.practicum.ewmservice.status.Status;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EventsAdminServiceImpl implements EventsAdminService {
    private final EventsRepository eventsRepository;
    private final RequestsRepository requestsRepository;
    private final JointEvents jointEvents;

    @Override
    public List<EventFullDto> findAll(List<Long> users, List<State> eventStates, List<Long> categories,
                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer page, Integer size) {
        List<Event> events;
        List<ViewStats> views;
        List<Request> requests;

        Predicate predicate = QPredicates.builder()
                .add(users, QEvent.event.initiator.id::in)
                .add(eventStates, QEvent.event.state::in)
                .add(categories, QEvent.event.category.id::in)
                .add(rangeStart, QEvent.event.eventDate::after)
                .add(rangeEnd, QEvent.event.eventDate::before)
                .buildAnd();

        if (predicate == null) {
            events = eventsRepository.findAll(PageRequest.of(page, size)).getContent();
        } else {
            events = eventsRepository.findAll(predicate, PageRequest.of(page, size)).getContent();
        }
        views = jointEvents.findViewStats(events, true);

        if (events.isEmpty()) {
            requests = List.of();
        } else {
            requests = requestsRepository.findByEventInAndStatus(events, Status.PENDING);
        }
        log.info("Get events count {} EventsAdminService", events.size());
        return jointEvents.toEventsFullDto(events, views, requests);
    }

    @Override
    @Transactional
    public EventFullDto edit(Long eventId, UpdateEventAdminRequest updateEvent) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotObjectException("not event"));
        setUpdateEventAdmin(event, updateEvent);
        log.info("Patch event title {} EventsAdminService", event.getTitle());
        return jointEvents.toEventFullDto(event);
    }

    private void setUpdateEventAdmin(Event event, UpdateEventAdminRequest updateEvent) {
        if (updateEvent.getStateAction() != null) {
            if (event.getState().equals(State.PENDING)) {
                if (updateEvent.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)) {
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                } else {
                    event.setState(State.CANCELED);
                }
            } else {
                throw new ConflictObjectException("событие не в состояние публикации");
            }
        }
        jointEvents.setUpdateEvent(event, updateEvent);
    }
}
