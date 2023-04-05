package ru.practicum.ewmservice.public_service.categories;

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
import ru.practicum.ewmservice.public_service.categories.service.CategoriesPublicService;
import ru.practicum.ewmservice.valide.ValidPage;

import javax.validation.constraints.Min;

@Controller
@RequestMapping("categories")
@Validated
@RequiredArgsConstructor
@Slf4j
public class CategoriesPublicController {
    private final CategoriesPublicService service;

    @GetMapping
    public ResponseEntity<Object> findAll(
            @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size
    ) {
        int page = ValidPage.page(from, size);
        log.info("Get Categories with from={} and size={}", from, size);
        return new ResponseEntity<>(service.findAll(page, size), HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<Object> findById(
            @PathVariable Long catId
    ) {
        log.info("Get Category with catId={}", catId);
        return new ResponseEntity<>(service.findById(catId), HttpStatus.OK);
    }
}
