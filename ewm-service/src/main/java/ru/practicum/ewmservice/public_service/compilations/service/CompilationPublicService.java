package ru.practicum.ewmservice.public_service.compilations.service;

import ru.practicum.ewmservice.dto.compilation.CompilationDto;

import java.util.List;

public interface CompilationPublicService {

    List<CompilationDto> findAll(Boolean pinned, Integer page, Integer size);

    CompilationDto findById(Long id);

}
