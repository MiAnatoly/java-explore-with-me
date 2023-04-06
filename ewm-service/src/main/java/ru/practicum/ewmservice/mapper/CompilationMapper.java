package ru.practicum.ewmservice.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewmservice.dto.compilation.CompilationDto;
import ru.practicum.ewmservice.dto.compilation.NewCompilationDto;
import ru.practicum.ewmservice.dto.events.EventShortDto;
import ru.practicum.ewmservice.model.compilations.Compilation;
import ru.practicum.ewmservice.model.events.Event;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return new Compilation(
                null,
                newCompilationDto.isPinned(),
                newCompilationDto.getTitle(),
                events
        );
    }

    public static CompilationDto toCompilationDto(Compilation compilation, Map<Long, List<EventShortDto>> eventsMap) {
        return new CompilationDto(
                eventsMap.get(compilation.getId()),
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle()
                );
    }

    public static CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {
        return new CompilationDto(
                events,
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle()
        );
    }

    public static List<CompilationDto> toCompilationsDto(List<Compilation> compilations,
                                                         Map<Long, List<EventShortDto>> eventsMap) {
        return compilations.stream().map(x -> toCompilationDto(x, eventsMap)).collect(Collectors.toList());
    }
}
