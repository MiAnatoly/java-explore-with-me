package ru.practicum.ewmservice.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewmservice.dto.requests.EventRequestStatusUpdateResult;
import ru.practicum.ewmservice.dto.requests.ParticipationRequestDto;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.requests.Request;
import ru.practicum.ewmservice.model.user.User;
import ru.practicum.ewmservice.status.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestsMapper {

    public static Request toRequest(User user, Event event, Status status) {
        return new Request(
                null,
                LocalDateTime.now(),
                event,
                user,
                status
        );
    }

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getCreated(),
                request.getEvent().getId(),
                request.getId(),
                request.getRequester().getId(),
                request.getStatus()
        );
    }

    public static EventRequestStatusUpdateResult toEventRequestStatusUpdateResult(
            List<ParticipationRequestDto> confirmedRequests, List<ParticipationRequestDto> rejectedRequests) {
        return new EventRequestStatusUpdateResult(
                confirmedRequests,
                rejectedRequests
        );
    }

    public static List<ParticipationRequestDto> toParticipationRequestsDto(List<Request> requests) {
        return requests.stream().map(RequestsMapper::toParticipationRequestDto).collect(Collectors.toList());
    }
}
