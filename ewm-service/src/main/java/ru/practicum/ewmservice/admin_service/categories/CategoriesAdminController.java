package ru.practicum.ewmservice.admin_service.categories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.admin_service.categories.service.CategoriesAdminService;
import ru.practicum.ewmservice.dto.categories.NewCategoryDto;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@Slf4j
public class CategoriesAdminController {
    private final CategoriesAdminService service;

    @PostMapping
    public ResponseEntity<Object> add(
            @Validated  @RequestBody NewCategoryDto category,
            HttpServletRequest request
    ) {
        log.info("Post category with name={}", category.getName());
        return new ResponseEntity<>(service.add(category, request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> delete(
            @PathVariable Long catId,
            HttpServletRequest request
    ) {
        log.info("Delete category with catId={}", catId);
        service.delete(catId, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<Object> edit(
            @PathVariable Long catId,
            @Validated @RequestBody NewCategoryDto category,
            HttpServletRequest request
    ) {
        log.info("Patch category with id={} name={}", catId, category.getName());
        return new ResponseEntity<>(service.edit(catId, category, request), HttpStatus.OK);
    }


}
