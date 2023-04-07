package ru.practicum.ewmservice.private_service.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.dao.events.EventsRepository;
import ru.practicum.ewmservice.dao.requests.RequestsRepository;
import ru.practicum.ewmservice.dao.users.UsersRepository;
import ru.practicum.ewmservice.dto.requests.ParticipationRequestDto;
import ru.practicum.ewmservice.exception.ConflictObjectException;
import ru.practicum.ewmservice.exception.NotObjectException;
import ru.practicum.ewmservice.mapper.RequestsMapper;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.requests.Request;
import ru.practicum.ewmservice.model.user.User;
import ru.practicum.ewmservice.status.State;
import ru.practicum.ewmservice.status.Status;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RequestsPrivateServiceImpl implements RequestsPrivateService {
    private final RequestsRepository requestsRepository;
    private final UsersRepository usersRepository;
    private final EventsRepository eventsRepository;

    @Override
    public List<ParticipationRequestDto> findByUser(Long userId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotObjectException("no user in a base data"));
        List<Request> requests = requestsRepository.findByRequester(user);
        log.info("get request in count:{}, to user:{} / RequestsPrivateService", requests.size(), user.getName());
        return RequestsMapper.toParticipationRequestsDto(requests);
    }

    @Transactional
    @Override
    public ParticipationRequestDto add(Long userId, Long eventId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotObjectException("нет пользователя"));
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotObjectException("нет события"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictObjectException("событие неопубликовано");
        }
        if (event.getInitiator().equals(user)) {
            throw new ConflictObjectException("инициатор события не может отправлять запрос");
        }
        List<Request> requestsUser = requestsRepository.findByRequesterAndEvent(user, event);
        if (requestsUser.isEmpty()) {
            Request request;
            List<Request> requests = requestsRepository
                    .findByEventAndStatusIn(event, List.of(Status.CONFIRMED));
            if (requests.size() >= event.getParticipantLimit()) {
                event.setNotAvailable(true);
                throw new ConflictObjectException("достигнут лимит запросов");
            }
            if (event.isRequestModeration()) {
                    request = RequestsMapper.toRequest(user, event, Status.PENDING);
                } else {
                request = RequestsMapper.toRequest(user, event, Status.CONFIRMED);
            }
            log.info("post request to event:{}, user:{} / RequestsPrivateService", event.getTitle(), user.getName());
            return RequestsMapper.toParticipationRequestDto(requestsRepository.save(request));
        } else {
            throw new ConflictObjectException("запрос на участие уже зарегистрирован");
        }
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotObjectException("no user in a base data"));
        Request request = requestsRepository.findByRequesterAndId(user, requestId)
                .orElseThrow(() -> new NotObjectException("no request in a base data"));
        request.setStatus(Status.CANCELED);
        log.info("cancel request to event:{}, user:{} / RequestsPrivateService",
                request.getEvent().getTitle(), user.getName());
        return RequestsMapper.toParticipationRequestDto(request);
    }
}
