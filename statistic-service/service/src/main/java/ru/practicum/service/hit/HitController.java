package ru.practicum.service.hit;

import dto.HitDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.hit.service.HitService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HitController {
    private final HitService hitService;

    @PostMapping(path = "/hit")
    public ResponseEntity<Object> add(@Validated @RequestBody HitDto hitDto) {
        hitService.add(hitDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/stats")
    public ResponseEntity<Object> find(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Get statics service controller");
        return new ResponseEntity<>(hitService.find(start, end, uris, unique), HttpStatus.OK);
    }
}
