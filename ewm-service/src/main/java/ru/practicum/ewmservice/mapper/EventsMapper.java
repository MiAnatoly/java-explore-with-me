package ru.practicum.ewmservice.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewmservice.dto.events.*;
import ru.practicum.ewmservice.model.categories.Category;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.location.Location;
import ru.practicum.ewmservice.model.user.User;
import ru.practicum.ewmservice.status.State;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventsMapper {
    public static Event toEvent(User user, Category category, Location location, NewEventDto newEventDto) {
        if (newEventDto.getParticipantLimit() == null) {
            newEventDto.setParticipantLimit(0);
        }
        return new Event(
                newEventDto.getAnnotation(),
                category,
                LocalDateTime.now(),
                newEventDto.getDescription(),
                newEventDto.getEventDate(),
                user,
                location,
                newEventDto.isPaid(),
                newEventDto.getParticipantLimit(),
                null,
                newEventDto.isRequestModeration(),
                State.PENDING,
                newEventDto.getTitle(),
                null,
                false
        );
    }

    public static EventFullDto toEventFullDto(Event event, Long views, Long confirmedRequests) {
        return new EventFullDto(
                event.getAnnotation(),
                CategoriesMapper.toCategoryDto(event.getCategory()),
                confirmedRequests,
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                event.getId(),
                UsersMapper.toUserShortDto(event.getInitiator()),
                LocationMapper.toLocationDto(event.getLocation()),
                event.isPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getState(),
                event.getTitle(),
                views,
                event.isRequestModeration()
        );
    }

    public static EventDto toEventDto(Event event) {
        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                CategoriesMapper.toCategoryDto(event.getCategory()),
                event.isPaid(),
                event.getEventDate(),
                UsersMapper.toUserShortDto(event.getInitiator()),
                event.getDescription(),
                event.getParticipantLimit(),
                event.getState(),
                event.getCreatedOn(),
                LocationMapper.toLocationDto(event.getLocation()),
                event.isRequestModeration()
        );
    }

    public static EventShortDto toEventShortDto(Long confirmedRequests, Long views, Event event) {
        return new EventShortDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                CategoriesMapper.toCategoryDto(event.getCategory()),
                event.isPaid(),
                confirmedRequests,
                event.getEventDate(),
                UsersMapper.toUserShortDto(event.getInitiator()),
                views
        );
    }

}
