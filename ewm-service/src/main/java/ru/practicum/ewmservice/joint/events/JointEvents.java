package ru.practicum.ewmservice.joint.events;

import client.WebClientService;
import dto.HitDto;
import dto.ViewStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.dao.categories.CategoriesRepository;
import ru.practicum.ewmservice.dao.location.LocationRepository;
import ru.practicum.ewmservice.dao.requests.RequestsRepository;
import ru.practicum.ewmservice.dto.events.EventFullDto;
import ru.practicum.ewmservice.dto.events.EventShortDto;
import ru.practicum.ewmservice.dto.events.UpdateEventRequest;
import ru.practicum.ewmservice.exception.ConflictObjectException;
import ru.practicum.ewmservice.exception.NotObjectException;
import ru.practicum.ewmservice.mapper.EventsMapper;
import ru.practicum.ewmservice.mapper.LocationMapper;
import ru.practicum.ewmservice.model.categories.Category;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.location.Location;
import ru.practicum.ewmservice.model.requests.Request;
import ru.practicum.ewmservice.status.Status;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JointEvents {
    private final RequestsRepository requestsRepository;
    private final WebClientService service;

    private final CategoriesRepository categoriesRepository;

    private final LocationRepository locationRepository;
    private static final String URI = "/events";
    private static final String APP = "ewn-service";

    public List<ViewStats> findViewStats(LocalDateTime start, LocalDateTime end, List<Event> events, Boolean unique) {
        if (events.isEmpty()) {
            return List.of();
        }
        List<String> uris = events.stream().map(x -> URI + "/" + x.getId()).collect(Collectors.toList());
        return service.find(start,
                end, uris, true);
    }

    public List<ViewStats> findViewStats(List<Event> events, Boolean unique) {
        if (events.isEmpty()) {
            return List.of();
        }
        List<String> uris = events.stream().map(x -> URI + "/" + x.getId()).collect(Collectors.toList());
        return service.find(uris, true);
    }

    public List<ViewStats> findAll(LocalDateTime start, LocalDateTime end, Boolean unique) {
        return service.find(start, end, null, true);
    }

    public List<ViewStats> findAll(Boolean unique) {
        return service.find(true);
    }

    public void addHitWeb(HttpServletRequest request) {
        HitDto hitDto = new HitDto(APP, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        HttpStatus statusAdd = service.add(hitDto).statusCode();
        log.info("save static hit{}", statusAdd);
    }

    public Long quantityConfirmedRequests(List<Request> requests, Event event) {
        return requests.stream().filter(x -> x.getEvent().equals(event)).count();
    }

    public Long findHits(List<ViewStats> viewStats, Event event) {
        return viewStats.stream().filter(x -> split(x.getUri()).equals(event.getId())).count();
    }

    public Long split(String uri) {
        List<String> id = List.of(uri.split("/"));
        return Long.parseLong(id.get(id.size() - 1));
    }

    public EventFullDto toEventFullDto(Event event) {
        Long hits = 0L;
        List<ViewStats> viewStats = service.find(List.of(URI + "/" + event.getId()), true);
        if (!viewStats.isEmpty()) {
            hits = viewStats.get(0).getHits();
        }
        Long requests = requestsRepository.findCount(event, Status.PENDING);
        return EventsMapper.toEventFullDto(event, hits, requests);
    }

    public List<Request> toRequestStatus(List<Request> requests, Status status) {
        return requests.stream().filter(x -> x.getStatus().equals(status)).collect(Collectors.toList());
    }

    public List<EventFullDto> toEventsFullDto(List<Event> events, List<ViewStats> views, List<Request> requests) {
        Map<Long, Long> viewByEvent = views.stream()
                .collect(Collectors.toMap(stat -> split(stat.getUri()), ViewStats::getHits));
        Map<Long, Long> requestsByEvent = requests.stream()
                .collect(Collectors.groupingBy(r -> r.getEvent().getId(), Collectors.counting()));
        return events.stream()
                .map(x -> EventsMapper.toEventFullDto(x, viewByEvent.get(x.getId()),
                        requestsByEvent.get(x.getId()))).collect(Collectors.toList());
    }

    public List<EventShortDto> toEventsShortDto(List<Event> events, List<ViewStats> views, List<Request> requests) {
        return events.stream()
                .map(x -> EventsMapper.toEventShortDto(quantityConfirmedRequests(requests, x),
                        findHits(views, x), x)).collect(Collectors.toList());
    }

    public void setUpdateEvent(Event event, UpdateEventRequest updateEvent) {
        if (updateEvent.getAnnotation() != null && !updateEvent.getAnnotation().isBlank()) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoriesRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new NotObjectException("категория не найдена, id " + updateEvent.getCategory()));
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null && !updateEvent.getDescription().isBlank()) {
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
            Location location = locationRepository.save(LocationMapper.toLocation(updateEvent.getLocation()));
            event.setLocation(location);
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            event.setTitle(updateEvent.getTitle());
        }
    }
}