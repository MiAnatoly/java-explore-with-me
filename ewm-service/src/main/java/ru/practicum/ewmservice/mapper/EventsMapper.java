package ru.practicum.ewmservice.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewmservice.dto.events.*;
import ru.practicum.ewmservice.exception.ConflictObjectException;
import ru.practicum.ewmservice.model.categories.Category;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.user.User;
import ru.practicum.ewmservice.status.State;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventsMapper {
    public static Event toEvent(User user, Category category, NewEventDto newEventDto) {
        if (newEventDto.getPaid() == null) {
            newEventDto.setPaid(false);
        }
        if (newEventDto.getParticipantLimit() == null) {
            newEventDto.setParticipantLimit(0);
        }
        if (newEventDto.getRequestModeration() == null) {
            newEventDto.setRequestModeration(true);
        }
        if (newEventDto.getTitle().length() < 3 && newEventDto.getTitle().length() > 120) {
            throw new ConflictObjectException("заголовок должен быть брльше 3 и меньше 120 символов");
        }
        return new Event(
                null,
                newEventDto.getAnnotation(),
                category,
                LocalDateTime.now(),
                newEventDto.getDescription(),
                newEventDto.getEventDate(),
                user,
                newEventDto.getLocation(),
                newEventDto.getPaid(),
                newEventDto.getParticipantLimit(),
                null,
                newEventDto.getRequestModeration(),
                State.PENDING,
                newEventDto.getTitle(),
                null
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
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getState(),
                event.getTitle(),
                views,
                event.getRequestModeration()
        );
    }

    public static EventDto toEventDto(Event event) {
        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                CategoriesMapper.toCategoryDto(event.getCategory()),
                event.getPaid(),
                event.getEventDate(),
                UsersMapper.toUserShortDto(event.getInitiator()),
                event.getDescription(),
                event.getParticipantLimit(),
                event.getState(),
                event.getCreatedOn(),
                event.getLocation(),
                event.getRequestModeration()
        );
    }

    public static EventShortDto toEventShortDto(Long confirmedRequests, Long views, Event event) {
        return new EventShortDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                CategoriesMapper.toCategoryDto(event.getCategory()),
                event.getPaid(),
                confirmedRequests,
                event.getEventDate(),
                UsersMapper.toUserShortDto(event.getInitiator()),
                views
        );
    }

}
