package ru.practicum.ewmservice.admin_service.events.service;

import dto.ViewStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.dao.categories.CategoriesRepository;
import ru.practicum.ewmservice.dao.events.EventsRepository;
import ru.practicum.ewmservice.dao.location.LocationRepository;
import ru.practicum.ewmservice.dao.requests.RequestsRepository;
import ru.practicum.ewmservice.dto.events.EventFullDto;
import ru.practicum.ewmservice.dto.events.UpdateEventAdminRequest;
import ru.practicum.ewmservice.exception.ConflictObjectException;
import ru.practicum.ewmservice.exception.NotObjectException;
import ru.practicum.ewmservice.joint.events.JointEvents;
import ru.practicum.ewmservice.model.categories.Category;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.location.Location;
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
    private final LocationRepository locationRepository;
    private final CategoriesRepository categoriesRepository;
    private final JointEvents jointEvents;

    @Override
    public List<EventFullDto> findAll(List<Long> users, List<State> eventStates, List<Long> categories,
                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer page, Integer size) {
        List<Event> events;
        List<ViewStats> views;
        List<Request> requests;
        if (rangeStart == null || rangeEnd == null) {
            if (eventStates == null) {
                events = eventsRepository.searchAdminAllState(
                        users, categories, PageRequest.of(page, size)).getContent();
            } else {
                events = eventsRepository.searchAdmin(
                        users, eventStates, categories, PageRequest.of(page, size)).getContent();
            }
                    views = jointEvents.findViewStats(events, true);
        } else {
            if (eventStates == null) {
                events = eventsRepository.searchAdminWithDateAllState(
                        users, categories, rangeStart, rangeEnd, PageRequest.of(page, size)).getContent();
            } else {
                events = eventsRepository.searchAdminWithDate(
                        users, eventStates, categories, rangeStart, rangeEnd, PageRequest.of(page, size)).getContent();
            }

                    views = jointEvents.findViewStats(rangeStart, rangeEnd, events, true);
        }
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
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoriesRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new NotObjectException("no category"));
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            if (LocalDateTime.now().isBefore(updateEvent.getEventDate())) {
                event.setEventDate(updateEvent.getEventDate());
            } else {
                throw new ConflictObjectException("нельзя изменить дату события на уже наступившую");
            }
        }
        if (updateEvent.getLocation() != null) {
            Location location = locationRepository.save(updateEvent.getLocation());
            event.setLocation(location);
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
    }
}
