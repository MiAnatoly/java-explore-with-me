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
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        Compilation compilation;
        if (newCompilationDto.getEvents().isEmpty()) {
            compilation = CompilationMapper.toCompilation(newCompilationDto);
            compilationRepository.save(compilation);
            log.info("Post compilation count events CompilationAdminService");
            return CompilationMapper.toCompilationDto(compilation, List.of());
        }
        List<Event> events = eventsRepository.findByIdIn(newCompilationDto.getEvents());
        compilation = CompilationMapper.toCompilation(newCompilationDto);
        compilationRepository.save(compilation);
        events.forEach(x -> x.setCompilation(compilation));
        List<ViewStats> views = jointEvents.findViewStats(events, true);
        List<Request> requests = requestsRepository.findByEventInAndStatus(events, Status.PENDING);
        log.info("Post compilation count events {} CompilationAdminService", events.size());
        List<EventShortDto> eventsShortDto = jointEvents.toEventsShortDto(events, views, requests);
        return CompilationMapper.toCompilationDto(compilation, eventsShortDto);
    }

    @Transactional
    public void remove(Long compId) {
        compilationRepository.deleteById(compId);
        log.info("Delete compilation{} CompilationAdminService", compId);
    }

    @Transactional
    public CompilationDto edit(Long compId, NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotObjectException("not compilation"));
        List<Event> eventsOld = eventsRepository.findByCompilation_Id(compId);
        eventsOld.forEach(x -> x.setCompilation(null));
        log.info("list {}", eventsOld);
        List<Event> events = eventsRepository.findByIdInAndState(newCompilationDto.getEvents(), State.PUBLISHED);
        setUpdateCompilation(compilation, newCompilationDto, events);
        List<ViewStats> views = jointEvents.findViewStats(events, true);
        List<Request> requests = requestsRepository.findByEventInAndStatus(events, Status.PENDING);
        log.info("Patch compilation count events {} CompilationAdminService", events.size());
        List<EventShortDto> eventsShortDto = jointEvents.toEventsShortDto(events, views, requests);
        return CompilationMapper.toCompilationDto(compilation, eventsShortDto);
    }

    private void setUpdateCompilation(Compilation compilation,
                                      NewCompilationDto newCompilationDto, List<Event> events) {
        events.forEach(x -> x.setCompilation(compilation));
        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        }
        if (newCompilationDto.getTitle() != null) {
            compilation.setTitle(newCompilationDto.getTitle());
        }
    }
}
