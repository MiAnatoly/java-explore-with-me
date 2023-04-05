package ru.practicum.ewmservice.public_service.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewmservice.public_service.compilations.service.CompilationPublicService;
import ru.practicum.ewmservice.valide.ValidPage;

import javax.validation.constraints.Min;

@Controller
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CompilationsPublicController {
    private final CompilationPublicService service;

    @GetMapping
    public ResponseEntity<Object> findAll(
            @RequestParam(value = "pinned", defaultValue = "false") Boolean pinned,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size
    ) {
        int page = ValidPage.page(from, size);
        log.info("get findAll compilations");
        return new ResponseEntity<>(service.findAll(pinned, page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(
            @PathVariable Long id
    ) {
        log.info("get findId {} compilation", id);
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }
}
