package client;

import dto.HitDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HitController {
    private final HitClient hitClient;

    @PostMapping(path = "/hit")
    public ResponseEntity<Object> add(@Validated @RequestBody HitDto hitDto) {
        log.info("Post statistic");
        return hitClient.add(hitDto);
    }

    @GetMapping(path = "/stats")
    public ResponseEntity<Object> find(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam (defaultValue = "false") Boolean unique) {
        log.info("Get statics");
        return hitClient.find(start, end, uris, unique);
    }

}
