package ru.practicum.ewmservice.admin_service.compilations.service;

import dto.ViewStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.dao.compilation.CompilationRepository;
import ru.practicum.ewmservice.dao.events.EventsRepository;
import ru.practicum.ewmservice.dao.requests.RequestsRepository;
import ru.practicum.ewmservice.dto.compilation.CompilationDto;
import ru.practicum.ewmservice.dto.compilation.NewCompilationDto;
import ru.practicum.ewmservice.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewmservice.dto.events.EventShortDto;
import ru.practicum.ewmservice.exception.NotObjectException;
import ru.practicum.ewmservice.joint.events.JointEvents;
import ru.practicum.ewmservice.mapper.CompilationMapper;
import ru.practicum.ewmservice.model.compilations.Compilation;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.requests.Request;
import ru.practicum.ewmservice.status.State;
import ru.practicum.ewmservice.status.Status;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CompilationAdminServiceImpl implements CompilationsAdminService {
    private final CompilationRepository compilationRepository;
    private final EventsRepository eventsRepository;
    private final RequestsRepository requestsRepository;
    private final JointEvents jointEvents;

    @Transactional
    @Override
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        Compilation compilation;
        if (newCompilationDto.getEvents() == null || newCompilationDto.getEvents().isEmpty()) {
            compilation = CompilationMapper.toCompilation(newCompilationDto, List.of());
            compilationRepository.save(compilation);
            log.info("Post compilation count events CompilationAdminService");
            return CompilationMapper.toCompilationDto(compilation, List.of());
        }
        List<Event> events = eventsRepository.findByIdIn(newCompilationDto.getEvents());
        compilation = CompilationMapper.toCompilation(newCompilationDto, events);
        compilationRepository.save(compilation);
        List<ViewStats> views = jointEvents.findViewStats(events, true);
        List<Request> requests = requestsRepository.findByEventInAndStatus(events, Status.PENDING);
        log.info("Post compilation count events {} CompilationAdminService", events.size());
        List<EventShortDto> eventsShortDto = jointEvents.toEventsShortDto(events, views, requests);
        return CompilationMapper.toCompilationDto(compilation, eventsShortDto);
    }

    @Transactional
    @Override
    public void remove(Long compId) {
        compilationRepository.deleteById(compId);
        log.info("Delete compilation{} CompilationAdminService", compId);
    }

    @Transactional
    @Override
    public CompilationDto edit(Long compId, UpdateCompilationRequest updateCompilationDto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotObjectException("not compilation"));
        List<Event> events = compilation.getEvents();
        log.info("list {}", events);
        if (updateCompilationDto.getEvents() != null) {
            events = eventsRepository.findByIdInAndState(updateCompilationDto.getEvents(), State.PUBLISHED);
        }
        setUpdateCompilation(compilation, updateCompilationDto, events);
        List<ViewStats> views = jointEvents.findViewStats(events, true);
        List<Request> requests = requestsRepository.findByEventInAndStatus(events, Status.PENDING);
        log.info("Patch compilation count events {} CompilationAdminService", events.size());
        List<EventShortDto> eventsShortDto = jointEvents.toEventsShortDto(events, views, requests);
        return CompilationMapper.toCompilationDto(compilation, eventsShortDto);
    }

    private void setUpdateCompilation(Compilation compilation,
                                      UpdateCompilationRequest updateCompilationDto, List<Event> events) {
        compilation.setEvents(events);
        if (updateCompilationDto.getPinned() != null) {
            compilation.setPinned(updateCompilationDto.getPinned());
        }
        if (updateCompilationDto.getTitle() != null && updateCompilationDto.getTitle().isBlank()) {
            compilation.setTitle(updateCompilationDto.getTitle());
        }
    }
}
