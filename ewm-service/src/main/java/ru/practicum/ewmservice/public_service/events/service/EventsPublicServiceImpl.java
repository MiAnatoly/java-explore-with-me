package ru.practicum.ewmservice.public_service.events.service;

import dto.ViewStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.dao.events.EventsRepository;
import ru.practicum.ewmservice.dao.requests.RequestsRepository;
import ru.practicum.ewmservice.dto.events.EventFullDto;
import ru.practicum.ewmservice.exception.NotObjectException;
import ru.practicum.ewmservice.joint.events.JointEvents;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.requests.Request;
import ru.practicum.ewmservice.status.Status;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EventsPublicServiceImpl implements EventsPublicService {
    private final EventsRepository eventsRepository;
    private final RequestsRepository requestsRepository;
    private final JointEvents jointEvents;

    @Transactional
    @Override
    public List<EventFullDto> search(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer page,
                                     Integer size, HttpServletRequest request) {
        List<Event> events;
        List<ViewStats> views;
        if (rangeStart == null || rangeEnd == null) {
            events = eventsRepository.searchWithoutDate(text, categories, paid,
                    PageRequest.of(page, size)).getContent();
            views = jointEvents.findViewStats(events, true);
        } else {
            events = eventsRepository.search(text, categories, paid, rangeStart,
                    rangeEnd, PageRequest.of(page, size)).getContent();
            views = jointEvents.findViewStatsWeb(rangeStart, rangeEnd, events, true);
        }
        List<Request> requests = requestsRepository.findByEventInAndStatus(events, Status.PENDING);
        jointEvents.addHitWeb(request);
        log.info("Get events count {} EventsPublicService", events.size());
        return jointEvents.toEventsFullDto(events, views, requests);
    }

    @Transactional
    @Override
    public EventFullDto findById(Long id, HttpServletRequest request) {
        Event event = eventsRepository.findById(id)
                .orElseThrow(() -> new NotObjectException("not event"));
        jointEvents.addHitWeb(request);
        log.info("Get event title {} EventsPublicService", event.getTitle());
        return jointEvents.toEventFullDto(event);
    }

}
