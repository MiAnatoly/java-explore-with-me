package ru.practicum.ewmservice.admin_service.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.admin_service.users.service.UserAdminService;
import ru.practicum.ewmservice.dto.users.NewUserRequest;
import ru.practicum.ewmservice.valide.ValidPage;

import javax.validation.constraints.Min;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
@Validated
@Slf4j
@RequiredArgsConstructor
public class UsersAdminController {
private final UserAdminService service;

    @GetMapping
    public ResponseEntity<Object> findAll(
            @RequestParam List<Long> ids,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) Integer size
    ) {
        int page = ValidPage.page(from, size);
        log.info("Get users with from={} and size={}", from, size);
        return new ResponseEntity<>(service.findAll(ids, page, size), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> add(
            @Validated @RequestBody NewUserRequest user
    ) {
        return new ResponseEntity<>(service.add(user), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> remove(
            @PathVariable Long userId
    ) {
        log.info("Delete user with userId={}", userId);
        service.remove(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
