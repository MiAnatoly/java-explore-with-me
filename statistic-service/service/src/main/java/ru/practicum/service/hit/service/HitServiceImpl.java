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
import java.util.List;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {
    private final HitRepository hitRepository;

    @Transactional
    @Override
    public void add(HitDto hitDto) {
        EndpointHit hit = HitMapper.toHit(hitDto);
        hit = hitRepository.save(hit);
        log.info("добавлен запрос id:{}, app:{}, ip:{}", hit.getId(), hitDto.getApp(), hitDto.getIp());
    }

    @Override
    public List<ViewStats> find(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique) { // уникальный пользователь
           if (uris == null) {
                if (start == null || end == null) {
                    return hitRepository.findUniqueTrueWithoutParam();
                } else {
                    return hitRepository.findUniqueTrueWithoutUris(start, end);
                }
            } else {
                if (start == null || end == null) {
                    return hitRepository.findUniqueTrueWithoutDate(uris);
                } else {
                    return hitRepository.findUniqueTrue(start, end, uris);
                }
            }
        } else {
            if (uris == null) {
                if (start == null || end == null) {
                    return hitRepository.findUniqueFalseWithoutParam();
                } else {
                    return hitRepository.findUniqueFalseWithoutUris(start, end);
                }
            } else {
                if (start == null || end == null) {
                    return hitRepository.findUniqueFalseWithoutDate(uris);
                } else {
                    return hitRepository.findUniqueFalse(start, end, uris);
                }
            }
        }
    }
}
