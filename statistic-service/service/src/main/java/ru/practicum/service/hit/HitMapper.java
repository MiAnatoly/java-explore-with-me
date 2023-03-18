package ru.practicum.service.hit;

import dto.HitDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.service.hit.model.EndpointHit;

import java.time.LocalDateTime;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HitMapper {

    public static EndpointHit toHit(LocalDateTime created, HitDto hitDto) {
        return new EndpointHit(
                null,
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                created
        );
    }
}
