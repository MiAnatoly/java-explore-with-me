package ru.practicum.ewmservice.admin_service.compilations.service;

import ru.practicum.ewmservice.dto.compilation.CompilationDto;
import ru.practicum.ewmservice.dto.compilation.NewCompilationDto;

public interface CompilationsAdminService {

    CompilationDto add(NewCompilationDto compilation);

    void remove(Long compId);

    CompilationDto edit(Long compId, NewCompilationDto compilation);
}
