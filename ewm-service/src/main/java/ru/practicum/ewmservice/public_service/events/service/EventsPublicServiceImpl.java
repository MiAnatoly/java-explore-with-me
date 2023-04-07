package ru.practicum.ewmservice.public_service.events.service;

import com.querydsl.core.types.Predicate;
import dto.ViewStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.dao.events.EventsRepository;
import ru.practicum.ewmservice.dao.requests.RequestsRepository;
import ru.practicum.ewmservice.dto.events.EventSearch;
import ru.practicum.ewmservice.dto.events.EventFullDto;
import ru.practicum.ewmservice.exception.NotObjectException;
import ru.practicum.ewmservice.joint.events.JointEvents;
import ru.practicum.ewmservice.model.QPredicates;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.events.QEvent;
import ru.practicum.ewmservice.model.requests.Request;
import ru.practicum.ewmservice.status.Sort;
import ru.practicum.ewmservice.status.Status;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
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
    public List<EventFullDto> search(EventSearch filter, Integer page,
                                     Integer size, HttpServletRequest request) {
        Boolean onlyAvailable = null;
        if (filter.isOnlyAvailable()) {
            onlyAvailable = false;
        }
        Predicate predicateText = QPredicates.builder()
                .add(filter.getText(), QEvent.event.annotation::likeIgnoreCase)
                .add(filter.getText(), QEvent.event.description::likeIgnoreCase)
                .buildOr();
        Predicate predicate = QPredicates.builder()
                .add(predicateText)
                .add(filter.getCategories(), QEvent.event.category.id::in)
                .add(filter.getPaid(), QEvent.event.paid::eq)
                .add(filter.getRangeStart(), QEvent.event.eventDate::after)
                .add(filter.getRangeEnd(), QEvent.event.eventDate::before)
                .add(onlyAvailable, QEvent.event.isNotAvailable::eq)
                .buildAnd();

        List<Event> events = eventsRepository.findAll(predicate, PageRequest.of(page, size)).getContent();
        List<ViewStats> views = jointEvents.findViewStats(events, true);

        List<Request> requests = requestsRepository.findByEventInAndStatus(events, Status.PENDING);
        jointEvents.addHitWeb(request);
        log.info("Get events count {} EventsPublicService", events.size());
        List<EventFullDto> sortEvents = jointEvents.toEventsFullDto(events, views, requests);
        if (filter.getSort() == null) {
            return sortEvents;
        }
        if (filter.getSort().equals(Sort.EVENT_DATE.toString())) {
            sortEvents.sort(Comparator.comparing(EventFullDto::getEventDate));
        } else if (filter.getSort().equals(Sort.VIEWS.toString())) {
            sortEvents.sort(Comparator.comparing(EventFullDto::getViews));
        }
        return sortEvents;
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
