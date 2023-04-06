package ru.practicum.ewmservice.model.location;

import lombok.Data;

import java.io.Serializable;

@Data
public class LocationId implements Serializable {
    private Float lat;
    private Float lon;

}
