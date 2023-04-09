package ru.practicum.ewmservice.joint.events;

import client.WebClientService;
import dto.HitDto;
import dto.ViewStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.dao.categories.CategoriesRepository;
import ru.practicum.ewmservice.dao.comments.CommentRepository;
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
import ru.practicum.ewmservice.model.comments.Comment;
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
    private final CommentRepository commentRepository;
    private static final String URI = "/events";
    private static final String APP = "ewn-service";

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
        Long comments = commentRepository.findCount(event);
        return EventsMapper.toEventFullDto(event, hits, requests, comments);
    }

    public List<Request> toRequestStatus(List<Request> requests, Status status) {
        return requests.stream().filter(x -> x.getStatus().equals(status)).collect(Collectors.toList());
    }

    public List<EventFullDto> toEventsFullDto(List<Event> events, List<ViewStats> views,
                                              List<Request> requests, List<Comment> comments) {
        Map<Long, Long> viewByEvent = viewByEvent(views);
        Map<Long, Long> requestsByEvent = requestsByEvent(requests);
        Map<Long, Long> commentsByEvent = commentsByEvent(comments);
        return events.stream()
                .map(x -> EventsMapper.toEventFullDto(x, viewByEvent.get(x.getId()),
                        requestsByEvent.get(x.getId()), commentsByEvent.get(x.getId()))).collect(Collectors.toList());
    }

    public List<EventShortDto> toEventsShortDto(List<Event> events, List<ViewStats> views,
                                                List<Request> requests, List<Comment> comments) {
        Map<Long, Long> viewByEvent = viewByEvent(views);
        Map<Long, Long> requestsByEvent = requestsByEvent(requests);
        Map<Long, Long> commentsByEvent = commentsByEvent(comments);
        return events.stream()
                .map(x -> EventsMapper.toEventShortDto(requestsByEvent.get(x.getId()), viewByEvent.get(x.getId()),
                         x, commentsByEvent.get(x.getId()))).collect(Collectors.toList());
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

    private Map<Long, Long> viewByEvent(List<ViewStats> views) {
        return views.stream().collect(Collectors.toMap(stat -> split(stat.getUri()), ViewStats::getHits));
    }

    private Map<Long, Long> requestsByEvent(List<Request> requests) {
        return   requests.stream()
                .collect(Collectors.groupingBy(r -> r.getEvent().getId(), Collectors.counting()));
    }

    private Map<Long, Long> commentsByEvent(List<Comment> comments) {
        return comments.stream()
                .collect(Collectors.groupingBy(r -> r.getEvent().getId(), Collectors.counting()));
    }
}