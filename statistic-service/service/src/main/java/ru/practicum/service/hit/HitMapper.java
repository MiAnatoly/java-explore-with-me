package ru.practicum.service.hit;

import dto.HitDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.service.hit.model.EndpointHit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HitMapper {

    public static EndpointHit toHit(HitDto hitDto) {
        return new EndpointHit(
                null,
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                hitDto.getTimestamp()
        );
    }
}
