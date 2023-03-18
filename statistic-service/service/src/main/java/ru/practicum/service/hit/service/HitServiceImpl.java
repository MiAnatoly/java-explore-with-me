package ru.practicum.service.hit.service;

import dto.HitDto;
import dto.ViewStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.hit.HitMapper;
import ru.practicum.service.hit.dao.HitRepository;
import ru.practicum.service.hit.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {
    private final HitRepository hitRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    @Override
    public void add(HitDto hitDto) {
        LocalDateTime created = LocalDateTime.parse(hitDto.getTimestamp(), dateFormatter);
        EndpointHit hit = HitMapper.toHit(created, hitDto);
        hit = hitRepository.save(hit);
        log.info("добавлен запрос id:" + hit.getId() + " + app:" + hit.getApp() + " ip:" + hit.getIp());
    }

    @Override
    public List<ViewStats> find(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(start, dateFormatter);
        LocalDateTime endTime = LocalDateTime.parse(end, dateFormatter);
        if (unique) {
            return hitRepository.findUniqueTrue(startTime, endTime, uris);
        } else {
            return hitRepository.findUniqueFalse(startTime, endTime, uris);
        }
    }
}
