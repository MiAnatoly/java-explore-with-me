package ru.practicum.ewmservice.private_service.events.service;

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
import ru.practicum.ewmservice.dao.users.UsersRepository;
import ru.practicum.ewmservice.dto.events.*;
import ru.practicum.ewmservice.dto.requests.EventRequestStatusUpdateRequest;
import ru.practicum.ewmservice.dto.requests.EventRequestStatusUpdateResult;
import ru.practicum.ewmservice.dto.requests.ParticipationRequestDto;
import ru.practicum.ewmservice.exception.ConflictObjectException;
import ru.practicum.ewmservice.exception.NotObjectException;
import ru.practicum.ewmservice.joint.events.JointEvents;
import ru.practicum.ewmservice.mapper.EventsMapper;
import ru.practicum.ewmservice.mapper.LocationMapper;
import ru.practicum.ewmservice.mapper.RequestsMapper;
import ru.practicum.ewmservice.model.categories.Category;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.location.Location;
import ru.practicum.ewmservice.model.requests.Request;
import ru.practicum.ewmservice.model.user.User;
import ru.practicum.ewmservice.status.State;
import ru.practicum.ewmservice.status.StateActionPrivate;
import ru.practicum.ewmservice.status.Status;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventsPrivateServiceImpl implements EventsPrivateService {
    private final EventsRepository eventsRepository;
    private final UsersRepository usersRepository;
    private final LocationRepository locationRepository;
    private final CategoriesRepository categoriesRepository;
    private final RequestsRepository requestsRepository;
    private final JointEvents jointEvents;

    @Override
    public List<EventShortDto> findByUser(Long userId, int page, int size) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotObjectException("пользователь не найден, id " + userId));
        List<Event> events = eventsRepository.findByInitiator(user, PageRequest.of(page, size)).getContent();
        List<ViewStats> views = jointEvents.findViewStats(events, true);
        List<Request> requests = requestsRepository.findByEventInAndStatus(events, Status.PENDING);
        log.info("Get events count {} EventsPrivateService", events.size());
        return jointEvents.toEventsShortDto(events, views, requests);
    }

    @Transactional
    @Override
    public EventDto add(Long userId, NewEventDto newEventDto) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotObjectException("пользователь не найден, id " + userId));
        Category category = categoriesRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotObjectException("категория не найдена, id " + newEventDto.getCategory()));
        Location location = LocationMapper.toLocation(newEventDto.getLocation());
        location = locationRepository.save(location);
        Event event = EventsMapper.toEvent(user, category, location, newEventDto);
        event = eventsRepository.save(event);
        log.info("Post event title {} EventsPrivateService", event.getTitle());
        return EventsMapper.toEventDto(event);
    }

    @Override
    public EventFullDto findById(Long userId, Long eventId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotObjectException("пользователь не найден, id " + userId));
        Event event = eventsRepository.findByInitiatorAndId(user, eventId)
                .orElseThrow(() -> new NotObjectException("событие не найдено, id " + eventId));
        log.info("Get event title {} EventsPrivateService", event.getTitle());
        return jointEvents.toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto edit(Long userId, Long eventId, UpdateEventUserRequest updateEvent) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotObjectException("пользователь не найден, id " + userId));
        Event event = eventsRepository.findByInitiatorAndId(user, eventId)
                .orElseThrow(() -> new NotObjectException("событие не найдено, id " + eventId));
        if (event.getState().equals(State.CANCELED) || event.getState().equals(State.PENDING)) {
            setUpdateEventUser(event, updateEvent);
            log.info("Patch event title {} EventsPrivateService", event.getTitle());
            return jointEvents.toEventFullDto(event);
        } else {
            throw new ConflictObjectException("событие прошло модерацию и находиться в статусе публикация");
        }

    }

    @Override
    public List<ParticipationRequestDto> findRequests(Long userId, Long eventId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotObjectException("пользователь не найден, id " + userId));
        Event event = eventsRepository.findByInitiatorAndId(user, eventId)
                .orElseThrow(() -> new NotObjectException("событие не найдено, id " + eventId));
        List<Request> requests = requestsRepository.findByEventAndStatus(event, Status.PENDING);
        log.info("Get requests count {} EventsPrivateService", requests.size());
        return RequestsMapper.toParticipationRequestsDto(requests);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult editRequest(Long userId, Long eventId,
                                                      EventRequestStatusUpdateRequest request) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotObjectException("пользователь не найден, id " + userId));
        Event event = eventsRepository.findByInitiatorAndId(user, eventId)
                .orElseThrow(() -> new NotObjectException("событие не найдено, id " + eventId));
        List<Request> updateRequest = requestsRepository
                .findByEventAndIdInAndStatus(event, request.getRequestIds(), Status.PENDING);
        if (request.getRequestIds().size() != updateRequest.size()) {
            throw new ConflictObjectException("статус можно изменить только у заявок в состояние ожидания");
        }
        updateRequest.forEach(x -> x.setStatus(request.getStatus()));
        List<Request> requests = requestsRepository.findByEvent(event);
        List<Request> requestsConfirmed = jointEvents.toRequestStatus(requests, Status.CONFIRMED);
        List<Request> requestsRejected = jointEvents.toRequestStatus(requests, Status.REJECTED);
        if (event.getParticipantLimit() > 0 || event.isRequestModeration()) {
            if (requestsConfirmed.size() > event.getParticipantLimit()) {
                throw new ConflictObjectException("Привышено количество заявок");
            }
        } else if (event.getParticipantLimit() == requestsConfirmed.size()) {
            List<Request> requestsPending = jointEvents.toRequestStatus(requests, Status.PENDING);
            requestsPending.forEach(x -> x.setStatus(Status.REJECTED));
        }
        log.info("Patch requests count {} EventsPrivateService", updateRequest.size());
        return RequestsMapper.toEventRequestStatusUpdateResult(
                RequestsMapper.toParticipationRequestsDto(requestsConfirmed),
                RequestsMapper.toParticipationRequestsDto(requestsRejected));
    }

    private void setUpdateEventUser(Event event, UpdateEventUserRequest updateEvent) {
        if (updateEvent.getStateAction() != null) {
            if (updateEvent.getStateAction().equals(StateActionPrivate.SEND_TO_REVIEW)) {
                event.setState(State.PENDING);
                event.setPublishedOn(LocalDateTime.now());
            } else {
                event.setState(State.CANCELED);
            }
        }
        jointEvents.setUpdateEvent(event, updateEvent);
    }
}
