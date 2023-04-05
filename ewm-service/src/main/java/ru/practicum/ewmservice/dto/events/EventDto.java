package ru.practicum.ewmservice.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewmservice.dto.categories.CategoryDto;
import ru.practicum.ewmservice.dto.users.UserShortDto;
import ru.practicum.ewmservice.model.location.Location;
import ru.practicum.ewmservice.status.State;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private Boolean paid;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private String description;
    private Integer participantLimit;
    private State state;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private Location location;
    private Boolean requestModeration;
}
