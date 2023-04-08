package ru.practicum.ewmservice.admin_service.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.admin_service.compilations.service.CompilationsAdminService;
import ru.practicum.ewmservice.dto.compilation.NewCompilationDto;
import ru.practicum.ewmservice.dto.compilation.UpdateCompilationRequest;

@Controller
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationsAdminController {
    private final CompilationsAdminService service;

    @PostMapping
    public ResponseEntity<Object> add(
            @Validated @RequestBody NewCompilationDto compilation
    ) {
        log.info("Post compilation");
        return new ResponseEntity<>(service.add(compilation), HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> remove(
            @PathVariable Long compId
    ) {
        log.info("Delete compilation");
        service.remove(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<Object> edit(
            @PathVariable Long compId,
            @Validated @RequestBody UpdateCompilationRequest compilation
    ) {
        log.info("Edit compilation");
        return new ResponseEntity<>(service.edit(compId, compilation), HttpStatus.OK);
    }
}
