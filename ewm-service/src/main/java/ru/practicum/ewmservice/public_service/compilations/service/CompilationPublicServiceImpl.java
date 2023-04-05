package ru.practicum.ewmservice.public_service.compilations.service;

import dto.ViewStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.dao.compilation.CompilationRepository;
import ru.practicum.ewmservice.dao.events.EventsRepository;
import ru.practicum.ewmservice.dao.requests.RequestsRepository;
import ru.practicum.ewmservice.dto.compilation.CompilationDto;
import ru.practicum.ewmservice.dto.events.EventShortDto;
import ru.practicum.ewmservice.exception.NotObjectException;
import ru.practicum.ewmservice.joint.events.JointEvents;
import ru.practicum.ewmservice.mapper.CompilationMapper;
import ru.practicum.ewmservice.model.compilations.Compilation;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.requests.Request;
import ru.practicum.ewmservice.status.Status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CompilationPublicServiceImpl implements CompilationPublicService {
    private final CompilationRepository compilationRepository;
    private final RequestsRepository requestsRepository;
    private final EventsRepository eventsRepository;
    private final JointEvents jointEvents;

    @Override
    public List<CompilationDto> findAll(Boolean pinned, Integer page, Integer size) {
        List<Compilation> compilations = compilationRepository
                .findByPinned(pinned, PageRequest.of(page, size)).getContent();
        Map<Long, List<EventShortDto>> eventsMap = toCompilationsInEvens(compilations);
        log.info("get count compilation{} count event{} /CompilationPublicService",
                compilations.size(), eventsMap.size());
        return CompilationMapper.toCompilationsDto(compilations, eventsMap);
    }

    @Override
    public CompilationDto findById(Long id) {
        Compilation compilation = compilationRepository
                .findById(id).orElseThrow(() -> new NotObjectException("Подборка не найдена или недоступна"));
        List<EventShortDto> eventsShortDto = toCompilationInEvens(compilation);
        log.info("get compilation id{} count event{} /CompilationPublicService",
                compilation.getId(), eventsShortDto.size());
        return CompilationMapper.toCompilationDto(compilation, eventsShortDto);
    }

    private Map<Long, List<EventShortDto>> toCompilationsInEvens(List<Compilation> compilations) {
        List<Event> events = eventsRepository.findByCompilationIn(compilations);
        List<ViewStats> views = jointEvents.findViewStats(events, true);
        List<Request> requests = requestsRepository.findByEventInAndStatus(events, Status.PENDING);
        Map<Long, List<EventShortDto>> eventsShortDtoMap = new HashMap<>();
        compilations.forEach(x -> eventsShortDtoMap
                .put(x.getId(), jointEvents.toEventsShortDto(
                        events.stream()
                                .filter(y -> y.getCompilation().equals(x))
                                .collect(Collectors.toList()),
                        views,
                        requests)));
         return eventsShortDtoMap;
    }

    private List<EventShortDto> toCompilationInEvens(Compilation compilation) {
        List<Event> events = eventsRepository.findByCompilation_Id(compilation.getId());
        List<ViewStats> views = jointEvents.findViewStats(events, true);
        List<Request> requests = requestsRepository.findByEventInAndStatus(events, Status.PENDING);
        return jointEvents.toEventsShortDto(events, views, requests);
    }
}
